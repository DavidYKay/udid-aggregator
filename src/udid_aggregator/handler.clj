(ns udid-aggregator.handler
  (:require [compojure.api.sweet :refer [GET POST defapi]]
            [ring.util.http-response :refer [ok]]
            [udid-aggregator.core :refer [current-bucket-id]]))

(defapi app
  (POST "/create-id" []
        (ok {:uuid (current-bucket-id)}))
  (GET "/hello" []
    :query-params [name :- String]
    (ok {:message (str "Hello, " name)})))
