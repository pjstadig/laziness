(ns laziness.ambit)

(defn ambit
  "Returns a non-chunked lazy sequence of numbers from 0 to n-1.  Also prints
  out a '.' when an element is realized."
  [n]
  (letfn [(printing-inc [x]
            (println \.)
            (inc x))]
    (take n (iterate printing-inc 0))))
