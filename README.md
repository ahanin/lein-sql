lein-sql
========

Leiningen plugin for executing SQL tasks.

![Travis CI Build Status](https://api.travis-ci.org/ahanin/lein-sql.png "Build Status")


Installation
------------

Use this for user-level plugins:

Put `[lein-sql "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your
`:user` profile, or if you are on Leiningen 1.x do `lein plugin install
lein-sql 0.1.0`.

Use this for project-level plugins:

Put `[lein-sql "0.1.0-SNAPSHOT"]` into the `:plugins` vector of your project.clj.

Usage
-----

To execute a query:

    $ lein sql execute :driver org.postgresql.Driver \
        :connection-uri jdbc:postgresql://localhost/ \
        :username postgres :password postgres \
        :query "CREATE DATABASE my_db"`

To run set of `.sql` files:

    $ lein sql execute :driver org.postgresql.Driver \
        :connection-uri jdbc:postgresql://localhost/ \
        :username postgres :password postgres \
        :directory foo/bar
        :includes **/*.sql
        :excludes **/templates/**/*.sql

License
-------

Copyright © 2013 Alexey Hanin

Distributed under the Eclipse Public License, the same as Clojure.
