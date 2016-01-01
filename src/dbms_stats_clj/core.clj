(ns dbms-stats-clj.core
  (:gen-class)
  (:require
    [clj-http.client :as http-client]
    [clojure.data.json :as json]
    [clojure.string :as str]))

(defn map-infopair
  "Map key value pairs of Infobox items"
  [infopair]
  (if (contains? #{"developer"
                   "released"
                   "frequently updated"
                   "operating system"
                   "website"
                   "license"}
                 (get infopair 0))
    (println (zipmap [:key :val] infopair))))

(defn clean-infolines
  "strip infolines from whitespace and other clutter"
  [infolines]
  (for [infoline infolines]
    (do
      (def infopair (str/split (str/replace infoline #"^\| " "") #"="))
      (if (= (count infopair) 2)
        (map-infopair (vector (str/trim (get infopair 0)) (str/trim (get infopair 1))))))))

(defn get-infobox-content
  "gets infobox content from wikipedia page"
  [url]
  (clean-infolines (drop-last (subvec (str/split (re-find (re-pattern "(?s)\\{\\{Infobox.*?\\}\\}\n\n")
                                                          (get-in (json/read-str (:body (http-client/get url)))
                                                                  ["parse" "wikitext" "*"]))
                                                 #"\n")
                                      1))))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (get-infobox-content "https://en.wikipedia.org/w/api.php?action=parse&pageid=244884&section=0&prop=wikitext|parsetree&format=json"))
