(defproject dircbot "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [pircbot/pircbot "1.5.0"]]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [dircbot.core]
  :main dircbot.core
  :source-paths ["src" "src/main/clojure"]
  :java-source-paths ["src/main/java"]  ; Java source is stored separately.
  :test-paths ["test" "src/test/clojure"]
  :resource-paths ["src/main/resource"] )
