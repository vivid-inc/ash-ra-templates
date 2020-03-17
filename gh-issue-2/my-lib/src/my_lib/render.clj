(ns my-lib.render
  (:require [hiccup.core :refer [html]]))

(defn em [text] (html [:em text]))
(defn my-header [text] (html [:h1 text]))
(defn span [text] (html [:span text]))
(defn span2 [text] (str "<span>" text "</span>"))

(def sample-template
  "test 1: <%= (my-header \"sample text!\") %>
   test 2: <%= (span \"sample span!\") %>
   test 3: <%= (span2 \"sample span 2\") %>")
