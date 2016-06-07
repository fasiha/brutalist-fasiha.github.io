(ns fullstack-cljs-tutorial.core
  (:require [sablono.core :as sab]
            [fullstack-cljs-tutorial.components :refer [like-seymore]]))

(defonce client-state (atom { :likes 0 }))

(defn render! [state-atom]
  (.render js/ReactDOM
           (like-seymore state-atom)
           (.getElementById js/document "app")))

(add-watch client-state :on-change (fn [_ state-atom _ _] (render! state-atom)))

(render! client-state)
