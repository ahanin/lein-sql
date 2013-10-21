(ns lein-sql.t-cmdarg
  (:use midje.sweet)
  (:require [lein-sql.cmdarg :as cmdarg]))

(fact "`parse` parses command line arguments into a map"
  (cmdarg/parse ["--jdbc-url" "jdbc:postgresql://localhost/"]) => {:jdbc-url "jdbc:postgresql://localhost/"}
  (cmdarg/parse ["--jdbc-url" "--jdbc-url""jdbc:postgresql://localhost/"]) => {:jdbc-url "jdbc:postgresql://localhost/"}
    )
