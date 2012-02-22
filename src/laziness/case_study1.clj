(ns laziness.case-study1)

(defn why-is-3495-magic?
  ([s n]
     (why-is-3495-magic? s n []))
  ([s n r]
     (if (> n 0)
       (recur s (dec n) (concat r s))
       r)))

;; (why-is-3495-magic? [:a] 5)
;; (why-is-3495-magic? [:a] 3495)
