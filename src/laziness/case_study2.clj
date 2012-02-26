(ns laziness.case-study2)

;; We have 100MB total heap space.

(def one-meg 1000000)

(defn everything-is-good-here []
  (dorun (map (fn [_] (byte-array one-meg)) (take 1000 (iterate inc 0)))))

;; (everything-is-good-here)

(defn holding-onto-head []
  (let [s (map (fn [_] (byte-array one-meg)) (take 1000 (iterate inc 0)))]
    (dorun s)
    (identity s)
    nil))

;; (holding-onto-head)

(defn locals-clearing-saves-the-day []
  (let [s (map (fn [_] (byte-array one-meg)) (take 1000 (iterate inc 0)))]
    (dorun s)
    nil))

;; (locals-clearing-saves-the-day)

(def ten-meg 10000000)

(defn without-chunking-this-is-good []
  (dorun (map (fn [_] (byte-array ten-meg)) (take 10 (iterate inc 0)))))

;; (without-chunking-this-is-good)

(defn chunked-to-death []
  (dorun (map (fn [_] (byte-array ten-meg)) (range 10))))

;; (chunked-to-death)
