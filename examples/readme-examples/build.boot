(set-env! :dependencies '[[boot/core "2.8.3" :scope "provided"]
                          [vivid/boot-art "0.5.0"]])

(require '[vivid.boot-art :refer [art]]
         '[clojure.java.io :as io])

(deftask rndr []
  (comp 
    (art :bindings   '{mysterious-primes [7 191]}
         :delimiters {:begin-forms "{%" :end-forms "%}" :begin-eval "{%=" :end-eval "%}"}
         :templates      [(io/file "templates/oracle.art")]
         :output-dir (io/file "target"))))
