(defproject laziness "1.0.0"
  :description "My Clojure/West presentation on the Clojure laziness constructs
  and common pitfalls."
  :dependencies [[org.clojure/clojure "1.4.0-beta3"]]
  :jvm-opts ["-Xmx100m"]
  :aot [laziness.case-study2])
