(defproject udid-aggregator "0.1.0-SNAPSHOT"
  :description "A simple server to aggregate users into buckets for HIPAA-avoiding de-identification"
  :url "http://github.com/DavidYKay/udid-aggregator"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [ring "1.3.1"]
                 [metosin/compojure-api "1.0.2"]
                 [environ "1.0.0"]
                 [org.clojure/java.jdbc "0.5.0"]
                 ;; Example for postgres:
                 ;[org.postgresql/postgresql "9.2-1002-jdbc4"]
                 ;[org.postgresql/postgresql "9.4.1208.jre7"]
                 [org.postgresql/postgresql "9.4-1206-jdbc42"]
                 
                 ;; Example for sqlite:
                 [org.xerial/sqlite-jdbc "3.7.15-M1"]
                 [com.taoensso/carmine "2.12.2"]]

  :profiles {:dev {:dependencies [[cheshire "5.5.0"]
                                  [midje "1.8.3"]
                                  [ring/ring-mock "0.3.0"]]
                   :plugins [[lein-midje "3.2"]
                             [lein-ring "0.9.7"]]}}
  :ring {:handler udid-aggregator.handler/app}
  :plugins [[lein-environ "1.0.0"]]
  :uberjar-name "udid-aggregator.jar")
