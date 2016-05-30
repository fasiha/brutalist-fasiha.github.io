#!/usr/bin/env planck
; fswatch -0 *.html | xargs -0 -n 1 -I {} planck build.cljs

(ns build.core
  (:require [planck.core :refer [slurp spit]]
            [planck.repl :refer [eval]]
            [clojure.string :as string]
            [cljs.tools.reader :as reader]
            [cljs.tools.reader.reader-types :as rt]))

(def template-file "template.html")

(defn my-eval [s params]
  (let [pbr (rt/string-push-back-reader s)
        expr (reader/read pbr)]
    (into [(eval `(let [~'params ~params] ~expr)
                 (.-name *ns*))]
          (take-while #(not (nil? %))
                      (repeatedly #(rt/read-char pbr))))))
(defn evaluate-lozenges [s re params]
  (let [[f & r] (string/split s re)]
    (apply str (into [f] (mapcat #(my-eval % params)) r))))

(defn proc [params template-file output-path]
  (let [baked (-> template-file
                      slurp
                      (evaluate-lozenges #"(?:^|[^\\])◊"
                                         params))]
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


;;; Example:

(def s2 "yo ◊[:b \"Ahmed\"] you are #◊(* 10 (:x params))! Magic via \\◊s.")
(def res2 (evaluate-lozenges s2 #"(?:^|[^\\])◊" {:x 123})) res2

