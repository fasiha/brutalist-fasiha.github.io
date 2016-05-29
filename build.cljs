#!/usr/bin/env planck
; fswatch -0 *.html | xargs -0 -n 1 -I {} planck build.cljs

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
                              (planck.repl/eval `(let [~'params ~params] ~expr)
                                                 (.-name *ns*)))
                            line)))
                   (string/join "\n"))]
    (spit (str output-path (:file params))
          baked)))

(println "Juicing…")
(doseq [params [{:title "top *** aldebrn.me" :file "index.html"} 
                {:title "about *** aldebrn.me" :file "about.html"} 
                {:title "code *** aldebrn.me" :file "code.html"}]]
  (proc params template-file "baked/"))

; (let [bindings '[x 1]] (eval `(let ~bindings ~(reader/read-string "(+ 1 x)"))))
; (eval `(let ~'[x 1] ~(reader/read-string "(+ 1 x)")))
; (let [params {:x 1}] (eval `(let [~'params ~params] ~(reader/read-string "(+ 1 (:x params))"))))


(def s "◊123.213 ... ◊(+ 4 1) ... ◊\"hi\" ...")
(map (juxt identity (partial read-string {:eof nil})) (string/split s #"◊"))

