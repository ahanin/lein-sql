(ns lein-sql.cmdarg)

(def arg-name-expr #"^--?([a-zA-Z0-9-]+)$")

(defn arg-name
  [s]
  (if s
    (second (re-matches arg-name-expr s))))

(defn arg-name?
  [s]
  (boolean (arg-name s)))

(defn arg-pairs
  [s]
  (when-let [s (seq s)]
    (let [[head tail] (split-with arg-name? s)]
      (cons (if head [(keyword (arg-name (last head))) (first tail)])
            (lazy-seq (arg-pairs (drop-while #(not (arg-name? %)) tail)))))))

(defn parse
  [arglist]
  (apply hash-map (into [] (flatten (arg-pairs arglist)))))

