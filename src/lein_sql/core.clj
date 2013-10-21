(ns lein-sql.core)

(defmacro lein-sql-command [command & control-map]
  `(~(symbol (str "lein-sql.core/" command)) ~@control-map))

(defn execute
  "Execute SQL query

  Options:
  --jdbc-driver   JDBC driver class
  --jdbc-url      JDBC url
  --username      Connection username
  --password      Connection password

  --query         SQL query to execute

  1. Using JDBC url
  lein sql \"CREATE DATABASE my_db\" --jdbc-url jdbc:postgresql://localhost/ --jdbc-driver org.postgresql.Driver
  "
  [project args]
  (if (and (:jdbc-url args)
           (:jdbc-driver args)
           (:username args)
           (:password args)
           (:query args))
    (do
      (Class/forName (:jdbc-driver args))
      (with-open [c (java.sql.DriverManager/getConnection (:jdbc-url args))]
        (with-open [s (.createStatement c)]
          (.execute s (:query args))
          )))))

