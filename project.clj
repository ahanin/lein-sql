(defproject lein-sql "0.1.0-SNAPSHOT"
  :description "Leiningen SQL plugin"
  :url "http://github.com/ahanin/lein-sql"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [leinjacker "0.4.2"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]]
  :profiles {:dev {:dependencies [[midje "1.5.1" :exclusions [org.clojure/clojure]]]
                   :plugins [[lein-midje "3.1.1"]]}}
  :eval-in-leiningen true)
