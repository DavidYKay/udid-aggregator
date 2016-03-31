(ns udid-aggregator.core-test
  (:require [cheshire.core :as cheshire]
            [midje.sweet :refer :all]
            [ring.mock.request :as mock]
            [udid-aggregator.handler :refer :all]))

(defn parse-body [body]
  (cheshire/parse-string (slurp body) true))

(facts "UDID CREATE-ID tests"
       (fact "handles the empty case"
             (let [response (app (-> (mock/request :post  "/create-id")))
                   body     (parse-body (:body response))]
               (:status response) => 200
               (:result body)    => 1))

       (fact "handles the semi-full case"
             (let [response (app (-> (mock/request :post  "/create-id")))
                   body     (parse-body (:body response))]
               (:status response) => 200
               (:result body)    => 3))

       (fact "handles the full case"
             (let [response (app (-> (mock/request :post  "/create-id")))
                   body     (parse-body (:body response))]
               (:status response) => 200
               (:result body)    => 3))
       )
