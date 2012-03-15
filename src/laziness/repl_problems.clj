(ns laziness.repl-problems)

;;============================================================
;; Set *print-length* to avoid realizing an infinite sequence
;;============================================================

;; (range 10000)
;; (set! *print-length* 25)


;;======================================
;; Set *print-level* to control nesting
;;======================================

;; '(1 (2 (3) 4) 5)
;; (set! *print-level* 2)
