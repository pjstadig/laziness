(ns laziness.seq)

(defn rest-is-never-nil []
  (map rest [[1 2] [1] [] nil]))

;; (rest-is-never-nil)

(defn first-can-be-nil []
  (map first [[1 2] [1] [] nil]))

;; (first-can-be-nil)

(defn next-is-never-empty []
  (map next [[1 2] [1] [] nil]))

;; (next-is-never-empty)

(defn next-is-rest-seq []
  (map (comp seq rest) [[1 2] [1] [] nil]))

;; (next-is-rest-seq)

(defn nil-pun []
  (loop [nums [1 2 3]]
    (when-let [f (first nums)]
      (do (println f)
          (recur (next nums))))))

;; (nil-pun)
