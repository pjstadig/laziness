(ns laziness.case-study2
  (:require [laziness.ambit :refer [ambit]]))

;; We have 100MB total heap space.

(def one-meg 1000000)

(defn everything-is-good-here []
  (dorun (map (fn [_] (byte-array one-meg)) (ambit 1000)))
  :done)

;; (everything-is-good-here)

(defn holding-onto-head []
  (let [s (map (fn [_] (byte-array one-meg)) (ambit 1000))]
    (dorun s)
    ;; prevents locals clearing
    (identity s)
    :done))

;; (holding-onto-head)

(defn locals-clearing-saves-the-day []
  (let [s (map (fn [_] (byte-array one-meg)) (ambit 1000))]
    (dorun s)
    :done))

;; (locals-clearing-saves-the-day)

(def ten-meg 10000000)

(defn without-chunking-this-is-good []
  (dorun (map (fn [_] (byte-array ten-meg)) (ambit 10)))
  :done)

;; (without-chunking-this-is-good)

(defn chunked-to-death []
  (dorun (map (fn [_] (byte-array ten-meg)) (range 10)))
  :done)

;; (chunked-to-death)
