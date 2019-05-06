; Copyright 2019 Vivid Inc.

(ns vivid.art.delimiters-test
  (:require
    [clojure.test :refer :all]
    [vivid.art :as art]
    [vivid.art.delimiters]))

(deftest delimiter-syntax
  (testing "Unbalanced delimiters"
    (are [expected template]
      (= expected (art/render template))
      ; TODO "unbalanced <% honyahonya"
      "Unbalanced  doesn't switch stream processing mode" "Unbalanced %> doesn't switch stream processing mode")))

(deftest api-contract
  (testing "Default ERB-style delimiters"
    (are [expected template]
      (= expected (art/render template))
      "plain text" "plain text"
      "juniper" "juni<%%>per"
      "START 1234 END" "START <%(def cnt 4)(doseq [i (range 1 (inc cnt))]%><%=i%><%)%> END"))
  (testing "Manually specify ERB-style delimiters"
    (are [expected template]
      (= expected (art/render template
                              :delimiters {"<%"  :begin-forms
                                           "<%=" :begin-eval
                                           "%>"  :end-forms}))
      "plain text" "plain text"
      "juniper" "juni<%%>per"
      "START 1234 END" "START <%(def cnt 4)(doseq [i (range 1 (inc cnt))]%><%=i%><%)%> END")))

(deftest bundled-delimiter-definitions
  (testing "ART-provided delimiter library: ERB"
    (are [expected template]
      (= expected (art/render template
                              :delimiters vivid.art.delimiters/erb))
      "plain text" "plain text"
      "juniper" "juni<%%>per"
      "START 1234 END" "START <%(def cnt 4)(doseq [i (range 1 (inc cnt))]%><%=i%><%)%> END"))
  (testing "ART-provided delimiter library: Jinja"
    (are [expected template]
      (= expected (art/render template
                              :delimiters vivid.art.delimiters/jinja))
      "plain text" "plain text"
      "juniper" "juni{%%}per"
      "START 1234 END" "START {%(def cnt 4)(doseq [i (range 1 (inc cnt))]%}{{i}}{%)%} END"))
  (testing "ART-provided delimiter library: PHP"
    (are [expected template]
      (= expected (art/render template
                              :delimiters vivid.art.delimiters/php))
      "plain text" "plain text"
      "juniper" "juni<??>per"
      "START 1234 END" "START <?(def cnt 4)(doseq [i (range 1 (inc cnt))]?><?=i?><?)?> END")))
