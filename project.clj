(defproject udid-aggregator "0.1.0-SNAPSHOT"
  :description "A simple server to aggregate users into buckets for HIPAA-avoiding de-identification"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.3.1"]
                 [metosin/compojure-api "1.0.2"]
                 [environ "1.0.0"]
                 [com.taoensso/carmine "2.12.2"]]

  :plugins [[lein-environ "1.0.0"]]
  :uberjar-name "udid-aggregator.jar")
