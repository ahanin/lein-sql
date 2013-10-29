(ns lein-sql.core
  (:import [java.sql DriverManager])
  (:require [clojure.java.jdbc :as jdbc])
  (:require [clojure.string :as string]))

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

(defn path-pattern [path-pattern]
  (re-pattern (path-pattern-string path-pattern)))

(defn execute-files [file-pattern]
  (let [path-regexp (file-pattern)]
    (doseq [f (file-seq file-pattern)]
      (println f))))

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
  :files            Files from which to sequentially read and execute SQL statements

  1. Using JDBC url
  lein sql :query \"CREATE DATABASE my_db\" :connection-uri jdbc:postgresql://localhost/ :driver org.postgresql.Driver
  "
  [project args]
  (let [{:keys [query files]} args]
    (cond query (with-connection args (execute-query query))
          files (with-connection args (execute-files files))
          :else (throw (IllegalArgumentException. (format "%s missing a required argument"))
                       ))))

