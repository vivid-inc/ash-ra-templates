; Copyright 2019 Vivid Inc.

(ns vivid.art.specs
  "Clojure Spec definitions for Ash Ra Temple data structures.
  All definitions exposed through ART's API plus all key
  definitions are namespaced."
  (:require
    [clojure.spec.alpha :as s]
    [clojure.tools.deps.alpha.specs]))


; ART templates

(s/def :vivid.art/template (s/nilable string?))


; Clojure dependency maps, required to render a given template

(s/def :vivid.art/dependencies :clojure.tools.deps.alpha.specs/deps-map)


; Failure descriptors

(s/def ::failure-type keyword?)
(s/def ::cause coll?)

(s/def :vivid.art/failure
  (s/keys :req-un [::failure-type ::cause ::template]))


; Template delimiter definitions

; TODO Unify naming
(s/def ::begin-forms (s/and string? #(not (empty? %))))
(s/def ::end-forms (s/and string? #(not (empty? %))))
(s/def ::begin-eval (s/and string? #(not (empty? %))))
(s/def ::end-eval (s/and string? #(not (empty? %))))

(s/def :vivid.art/delimiters
  (s/keys :req-un [::begin-forms ::end-forms]
          :opt-un [::begin-eval ::end-eval]))
