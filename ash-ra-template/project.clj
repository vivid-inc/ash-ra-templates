; Copyright 2019 Vivid Inc.

; Referencing https://github.com/technomancy/leiningen/blob/master/sample.project.clj

(defproject vivid/ash-ra-template "0.3.0"

  :description "Ash Ra Template: Simplistic template library featuring Clojure language processing with Ruby 2.0 ERB-esque syntax."
  :url "https://github.com/vivid/ash-ra-template"
  :license {:distribution :repo
            :name         "Eclipse Public License"
            :url          "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/tools.deps.alpha "0.6.496"]
                 [org.projectodd.shimdandy/shimdandy-api "1.2.1"]
                 [org.projectodd.shimdandy/shimdandy-impl "1.2.1"]
                 [org.xeustechnologies/jcl-core "2.8"]
                 [reduce-fsm "0.1.4"]]

  :aliases {"build" ["do"
                     "clean,"
                     "cloverage,"
                     "eastwood,"
                     "kibit,"
                     "jar,"
                     "install"]}

  :cloverage {:codecov? true
              :html?    false
              :output   "."                                 ; "lein jar" appears to destroy target/coverage/codecov.json
              }

  :global-vars {*warn-on-reflection* true}

  :javac-options ["-target" "1.8"]

  :min-lein-version "2.9.1"

  :plugins [[jonase/eastwood "0.3.5"]
            [lein-ancient "0.6.15"]
            [lein-cloverage "1.1.1"]
            [lein-kibit "0.1.6"]
            [lein-nvd "1.0.0"]]

  :profiles {:dev {:dependencies   [[pjstadig/humane-test-output "0.9.0"]]
                   :plugins        [[com.jakemccrary/lein-test-refresh "0.24.0"]]
                   :injections     [(require 'pjstadig.humane-test-output)
                                    (pjstadig.humane-test-output/activate!)]
                   :resource-paths ["test-resources"]
                   :test-refresh   {:quiet true}}}

  :repositories [["clojars" {:sign-releases false}]])
