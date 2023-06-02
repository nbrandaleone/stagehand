# Create a new project
## https://rattlin.blog/finding-clj-new-beginnings.html
## https://blog.michielborkent.nl/new-clojure-project-quickstart.html
## https://github.com/babashka/neil
neil new --name <project>      ; minimal. Same as scratch
neil new app --name <project>  ; full blown
neil new scratch stagehand --scratch stagehand/app  ; src/stagehand/app.clj

# Search for libraries
neil dep search "file system"

# Add libraries to deps
neil dep add :lib babashka/fs :version 0.1.6

# Now that library has been added to project, it can be used
## clj is for REPL, and clojure for everything else 
` ``clj```
(require '[babashka.fs :as fs])

# Add a test runner
neil add test

# Run tests
clojure -M:test  ; or
neil test

## No tests, so lets add some
## test/myproject/core_test.clj
(ns myproject.core-test
  (:require [clojure.test :as t :refer [deftest is testing]]))

(deftest failing-test
  (testing "TODO: fix test"
    (is (= 3 4))))

## Add network REPL.  Not needed if using 'jack-in'
neil add repl

# Create Uberjar
## Add build tools support
neil add build :deps-deploy true

## The build.clj assumes it is under git control
git init
git add deps.eds src test
git commit -m "initial commit"

(ns scratch
    (:gen-class))

; Add :main in the call to b/uber:
(b/uber {:class-dir class-dir
         :uber-file uber-file
         :basis basis
         :main 'scratch})

clojure -T:build uber

# Run uberjar
java -jar target/lib1-1.2.1-standalone.jar 1 2 3

# Create Babashka tasks. Similiar to a makefile
## See blog for details

; bb.edn
{:tasks
 {:requires ([babashka.fs :as fs])

  test {:doc "Run tests"
        :task (apply clojure "-M:test" *command-line-args*)}

  nrepl {:doc "Start REPL"
         :task (if (fs/windows?)
                 (clojure "-M:nrepl")
                 (shell "rlwrap bb clojure -M:nrepl"))}

  uber {:doc "Build uberjar"
        :task (clojure "-T:build uber")}}}

```
bb test -v myproject.core-test/failing-test
```

$ bb tasks
The following tasks are available:

test  Run tests
nrepl Start REPL
uber  Build uberjar
