(ns lein-sql.t-core
  (:use midje.sweet)
  (:require [lein-sql.core :as core]))

(fact "path-pattern-string transforms a path pattern to a regexp string"
  (core/path-pattern-string "**/*.sql") => "^.*.*\\.sql$"
    )

(fact "path-pattern build a regexp that matches path string"
  (re-matches (core/make-path-pattern "**/*.sql") "a/b/c/file.sql") => "a/b/c/file.sql"
  (re-matches (core/make-path-pattern "a/**/*.sql") "a/b/c/file.sql") => "a/b/c/file.sql"
  (re-matches (core/make-path-pattern "**/*.sql") "file.sql") => "file.sql"
  (re-matches (core/make-path-pattern "**/b/*.sql") "a/b/c/file.sql") => "a/b/c/file.sql"
    )
