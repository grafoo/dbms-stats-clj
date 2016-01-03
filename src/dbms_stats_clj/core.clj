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
  [pageid]
  (clean-infolines (drop-last (subvec (str/split (re-find (re-pattern "(?s)\\{\\{Infobox.*?\\}\\}\n\n")
                                                          (get-in (json/read-str (:body (http-client/get (str "https://en.wikipedia.org/w/api.php?action=parse&section=0&prop=wikitext&format=json&redirects=true&pageid=" pageid))))
                                                                  ["parse" "wikitext" "*"]))
                                                 #"\n")
                                      1))))

(defn get-rdbms-systems-pagetitles
  "asdf"
  []
  (map #(get % 0) (map #(str/split % #"\|") (map #(str/replace % #"[\*\[\]\n]" "")
                                          (re-seq #"\*.*?\n"
                                                  (get-in (json/read-str (:body (http-client/get "https://en.wikipedia.org/w/api.php?action=query&format=json&titles=List_of_relational_database_management_systems&prop=revisions&rvprop=content&rvsection=1")))
                                                          ["query" "pages" "1568820" "revisions" 0 "*"]))))))
(defn get-page-id
  "gets the corresponding pageid to a wikipedia title"
  [pagetitle]
  (keys (get-in (json/read-str (:body (http-client/get (str "https://en.wikipedia.org/w/api.php?action=query&format=json&titles=" pagetitle)))) ["query" "pages"])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  ;(get-infobox-content "244884")
  ;(get-page-id "4th Dimension (Software)")
  (def pageids (pmap #(get-page-id %) (get-rdbms-systems-pagetitles)))
  (map #(println (type %)) pageids))
