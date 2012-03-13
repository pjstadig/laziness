(ns laziness.evaluation)

;;~~~~~~~~~~~~
;; EVALUATION
;;~~~~~~~~~~~~

;;===============================
;; Clojure's order of evaluation
;;===============================

;; Evaluates function first, then each argument, then calls the function
;; with the arguments.

(defn minus [a b]
  (println :a a :b b)
  (- a b))

;; (minus 42 (+ 5 7))

;;========
;; Macros
;;========

;; Macros do not evaluate their arguments (at compile-time)

(defmacro -m [a b]
  (println :a a :b b)
  `(- ~a ~b))

;; (-m 42 (+ 5 7))
