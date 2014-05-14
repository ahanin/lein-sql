(ns lein-sql.t-parser
  (:use midje.sweet)
  (:require [lein-sql.parser :as p]))

(fact "path-pattern-string transforms a path pattern to a regexp string"
  (p/sql-seq "
             SELECT * FROM fruits
             ;
             SELECT * FROM animals
             ") => ["SELECT * FROM fruits" "SELECT * FROM animals"]
  (p/sql-seq "
             -- DROP DATABASE IF EXISTS testdb;
             CREATE DATABASE testdb
             ") => ["-- DROP DATABASE IF EXISTS testdb" "CREATE DATABASE testdb"]
    )

