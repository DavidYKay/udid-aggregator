(ns udid-aggregator.handler
  (:require [compojure.api.sweet :refer [GET POST defapi]]
            [uuid-aggregator.core :refer [current-bucket-id]]
            [ring.util.http-response :refer [ok]]))

(defapi app
  (POST "/create-id" []
        (ok {:uuid (current-bucket-id)}))
  (GET "/hello" []
    :query-params [name :- String]
    (ok {:message (str "Hello, " name)})))
