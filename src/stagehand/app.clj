(ns stagehand.app
  "FIXME: my new org.corfield.new/scratch project.")

(defn exec
  "Invoke me with clojure -X stagehand.app/exec"
  [opts]
  (println "exec with" opts))

(defn -main
  "Invoke me with clojure -M -m stagehand.app"
  [& args]
  (println "-main with" args))
