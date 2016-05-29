#!/usr/bin/env planck
(ns build.core
  (:require [planck.core :refer [slurp spit eval]]
            [clojure.string :as string]
            [cljs.tools.reader :as reader]))

(def template-file "template.html")

(defn proc [params template-file output-path]
  (let [template (->> template-file
                      slurp
                      string/split-lines)
        baked (->> template
                   (map (fn [line]
                          (if (re-find #"^ *◊" line)
                            (let [expr (reader/read-string (string/replace line #"◊" ""))]
                              (eval `(let [~'params ~params] ~expr)))
                            line)))
                   (string/join "\n"))]
    (spit (str output-path (:file params))
          baked)))

(proc {:title "top" :file "top.html"} template-file "baked/")

; (let [bindings '[x 1]] (eval `(let ~bindings ~(reader/read-string "(+ 1 x)"))))
; (eval `(let ~'[x 1] ~(reader/read-string "(+ 1 x)")))
; (let [params {:x 1}] (eval `(let [~'params ~params] ~(reader/read-string "(+ 1 (:x params))"))))
