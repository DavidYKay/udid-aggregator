(ns udid-aggregator.core
  (:require [clojure.java.jdbc :as sql]))

(def max-bucket-size (or (System/getenv "BUCKET_SIZE")
                         5))

(def db (or (System/getenv "DATABASE_URL")
            "postgresql://localhost:5432/korma"))

(defn- uuid []
  (str (java.util.UUID/randomUUID)))

(defn- all-buckets
  "For debug only"
  []
  (sql/query db
             ["select * from buckets"]))

(defn- current-bucket
  "Returns nil if there is no current bucket with capacity"
  []
  (first (sql/query db
                    ["select * from buckets where counter < ?" max-bucket-size])))


(defn- increment-bucket [{uuid :uuid}]
  (sql/execute! db
               ["UPDATE buckets SET counter = counter + 1 WHERE uuid = ?" uuid]))

(defn- drop-table []
  (sql/db-do-commands db
                      (sql/drop-table-ddl :buckets)))

(defn create-bucket [n]
  (first (sql/insert! db :buckets
                      {:uuid (uuid)
                       :counter n})))

(defn current-bucket-id []
  (let [{:keys [uuid] :as current} (current-bucket)]
    (if (nil? current)
      (:uuid (create-bucket 1))
      (do
        (increment-bucket current)
        uuid))))
