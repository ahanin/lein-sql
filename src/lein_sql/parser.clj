(ns lein-sql.parser
  (:use [clojure.java.io :only [reader]])
  (:use [clojure.string :only [trim]])
  )

(def ^:dynamic *delimiter* \;)

(defn char-seq [^java.io.BufferedReader r]
  (when-let [c (.read r)]
    (if (> c -1)
      (cons (char c) (lazy-seq (char-seq r))))))

(defn sql-seq [input]
  (defn- sql-seq-int [r]
    ;;TODO parse better, e.g. consider delimiter as part of strings
    (let [delimiter (list *delimiter*)]
      (map #(->> % (apply str) trim)
        (filter #(not (= delimiter %))
          (partition-by #(not (= *delimiter* %)) (char-seq r))))))
  (let [reader (if (instance? String input) (java.io.BufferedReader. (java.io.StringReader. input)) input)]
    (sql-seq-int reader)))