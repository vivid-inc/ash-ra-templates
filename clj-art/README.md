# clj-art Ash Ra Template Clojure Tool 

[![License](https://img.shields.io/badge/license-Apache%202-blue.svg?style=flat-square)](LICENSE.txt)
[![Clojars Project](https://img.shields.io/clojars/v/vivid/clj-art.svg?color=blue&style=flat-square)](https://clojars.org/vivid/clj-art)

`clj-art` is a Clojure `deps.edn` tool for rendering [Ash Ra Template](https://github.com/vivid-inc/ash-ra-template) `.art` templates.

Render Ash Ra .art templates.

Provided file or directory tree paths containing Ash Ra .art template files and an output dir, this
Clojure tool renders the ART templates to the output dir, preserving the relative sub-paths.


## Quick Start

Create an alias in your project `deps.edn` or personal `~/.clojure/deps.edn` map:
```edn
{:aliases 
  {:art {:extra-deps {org.clojure/clojure {:mvn/version "1.9.0"}
                      vivid/clj-art       {:mvn/version "0.5.0"}}
         :main-opts  ["-m" "vivid.art.clj-tool"
                      ... ART options ... ART templates]}
```

Templates are supplied as one or more paths to `.art` template files and/or directory trees containing `.art` templates. 
Depending on the situation, you might need to state Clojure as an explicit dependency, as ART doesn't include one itself.

```sh
$ clj -A:art --help
```


### Options

| CLI argument | Parameters | Cardinality | Default | Explanation |
| --- | --- | --- | --- | --- |
| `-b`, `--bindings` | Map, var, EDN file path, EDN literal | Single or collection | | Bindings made available to templates for symbol resolution |
| `-d`, `--delimiters` | Map, var, EDN literal | Single or collection | `erb` | Template delimiters |
| `--dependencies` | Map, var, EDN file path, EDN literal | Single | | Clojure deps map providing libs within the template evaluation environment. Deps maps are merged into this one. Supply your own Clojure dep to override the current version. |
| `-h`, `--help` | | | | Displays lovely help and then exits |
| `-o`, `--output-dir` | File path | Single | `.` | Write rendered files to DIR |
| `p`, `--to-phase` | One of: `parse`, `translate`, `enscript`, `evaluate` | Single | | Stop the render dataflow on each template at an earlier phase |


### Points of caution

**Slow rendering**: ART's render time is deathly slow, assume a per-file delay of around 1 second. This is accounted for by ShimDandy doing its magic to set up a newly-initialized Clojure environment for each of your templates.

**deps.edn**: When supplying double-quoted parameters to options in your `deps.edn` file, spaces must be replaced with comma ',' characters.
Example:
```edn
  "--dependencies" "{vivid/art {:mvn/version \"0.5.0\"}}"    ; BAD, will fail.

  "--dependencies" "{vivid/art,{:mvn/version,\"0.5.0\"}}"    ; OK.
```
This is idiosyncratic to `deps.edn`.
`clj-art` invoked at the command line obediently accepts the plain form:
```
$ clojure -m vivid.art.clj-tool \
    --dependencies "{vivid/art {:mvn/version \"0.5.0\"}}"    ; OK.
    ...
```



TODO https://github.com/clojure/tools.deps.alpha/wiki/Tools
TODO default deps
TODO Single or collection deps





## License

© Copyright Vivid Inc.
[Apache License 2.0](LICENSE.txt) licensed.