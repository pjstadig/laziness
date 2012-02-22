(ns laziness.core)

;;=========
;; ACT ONE
;;=========


;;~~~~~~~~~~~~~
;; EVALUATION?
;;~~~~~~~~~~~~~

;; An evaluator is a function that takes a program and its arguments, and
;; produces the result.  Clojure has an evaluator built into the language.

;; Clojure goes through several steps to compile and execute your code.  First
;; it reads in the code, then it compiles it, and then it executes it.

(read-string "(+ 1 2)")

(+ 1 2)

(quote (+ 1 2))

(eval (quote (+ 1 2)))

(eval '(+ 1 2))

;; Most languages that people have worked with have "normal order," or "strict," evaluation.

(foo 'a (+ 1 2))

;; First x is evaluated, then (+ 1 2), then foo (which should evaluate to a
;; function), then the function is called with the results of evaluating x and
;; (+ 1 2).

;; Some languages (like Haskell) use "lazy" evaluation.  In this case, foo is
;; evaluated, then the function is called with x and (+ 1 2) unevaluated.  They
;; will not be evaluated until they are needed, and if they are never needed,
;; then they will never be evaluated.

;;~~~~~~~
;; TIMES
;;~~~~~~~

;; Like most languages, Clojure has several 'times.'  Clojure has read-time,
;; compile-time, and run-time.  Some LISPs have other 'times.'  For instance,
;; Common Lisp makes a distinction between compiling and loading code, and has
;; read-time, compile-time, load-time, and run-time.

;; In Clojure, code that you write can execute in any of the 'times.'  Macros
;; are run during compilation.  They are basically a way for you to extend the
;; compiler.  This is why so many people consider LISPs to be so powerful.

;; run-time
(defn what-is-this [x]
  (println x)
  x)

(what-is-this 3)

;; compile-time
(defmacro i-dont-even [x]
  (println x)
  x)

(i-dont-even (+ 1 2))

;; read-time
(i-dont-even #=(+ 1 2))

;;~~~~~~~~~~~~~~~~~~~~~~~~~~~~
;; LAZY VS. STRICT EVALUATION
;;~~~~~~~~~~~~~~~~~~~~~~~~~~~~



;;~~~~~~~~~~~~~~~~~~~~~~
;; LAZY-SEQ AND ITS ILK
;;~~~~~~~~~~~~~~~~~~~~~~
(find-doc #"lazy")

;; -------------------------
;; clojure.core/lazy-seq
;; ([& body])
;; Macro
;;   Takes a body of expressions that returns an ISeq or nil, and yields
;;   a Seqable object that will invoke the body only the first time seq
;;   is called, and will cache the result and return it on all subsequent
;;   seq calls. See also - realized?

lazy-seq

;; -------------------------
;; clojure.core/doall
;; ([coll] [n coll])
;;   When lazy sequences are produced via functions that have side
;;   effects, any effects other than those needed to produce the first
;;   element in the seq do not occur until the seq is consumed. doall can
;;   be used to force any effects. Walks through the successive nexts of
;;   the seq, retains the head and returns it, thus causing the entire
;;   seq to reside in memory at one time.

(defn side-effecting-fn [n]
  (dotimes [i (inc n)]
    (println i)))

(defn side-effecting-seq [n]
  (println n)
  (cons n (when (> n 0)
            (lazy-seq (side-effecting-seq (dec n))))))

(def this-is-not-lazy (side-effecting-fn 3))
(def this-is-lazy (side-effecting-seq 3))

(do (println "side-effecting-seq")
    (doall (side-effecting-seq 3)))
;;=> (3 2 1 0)

;; -------------------------
;; clojure.core/concat
;; ([] [x] [x y] [x y & zs])
;;   Returns a lazy seq representing the concatenation of the elements in the supplied colls.

(concat [1 2] [3 4])
;;=> (1 2 3 4)

;; -------------------------
;; clojure.core/cycle
;; ([coll])
;;   Returns a lazy (infinite!) sequence of repetitions of the items in coll.

(take 8 (cycle [1 2 3]))
;;=> (1 2 3 1 2 3 1 2)

;; -------------------------
;; clojure.core/distinct
;; ([coll])
;;   Returns a lazy sequence of the elements of coll with duplicates removed

(distinct [1 1 2 3 2 4])
;;=> (1 2 3 4)

;; -------------------------
;; clojure.core/dorun
;; ([coll] [n coll])
;;   When lazy sequences are produced via functions that have side
;;   effects, any effects other than those needed to produce the first
;;   element in the seq do not occur until the seq is consumed. dorun can
;;   be used to force any effects. Walks through the successive nexts of
;;   the seq, does not retain the head and returns nil.

(dorun (side-effecting-seq 3))
;;=> nil

;; -------------------------
;; clojure.core/drop
;; ([n coll])
;;   Returns a lazy sequence of all but the first n items in coll.

(drop 3 [1 2 3 4])
;;=> (4)

;; -------------------------
;; clojure.core/drop-last
;; ([s] [n s])
;;   Return a lazy sequence of all but the last n (default 1) items in coll

(drop-last 3 [1 2 3 4])
;;=> (1)

;; -------------------------
;; clojure.core/drop-while
;; ([pred coll])
;;   Returns a lazy sequence of the items in coll starting from the first
;;   item for which (pred item) returns nil.

(drop-while odd? [1 3 5 6 7])
;;=> (6 7)

;; -------------------------
;; clojure.core/filter
;; ([pred coll])
;;   Returns a lazy sequence of the items in coll for which
;;   (pred item) returns true. pred must be free of side-effects.

(filter odd? [1 3 5 6 7])
;;=> (1 3 5 7)

;; -------------------------
;; clojure.core/for
;; ([seq-exprs body-expr])
;; Macro
;;   List comprehension. Takes a vector of one or more
;;    binding-form/collection-expr pairs, each followed by zero or more
;;    modifiers, and yields a lazy sequence of evaluations of expr.
;;    Collections are iterated in a nested fashion, rightmost fastest,
;;    and nested coll-exprs can refer to bindings created in prior
;;    binding-forms.  Supported modifiers are: :let [binding-form expr ...],
;;    :while test, :when test.

;;   (take 100 (for [x (range 100000000) y (range 1000000) :while (< y x)] [x y]))

(for [x [1 3 5 6 7] :while (odd? x)]
  x)
;;=> (1 3 5)

;; -------------------------
;; clojure.core/interleave
;; ([c1 c2] [c1 c2 & colls])
;;   Returns a lazy seq of the first item in each coll, then the second etc.

(interleave [1 3 5] [2 4 6])
;;=> (1 2 3 4 5 6)

;; -------------------------
;; clojure.core/interpose
;; ([sep coll])
;;   Returns a lazy seq of the elements of coll separated by sep

(interpose '| [1 2 3])

;; -------------------------
;; clojure.core/iterate
;; ([f x])
;;   Returns a lazy sequence of x, (f x), (f (f x)) etc. f must be free of side-effects

(take 5 (iterate inc 1))

;; -------------------------
;; clojure.core/keep
;; ([f coll])
;;   Returns a lazy sequence of the non-nil results of (f item). Note,
;;   this means false return values will be included.  f must be free of
;;   side-effects.

(keep identity [1 2 nil 4 5])

;; -------------------------
;; clojure.core/keep-indexed
;; ([f coll])
;;   Returns a lazy sequence of the non-nil results of (f index item). Note,
;;   this means false return values will be included.  f must be free of
;;   side-effects.

(keep-indexed #(vector %1 %2) [1 2 nil 4 5])

;; -------------------------
;; clojure.core/lazy-cat
;; ([& colls])
;; Macro
;;   Expands to code which yields a lazy sequence of the concatenation
;;   of the supplied colls.  Each coll expr is not evaluated until it is
;;   needed.

;;   (lazy-cat xs ys zs) === (concat (lazy-seq xs) (lazy-seq ys) (lazy-seq zs))

(lazy-cat [1 2] [3 4])
;;=> (1 2 3 4)

;; -------------------------
;; clojure.core/map-indexed
;; ([f coll])
;;   Returns a lazy sequence consisting of the result of applying f to 0
;;   and the first item of coll, followed by applying f to 1 and the second
;;   item in coll, etc, until coll is exhausted. Thus function f should
;;   accept 2 arguments, index and item.

(map-indexed vector [:a :b :c])
;;=> ([0 :a] [1 :b] [2 :c])

;; -------------------------
;; clojure.core/partition
;; ([n coll] [n step coll] [n step pad coll])
;;   Returns a lazy sequence of lists of n items each, at offsets step
;;   apart. If step is not supplied, defaults to n, i.e. the partitions
;;   do not overlap. If a pad collection is supplied, use its elements as
;;   necessary to complete last partition upto n items. In case there are
;;   not enough padding elements, return a partition with less than n items.

(partition 2 1 [1 2 3])
;;=> ((1 2) (2 3))

;; -------------------------
;; clojure.core/partition-all
;; ([n coll] [n step coll])
;;   Returns a lazy sequence of lists like partition, but may include
;;   partitions with fewer than n items at the end.

(partition-all 2 1 [1 2 3])
;;=> ((1 2) (2 3) (3))

;; -------------------------
;; clojure.core/partition-by
;; ([f coll])
;;   Applies f to each value in coll, splitting it each time f returns
;;    a new value.  Returns a lazy seq of partitions.

(partition-by odd? [2 2 1 4 4 3 5])
;;=> ((2 2) (1) (4 4) (3 5))

;; -------------------------
;; clojure.core/pcalls
;; ([& fns])
;;   Executes the no-arg fns in parallel, returning a lazy sequence of
;;   their values

(pcalls #(+ 1 2) #(- 2 1))
;;=> (3 1)

;; -------------------------
;; clojure.core/pmap
;; ([f coll] [f coll & colls])
;;   Like map, except f is applied in parallel. Semi-lazy in that the
;;   parallel computation stays ahead of the consumption, but doesn't
;;   realize the entire result unless required. Only useful for
;;   computationally intensive functions where the time of f dominates
;;   the coordination overhead.

(pmap inc [1 2 3])
;;=> (2 3 4)

;; -------------------------
;; clojure.core/pvalues
;; ([& exprs])
;; Macro
;;   Returns a lazy sequence of the values of the exprs, which are
;;   evaluated in parallel

(pvalues (+ 1 2) (- 2 1))
;;=> (3 1)

;; -------------------------
;; clojure.core/range
;; ([] [end] [start end] [start end step])
;;   Returns a lazy seq of nums from start (inclusive) to end
;;   (exclusive), by step, where start defaults to 0, step to 1, and end
;;   to infinity.

(take 5 (range))
;;=> (0 1 2 3 4)

;; -------------------------
;; clojure.core/re-seq
;; ([re s])
;;   Returns a lazy sequence of successive matches of pattern in string,
;;   using java.util.regex.Matcher.find(), each such match processed with
;;   re-groups.

(re-seq #"[aeiou]" "J.Q. Vandz struck my big fox whelp.")
;;=> ("a" "u" "i" "o" "e")

;; -------------------------
;; clojure.core/realized?
;; ([x])
;;   Returns true if a value has been produced for a promise, delay, future or lazy sequence.

(realized? (range 5))
;;=> false

(let [r (range 5)]
  (doall r)
  (realized? r))
;;=> true

;; -------------------------
;; clojure.core/reductions
;; ([f coll] [f init coll])
;;   Returns a lazy seq of the intermediate values of the reduction (as
;;   per reduce) of coll by f, starting with init.

(reductions + 0 [1 2 3 4])
;;=> (0 1 3 6 10)

;; -------------------------
;; clojure.core/remove
;; ([pred coll])
;;   Returns a lazy sequence of the items in coll for which
;;   (pred item) returns false. pred must be free of side-effects.

(remove odd? [1 2 3 4 5])
;;=> (2 4)

;; -------------------------
;; clojure.core/repeat
;; ([x] [n x])
;;   Returns a lazy (infinite!, or length n if supplied) sequence of xs.

(repeat 5 7)
;;=> (7 7 7 7 7)
(take 5 (repeat 'a))
;;=> (7 7 7 7 7)

;; -------------------------
;; clojure.core/repeatedly
;; ([f] [n f])
;;   Takes a function of no args, presumably with side effects, and
;;   returns an infinite (or length n if supplied) lazy sequence of calls
;;   to it

(take 3 (repeatedly #(rand-int 5)))
;;=> (3 1 3)

;; -------------------------
;; clojure.core/seque
;; ([s] [n-or-q s])
;;   Creates a queued seq on another (presumably lazy) seq s. The queued
;;   seq will produce a concrete seq in the background, and can get up to
;;   n items ahead of the consumer. n-or-q can be an integer n buffer
;;   size, or an instance of java.util.concurrent BlockingQueue. Note
;;   that reading from a seque can block if the reader gets ahead of the
;;   producer.

(seque 2 (slow-seq 5))

;; -------------------------
;; clojure.core/take
;; ([n coll])
;;   Returns a lazy sequence of the first n items in coll, or all items if
;;   there are fewer than n.

(take 2 [1 2 3 4 5])
;;=> (1 2)

;; -------------------------
;; clojure.core/take-nth
;; ([n coll])
;;   Returns a lazy seq of every nth item in coll.

(take-nth 2 [1 2 3 4 5])
;;=> (1 3 5)

;; -------------------------
;; clojure.core/take-while
;; ([pred coll])
;;   Returns a lazy sequence of successive items from coll while
;;   (pred item) returns true. pred must be free of side-effects.

(take-while odd? [1 3 4 5 7])
;;=> (1 3)

;; -------------------------
;; clojure.core/tree-seq
;; ([branch? children root])
;;   Returns a lazy sequence of the nodes in a tree, via a depth-first walk.
;;    branch? must be a fn of one arg that returns true if passed a node
;;    that can have children (but may not).  children must be a fn of one
;;    arg that returns a sequence of the children. Will only be called on
;;    nodes for which branch? returns true. Root is the root node of the
;;   tree.

;;      1
;;     / \
;;    /   \
;;   2     5
;;  / \   /
;; 3   4 6

(map first (tree-seq next rest '(1 (2 (3) (4)) (5 (6)))))

;;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
;; STARTING INTO SOME TROUBLE >>HERE<<
;;~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

;; -------------------------
;; clojure.core/line-seq
;; ([rdr])
;;   Returns the lines of text from rdr as a lazy sequence of strings.
;;   rdr must implement java.io.BufferedReader.

(with-open [rdr (clojure.java.io/reader "/etc/hosts")]
  (line-seq rdr))

;; -------------------------
;; clojure.core/map
;; ([f coll] [f c1 c2] [f c1 c2 c3] [f c1 c2 c3 & colls])
;;   Returns a lazy sequence consisting of the result of applying f to the
;;   set of first items of each coll, followed by applying f to the set
;;   of second items in each coll, until any one of the colls is
;;   exhausted.  Any remaining items in other colls are ignored. Function
;;   f should accept number-of-colls arguments.

(map inc [1 2 3])
;;=> (2 3 4)
