; Copyright 2020 Vivid Inc.
;
; Licensed under the Apache License, Version 2.0 (the "License");
; you may not use this file except in compliance with the License.
; You may obtain a copy of the License at
;
;    https://www.apache.org/licenses/LICENSE-2.0
;
; Unless required by applicable law or agreed to in writing, software
; distributed under the License is distributed on an "AS IS" BASIS,
; WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
; See the License for the specific language governing permissions and
; limitations under the License.

(ns vivid.art.render.examples-test
    "Confirms that ART works as promised in the README file."
    (:require
      [clojure.java.io :as io]
      [clojure.test :refer :all]
      [vivid.art :as art]))

(deftest usage
         (testing "All code samples in the README file"
                  (are [expected template]
                       (= expected (art/render template))
                       "There were 3 swallows, dancing in the sky."
                       "There were <%= (+ 1 2) %> swallows, dancing in the sky."

                       "There were 3 swallows, dancing in the sky."
                       (slurp (io/resource "prelude.html.art"))

                       "We are but stowaways aboard a drifting ship, forsaken to the caprices of the wind and currents."
                       "We are but stowaways aboard a drifting ship, forsaken to the caprices of the wind and currents."

                       ""
                       "<% (def button-classes [:primary :secondary :disabled]) %>"

                       "\n\n"
                       "
<%
(defn updated-statement
  [date version]
  (format \"This document was updated on %s for version %s\"
          date version))
%>
")

                  (is (= "
<p>
Chondrichthyes research published in 1987, 1989, 1992.
</p>
"
                         (art/render "<%
(require '[clojure.string])
(def publication-dates [1987 1989 1992])
(defn cite-dates [xs] (clojure.string/join \", \" xs))
%>
<p>
Chondrichthyes research published in <%= (cite-dates publication-dates) %>.
</p>
")))



                  (are [expected template]
                       (= expected (art/render template))
                       "Splash!"
                       "<% (emit \"Splash!\") %>"

                       "Splash!"
                       "<%= \"Splash!\" %>"

                       "Splash!"
                       "<%= (str \"Splash!\") %>")


                  (is (=
                        "April 5 was a most pleasant, memorable day."
                        (do
                          (def my-bindings {'month "April"
                                            'day   5})
                          (art/render "<%= month %> <%= day %> was a most pleasant, memorable day."
                                      {:bindings my-bindings}))))

                  (is (= "
The natural number e is approximately 2.7182"
                         (art/render "<% (def e 2.7182) %>
The natural number e is approximately <%= e %>")))

                  (is (= "
The natural number e is approximately 2.7182"
                         (art/render "{| (def e 2.7182) |}
The natural number e is approximately {|= e |}"
                                     {:delimiters {:begin-forms "{|"
                                                   :end-forms   "|}"
                                                   :begin-eval  "{|="}})))

                  (is (= "\n\n<li><a href=\"#739\" id=\"link\">Moving wing assembly into place</a></li><li><a href=\"#740\" id=\"link\">Connecting fuel lines and hydraulics</a></li><li><a href=\"#741\" id=\"link\">Attaching wing assembly to fuselage</a></li>\n"
                         (art/render "
<%
(require '[hiccup.core])

(def ^:const toc-headings [{:id 739 :text \"Moving wing assembly into place\"}
                           {:id 740 :text \"Connecting fuel lines and hydraulics\"}
                           {:id 741 :text \"Attaching wing assembly to fuselage\"}])

(defn toc-entry [heading]
  (hiccup.core/html [:li
    [:a#link
      {:href (str \"#\" (heading :id))}
      (heading :text)]]))
%>
<%= (apply str (map toc-entry toc-headings)) %>
"
                                     {:dependencies {'hiccup {:mvn/version "1.0.5"}}})))))
