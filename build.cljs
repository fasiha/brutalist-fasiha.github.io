#!/usr/bin/env planck
(require '[planck.io :refer [slurp spit]])
(require '[clojure.string :as string])
(require '[cljs.tools.reader :as reader])
(require '[planck.core :refer [eval]])

(def template-file "template.html")

(def template (->> template-file
                   slurp
                   string/split-lines))
(def files ["top.html"])
(doseq [file files]
  (->> template
       (map (fn [s] 
              (if (re-find #"◊" s)
                (slurp file)
                s)))
       (string/join "\n")
       (spit (str "baked/" file))
       ))

; (->> template
;      (filter #(re-find #"^ *◊" %))
;      (map #(string/replace % #"◊" ""))
;      (map reader/read-string)
;      (map eval)
;      )


