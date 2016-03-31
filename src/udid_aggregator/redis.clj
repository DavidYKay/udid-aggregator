(ns udid-aggregator.redis
  (:require [taoensso.carmine :as car :refer (wcar)]))

(def max-bucket-size 5)

(def server1-conn {:pool {}
                   :spec {:host "127.0.0.1"
                          :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn- hello-redis []
  (wcar* (car/ping)
         (car/set "foo" "bar")
         (car/get "foo")))


(defn- uuid []
  (str (java.util.UUID/randomUUID)))

(defn- current-bucket []
  (wcar* (car/get "current")))

(defn- create-bucket []
  (wcar* (let [id (uuid)]
           (car/set "current" id)
           (car/set id 1))))

(defn- increment-bucket [{id :id}]
  (wcar* (car/incr id 1)))

(defn- init-redis
  "on first run"
  []
  (create-bucket))

(defn current-bucket-id []
  (let [{cur-count :count cur-id :id :as current} (current-bucket)
        bucket (if (>= cur-count max-bucket-size)
                 (create-bucket)
                 (increment-bucket current))]
    (:id bucket)))
