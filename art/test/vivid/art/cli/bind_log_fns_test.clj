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

(ns vivid.art.cli.bind-log-fns-test
  (:require
    [clojure.test :refer :all]
    [vivid.art.cli.log]))

(deftest bound
  (binding [vivid.art.cli.log/*info-fn* identity
            vivid.art.cli.log/*warn-fn* identity]
    (are [log-fn message]
    (= message
       (log-fn message))
    vivid.art.cli.log/*info-fn* "Amiga"
    vivid.art.cli.log/*warn-fn* "Amigo")))

(deftest unbound
  (are [log-fn]
    (thrown? IllegalStateException
             (apply log-fn ["message"]))
    vivid.art.cli.log/*info-fn*
    vivid.art.cli.log/*warn-fn*))
