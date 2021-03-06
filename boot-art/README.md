# boot-art Ash Ra Template Boot Task



[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat-square)](LICENSE.txt)
[![Current version](https://img.shields.io/clojars/v/vivid/boot-art.svg?color=blue&style=flat-square)](https://clojars.org/vivid/boot-art)

`boot-art` composes easily into your existing [Boot](https://github.com/boot-clj/boot) infrastructure for rendering [Ash Ra Template](https://github.com/vivid-inc/ash-ra-template) `.art` templates.



## Quick Start


```sh
$ cat oracle.art

<% (defn mult [multiplicands] (apply * multiplicands)) %>
Wait, I see it! Your destiny lies deep within the number <%= (mult mysterious-primes) %>.

$ boot -d vivid/boot-art art --bindings "'{mysterious-primes [7 191]}" \
                             --files oracle.art \
                             --output-dir .
```
`boot-art` will render the output file `oracle` into the current directory.

Re-writing the above into a `build.boot`:

```clojure
(set-env! :dependencies '[[vivid/boot-art "0.5.0"]]
          :resource-paths #{"templates"})    ; NOTE: Move oracle.art into this dir

(require '[vivid.boot-art :refer [art]])

(deftask rndr []
         (comp (art :bindings '{mysterious-primes [7 191]})
               (target)))
```

You can render ART templates using `boot` at the CLI anywhere you desire:
```sh
$ boot -d vivid/boot-art art --help
```



## Synopsis

`boot-art` can be used with Boot `build.boot` and at the CLI.

Templates occur as `.art` template files in Boot's fileset, or optionally by
`:file` as one or more paths `.art` template files and/or directory trees thereof.
The `art` Boot task scans those paths for all ART template files with the `.art`
filename extension.

Templates are rendered and written with a task like `(target)`, and optionally
copied to the output directory `:output-dir`, stripped of their `.art`
filename extensions, overwriting any existing files with the same paths.
`output-dir` and sub-paths therein are created as necessary.



#### Options

| `build.boot` | CLI argument | Parameters | Default | Explanation |
| --- | --- | --- | --- | --- |
| `:bindings` | `--bindings` | VAL | | Bindings made available to templates for symbol resolution |
| `:delimiters` | `--delimiters` | VAL | `erb` | Template delimiters |
| `:dependencies` | `--dependencies` | VAL | | Clojure deps map providing libs within the template evaluation environment. |
| | `-h`, `--help` | | | Displays lovely help and then exits |
| `:output-dir` | `--output-dir` | DIR | | Write a copy of the rendered file set to DIR |
| `:templates` | `--templates` | FILES | | Render these ART files and directory trees thereof, instead of Boot's fileset |
| `:to-phase` | `--to-phase` | One of: `parse`, `translate`, `enscript`, `evaluate` | `evaluate` | Stop the render dataflow on each template at an earlier phase |

Depending on what types of values a particular option accepts and whether `boot-art` was invoked as a Boot task in Clojure or from the CLI,
ART attempts to interpret arguments in this order of precedence:
1. As a map.
1. As the (un-)qualified name of a var.
1. As a path to an EDN file.
1. As an EDN literal.



## Cookbook





### Custom bindings, delimiters, dependencies, and project code
```clojure
(set-env! :dependencies '[[vivid/boot-art "0.5.0"]]
          :source-paths #{"src"}           ; Give templates use of project code
          :resource-paths #{"content"})    ; Render all .art templates in the content/ directory

(require '[clojure.java.io :as io]
         '[com.acme.data]
         '[vivid.boot-art :refer [art]])

(deftask rndr
  "Render all .art template files in the content/ directory to out/cdn/"
  []
  (comp (art :bindings     [{'manufacturer     "Acme Corporation"   ; Map literal
                             'manufacture-year "2022"}
                             #'com.acme.data/product-data           ; Var, value is a map
                             "{current-year 2021}"                  ; EDN as a string
                             "data/sales-offices.edn"]              ; EDN file; top-level form is a map
             :delimiters   'jinja                                   ; Unqualified, resolves to #'vivid.art.delimiters/jinja
             :dependencies '{hiccup {:mvn/version "1.0.5"}})

        (target :dir #{"out/cdn"})))
```

__Discussion:__
Template syntax is set by the `:delimiters` options.
Clojure forms within the templates can resolve vars and dependencies provided
by several factors: `:bindings` for resolving vars, `:dependencies` for
libraries, and code in the project.

__See also:__
[Example](../examples/custom-options).
[Rendering and options](../art/README.md#rendering-and-options) in the ART documentation.



### Specify ART template files and/or the output directory
```clojure
(import '(java.io File))

(deftask rndr []
         (art :templates  #{"source/index.html.art" "templates"}
              :output-dir (File. "out")))
```

__Discussion:__
If `:templates` is specified, `(art)` will use those files instead of searching Boot's fileset.
Providing an `:output-dir` will cause a copy of the rendered output fileset to be written there, in addition to Boot's `(target)`.

__See also:__
[Example](../examples/boot-templates-output-dir).



### Override bundled Clojure version
```clojure
(deftask rndr []
         (comp (art :dependencies '{org.clojure/clojure {:mvn/version "1.10.1"}})
               (target)))
```

__Discussion:__
As an implicit dependency, the template execution environment provides ART's minimum supported version of Clojure, version 1.9.0, but this can be overridden by supplying the `org.clojure/clojure` dependency with a different version.

__See also:__
[Example](../examples/override-clojure-version).
[`:dependencies`](../art/README.md#external-dependencies) in the ART documentation.



### Re-render templates whenever their source files change
```clojure
(set-env! :dependencies '[[vivid/boot-art "0.5.0"]]
          :resource-paths #{"resources"})    ; Render all .art templates in the content/ directory

(require '[vivid.boot-art :refer [art]])

(deftask dev
  "Development mode: Render all .art template files in the resources/ directory to target/
  whenever they change."
  []
  (comp (watch)
        (art)
        (target)))
```

__Discussion:__
Boot offers a build-in `(watch)` task.

__See also:__
[Example](../examples/watch).
[`(watch)`](https://github.com/boot-clj/boot/blob/master/doc/boot.task.built-in.md#watch) in Boot's documentation.



### Configure multi-batch rendering in build.boot
```clojure
(deftask rndr []
  (comp
    ; An ART render batch configuration
    (art :templates    [(io/file "src/templates/css")]
         :dependencies '{garden {:mvn/version "1.3.10"}}
         :output-dir   (io/file "src/resources"))

    ; Another, different batch
    (art :templates  [(io/file "src/templates/java")]
         :bindings   '{version "1.2.3"}
         :output-dir (io/file "target/generated-sources/java"))))
```
__Discussion:__
Each individual render batch is performed as a sequence of `(art)` tasks,
leveraging Boot's idiom of pipelined tasks operating on a fileset.

__See also:__
[Example](../examples/multi-batch).



## License

© Copyright Vivid Inc.
[Apache License 2.0](LICENSE.txt) licensed.
