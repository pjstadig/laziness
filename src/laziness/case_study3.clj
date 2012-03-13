(ns laziness.case-study3)

(defn concat-n
  ([s n]
     (concat-n s n []))
  ([s n result]
     (if (> n 0)
       (recur s (dec n) (concat result s))
       result)))

;; (concat-n [:a] 5)
;; (concat-n [:a] 4000)

;;========================
;; What is going on here?
;;========================

;; (lazy-seq
;;   (lazy-seq
;;     (lazy-seq
;;       (lazy-seq
;;         (lazy-seq
;;           (lazy-seq
;;             (lazy-seq ...)))))))
