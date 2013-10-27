(ns lein-sql.core
  (:import [java.sql DriverManager])
  (:require [clojure.java.jdbc :as jdbc]))

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

(defn execute
  "Execute SQL query

  Options:

  :connection-uri   Connection URL
  :driver           JDBC driver class
  :username         Username
  :password         Password

  :query            SQL query to execute

  1. Using JDBC url
  lein sql :query \"CREATE DATABASE my_db\" :connection-uri jdbc:postgresql://localhost/ :driver org.postgresql.Driver
  "
  [project args]
  (when-let [q (get-query args)]
    (with-open [c (get-connection args)]
       (with-open [s (.createStatement c)]
         (.execute s (:query args))
         ))))

