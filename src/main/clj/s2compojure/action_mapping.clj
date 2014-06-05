(ns s2compojure.action-mapping
    (:require [compojure.core :as compojure]))

(def action-routes (atom nil))

(defn add-routes [& routing]
  (if @action-routes
    (reset! action-routes (apply compojure/routes @action-routes routing))
    (reset! action-routes (apply compojure/routes routing))))

