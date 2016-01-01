(defproject dbms-stats-clj "0.1.0-SNAPSHOT"
  :description "statistics about popular database management systems"
  :url "https://github.com/mangra/dbms-stats-clj"
  :license {:name "The MIT License (MIT)"
            :url "http://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clj-http "2.0.0"]
                 [org.clojure/data.json "0.2.6"]]
  :main ^:skip-aot dbms-stats-clj.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
