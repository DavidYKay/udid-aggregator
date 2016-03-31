(ns udid-aggregator.core
  (:require [clojure.java.jdbc :as sql]))

(def max-bucket-size 5)

(def db {:subprotocol "postgresql"
         :subname "//127.0.0.1:5432/korma"
         :user "dk"
         :password "secret"})

(defn- uuid []
  (str (java.util.UUID/randomUUID)))

(defn- all-buckets
  "For debug only"
  []
  (sql/query db
             ["select * from buckets"]))

(defn- current-bucket
  []
  (first (sql/query db
                    ["select * from buckets where counter < ?" max-bucket-size])))

(defn- create-bucket []
  (sql/insert! db :buckets
               {:uuid (uuid)
                :counter 0}))

(defn- increment-bucket [{guid :id}]
  (sql/update! db :buckets
               {:counter inc}
               ["zip = ?" 94546]))

(defn- init-postgres
  "on first run"
  []
  (sql/db-do-commands db
                      (sql/create-table-ddl :buckets
                                            [:id :serial "PRIMARY KEY"]
                                            [:uuid :text] 
                                            [:counter :smallint]))
  (create-bucket))

(defn- drop-table []
  (sql/db-do-commands db
                      (sql/drop-table-ddl :buckets)))

(defn current-bucket-id []
  (let [{cur-count :counter :as current} (current-bucket)
        bucket (if (>= cur-count max-bucket-size)
                 (create-bucket)
                 (increment-bucket current))]
    (:uuid bucket)))
