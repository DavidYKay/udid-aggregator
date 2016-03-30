(ns udid-aggregator.core
  (:require [compojure.api.sweet :refer [GET defapi]]
            [ring.util.http-response :refer [ok]]
            [taoensso.carmine :as car :refer (wcar)]))

(def server1-conn {:pool {}
                   :spec {:host "127.0.0.1"
                          :port 6379}})
(defmacro wcar* [& body] `(car/wcar server1-conn ~@body))

(defn hello-redis []
  (wcar* (car/ping)
         (car/set "foo" "bar")
         (car/get "foo")))

(defapi app
  (GET "/hello" []
    :query-params [name :- String]
    (ok {:message (str "Hello, " name)})))

