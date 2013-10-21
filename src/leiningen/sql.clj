(ns leiningen.sql
  (:use [leiningen.core.eval :only [eval-in-project]])
  (:use [leiningen.help :only (subtask-help-for)])
  (:require [clojure.pprint :as pprint])
  (:require [clojure.set :as set])
  (:require [lein-sql.core :as core])
  (:require [lein-sql.cmdarg :as cmdarg]))

(defn help
  ([])
  ([sql-var]
   (println "lein-sql is a plugin for executing SQL tasks."
            (subtask-help-for nil sql-var))))

(defn ^{:subtasks [#'lein-sql.core/execute]}
  sql
  "Executes SQL tasks using JDBC interface"
  ([project]
   (help #'sql))
  ([project command & args]
   (let [control-map (cmdarg/parse args)
         project (leiningen.core.project/merge-profiles project [{:dependencies [['lein-sql "0.1.0-SNAPSHOT"]]}])]
     (eval-in-project project
                      `(core/lein-sql-command ~command '~project '~control-map)
                      '(do (require '[lein-sql.core :as core :only (lein-sql-command)])
                           )
                      ))))
