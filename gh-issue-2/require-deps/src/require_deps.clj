(ns require-deps
  (:require
    [clojure.java.classpath :as cp]
    [my-lib.render]
    [vivid.art :as art]))

(defn art-classpath []
  (art/render "art-classpath: <% (require '[clojure.java.classpath :as cp]) %><%= (cp/classpath) %>"
              {:dependencies {'org.clojure/java.classpath {:mvn/version "1.0.0"}}}))

(defn art-eval-template []
  (art/render "art-eval-template: Today is brought to you by the number <%= (mod 29 13) %>"))

(defn art-plain-template []
  (art/render "art-plain-template: plain template"))

(defn art-hiccup []
  (art/render "art-hiccup: <% (require '[hiccup.core :refer [html]]) %><%= (html [:span \"text\"])"
                  {:dependencies {'hiccup {:mvn/version "2.0.0-alpha2"}}}))

(defn art-my-lib-local-root []
  (art/render "art-my-lib-local-root: <% (require '[my-lib.render :refer [em]]) %>Let's <%= (em \"emphasize\") %> this" {:dependencies {'my-lib {:local/root "../my-lib"}}}))

(defn art-my-lib-mvn-dep []
  (art/render "art-my-lib-mvn-dep: <% (require '[my-lib.render :refer [em]]) %>Let's <%= (em \"emphasize\") %> this" {:dependencies {'my-lib {:mvn/version "1.0.0"}}}))

(defn my-lib-my-header []
  (str
    "my-lib-my-header: "
    (my-lib.render/my-header "My Header")))

(defn -main
  [& args]
  (let [fns [; (fn [] (cp/classpath))
             ; art-classpath
             ; art-eval-template
             art-hiccup
             art-my-lib-mvn-dep
             ; art-my-lib-local-root
             ; art-plain-template
             my-lib-my-header]]
    (doseq [f fns]
      (println (f)))))
