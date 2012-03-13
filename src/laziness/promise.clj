(ns laziness.promise
  (:require [laziness.ambit :refer [ambit]]))

;;~~~~~~~~~~~~~~~~~~~~~~~~~
;; THE PROMISE OF LAZINESS
;;~~~~~~~~~~~~~~~~~~~~~~~~~

(defn nested-maps []
  (map str (map inc (ambit 5))))

;; (nested-maps)

(defn single-map-with-composed-fn []
  (map (comp str inc) (ambit 5)))

;; (single-map-with-composed-fn)

(defn fully-realized-inner-map []
  (map str (mapv inc (ambit 5))))

;; (fully-realized-inner-map)

(defn only-use-what-you-need []
  (take 5 (map str (map inc (ambit 5000000)))))

;; (only-use-what-you-need)
