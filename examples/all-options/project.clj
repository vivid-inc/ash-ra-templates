(defproject art-example--all-options "0"

  ; Add the lein-art Leiningen plugin:
  :plugins [[vivid/lein-art "0.5.0"]]

  ; Render .art templates
  :art {:bindings     {updated "2021-01-01"}
        :dependencies {hiccup {:mvn/version "1.0.5"}}
        :delimiters   {:begin-forms "{%" :end-forms "%}" :begin-eval "{%=" :end-eval "%}"}
        :output-dir   "target"
        :templates    "templates"
        :to-phase     :evaluate})
