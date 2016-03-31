(ns udid-aggregator.core
  (:require [clojure.java.jdbc :as sql]))

(def max-bucket-size 5)

(def db {:subprotocol "postgresql"
         :subname "//127.0.0.1:5432/korma"
         :user "dk"
         :password "secret"})

(defentity buckets)

(defn- uuid []
  (str (java.util.UUID/randomUUID)))

(defn- current-bucket []
  (sql/query db
             ["select * from buckets where count < ?" max-bucket-size]
             :row-fn :uuid))

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
                                            [:uuid :text] 
                                            [:count :smallint])))

(defn current-bucket-id []
  (let [{cur-count :count cur-id :id :as current} (current-bucket)
        bucket (if (>= cur-count max-bucket-size)
                 (create-bucket)
                 (increment-bucket current))]
    (:id bucket)))
