#!/usr/bin/env planck
; fswatch -0 -r src | xargs -0 -n 1 -I {} planck build.cljs

(ns build.core
  (:require [planck.core :refer [slurp spit]]
            [planck.repl :refer [eval]]
            [clojure.string :as string]
            [cljs.tools.reader :as reader]
            [cljs.tools.reader.reader-types :as rt]))

(def template-file "src/template.html")
(def input-directory "src")
(def output-directory "../master")

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
(defn evaluate-lozenges-recursive [s re params]
  (if (re-find re s)
    (recur (evaluate-lozenges s re params) re params)
    s))

(defn proc [params template-file output-directory]
  (let [baked (-> template-file
                      slurp
                      (evaluate-lozenges-recursive #"◊"
                                                   params))]
    (spit (str output-directory "/" (:file params))
          baked)))

; input directory is needed inside so template knows path
(doseq [params [{:path input-directory :title "top *** aldebrn.me" :file "index.html"}
                {:path input-directory :title "about *** aldebrn.me" :file "about.html"}
                {:path input-directory :title "code *** aldebrn.me" :file "code.html"}
                {:path input-directory :title "maps *** aldebrn.me" :file "maps.html"}
                {:path input-directory :title "Matrix expansion * code *** aldebrn.me" :file "code/mat-expando.html"}
                {:path input-directory :title "Webapps * code *** aldebrn.me" :file "code/cljs-intro.html"}
                {:path input-directory :title "Texture-shaded terrain *** aldebrn.me" :file "maps/texshade/index.html"}
                {:path input-directory :title "" :file "texshade/tutorial.html"}
                ]]
  (proc params template-file output-directory))
(println "Juicing…")

; (let [bindings '[x 1]] (eval `(let ~bindings ~(reader/read-string "(+ 1 x)"))))
; (eval `(let ~'[x 1] ~(reader/read-string "(+ 1 x)")))
; (let [params {:x 1}] (eval `(let [~'params ~params] ~(reader/read-string "(+ 1 (:x params))"))))


;;; Example:

(def s2 "yo ◊[:b \"Ahmed\"] you are #◊(* 10 (:x params))! Magic via ◊(:loz params)s.")
(def res2 (evaluate-lozenges s2 #"◊" {:x 123 :loz "◊"})) res2

