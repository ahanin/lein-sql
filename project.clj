(defproject lein-sql "0.1.0-SNAPSHOT"
  :description "Leiningen SQL plugin"
  :url "http://github.com/ahanin/lein-sql"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[leinjacker "0.4.2"]]
  :profiles {:dev {:plugins [[lein-midje "3.1.1"]]
                   :dependencies [[midje "1.5.1"]]}}
  :eval-in-leiningen true)