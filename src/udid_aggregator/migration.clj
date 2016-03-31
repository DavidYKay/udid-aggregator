(ns udid-aggregator.migration
  (:require [clojure.java.jdbc :as sql]
            [udid-aggregator.core :refer [create-bucket db]]))

(defn migrated? []
  (-> (sql/query db
                 [(str "select count(*) from information_schema.tables "
                       "where table_name='buckets'")])
      first :count pos?))

(defn migrate []
  (when (not (migrated?))
    (print "Creating database structure...") (flush)
    (sql/db-do-commands db
                        (sql/create-table-ddl :buckets
                                              [:id :serial "PRIMARY KEY"]
                                              [:uuid :text]
                                              [:counter :smallint]))
    (create-bucket 0)
    (println " done")))
