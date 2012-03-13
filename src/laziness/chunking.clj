(ns laziness.chunking)

;;~~~~~~~~~~
;; Chunking
;;~~~~~~~~~~

;;==================
;; range is chunked
;;==================

(def chunked-range (range 64))

;; (first (map #(doto % println) (seq chunked-range)))


;;=============================
;; PersistentVector is chunked
;;=============================

(def chunked-vector (vec (range 64)))

;; (first (map #(doto % println) (seq chunked-vector)))
