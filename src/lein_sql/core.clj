(ns lein-sql.core
  (:import [java.sql DriverManager])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [clojure.string :as string])
  (:use [clojure.java.io :only [as-file]]))

(defmacro lein-sql-command [command & control-map]
  `(~(symbol (str "lein-sql.core/" command)) ~@control-map))

(defn get-query [opts]
  (:query opts))

(defn get-connection [opts]
  (let [{:keys [driver connection-uri username password]} opts]
    (if driver (Class/forName driver))
    (cond
      (and connection-uri username password)
        (DriverManager/getConnection connection-uri username password)
      (and connection-uri)
        (DriverManager/getConnection connection-uri)
      :else (throw (IllegalArgumentException. (format "%s missing a required parameter" opts))))))

(def ^:dynamic *db-conn*)

(defn execute-query [query]
  (with-open [s (.createStatement *db-conn*)]
     (.execute s query)))

(defn re-quote [s]
  (let [special (set ".?*+^$[]\\(){}|")
    escfn #(if (special %) (str \\ %) %)]
      (apply str (map escfn s))))

(defn path-pattern-string [path-pattern]
  (str "^"
    (string/join
      (into []
        (for [x (re-seq #"(?:[\*]+/?|[^\*]+)" path-pattern)]
          (cond
            (re-matches #"\*{2,}/?" x) ".*"
            (re-matches #"\*" x) ".*"
            :else (re-quote x))
          )))
    "$"))

(defn make-path-pattern [s]
  (re-pattern (path-pattern-string s)))

(defn make-path-matcher [path-pattern]
  (let [regexp (make-path-pattern path-pattern)]
    (partial re-matches regexp)))

(defn execute-files [directory includes excludes]
  (let [including (if includes (make-path-matcher includes) (fn [s] (boolean true)))
        excluding (if excludes (make-path-matcher excludes) (fn [s] (boolean false)))]
    (doseq [f (file-seq (as-file directory))
            :let [path (.getPath f)]
            :when (and (.isFile f) (including path) (not (excluding path)))] [path]
      (println path)))
  ; TODO finish execution of files
  (println "File execution is still in progress... stay tuned."))

(defmacro with-connection [db-spec & body]
  `(with-open [conn# (get-connection ~db-spec)]
     (binding [*db-conn* conn#]
       ~@body)))

(defn execute
  "Execute SQL query

  Options:

  :connection-uri   Connection URL
  :driver           JDBC driver class
  :username         Username
  :password         Password

  :query            SQL query to execute

  :directory        Directory to scan for files containing SQL statements
  :includes         Including pattern
  :excludes         Excluding pattern

  1. Using JDBC url
      lein sql :query \"CREATE DATABASE my_db\" :connection-uri jdbc:postgresql://localhost/ :driver org.postgresql.Driver
  "
  [project args]
  (let [{:keys [query directory includes excludes]} args]
    (cond query (with-connection args (execute-query query))
          directory (with-connection args (execute-files directory includes excludes))
          :else (throw (IllegalArgumentException. (format "%s missing a required argument" args))
                       ))))

