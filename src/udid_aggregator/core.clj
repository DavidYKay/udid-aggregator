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

(defn- create-bucket [n]
  (first (sql/insert! db :buckets
                      {:uuid (uuid)
                       :counter n})))

(defn- increment-bucket [{uuid :uuid}]
  (sql/execute! db
               ["UPDATE buckets SET counter = counter + 1 WHERE uuid = ?" uuid]))

(defn- init-postgres
  "on first run"
  []
  (sql/db-do-commands db
                      (sql/create-table-ddl :buckets
                                            [:id :serial "PRIMARY KEY"]
                                            [:uuid :text] 
                                            [:counter :smallint]))
  (create-bucket 0))

(defn- drop-table []
  (sql/db-do-commands db
                      (sql/drop-table-ddl :buckets)))

(defn current-bucket-id []
  (let [{:keys [uuid] :as current} (current-bucket)]
    (if (nil? current)
      (:uuid (create-bucket 1))
      (do
        (increment-bucket current)
        uuid))))
