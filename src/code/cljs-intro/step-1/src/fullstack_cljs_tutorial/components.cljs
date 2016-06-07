(ns fullstack-cljs-tutorial.components
  (:require [sablono.core :as sab]))

(defn like-seymore [state-atom]
  (sab/html [:div
             [:h1 "Seymore's quantified popularity: " (:likes @state-atom)]
             [:div [:a {:href "#"
                        :onClick #(swap! state-atom update-in [:likes] inc)}
                    "Thumbs up"]]]))

