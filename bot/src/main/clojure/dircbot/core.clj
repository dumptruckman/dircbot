(ns dircbot.core
  (:gen-class)
  (:import (com.dumptruckman.dircbot DircBot)))

(defn -main
  []
  (let [bot (proxy [DircBot] ["dtmbot"]
              (onMessage [channel sender login hostname message]
                (.sendMessage this channel "testing")))]
    (.setVerbose bot true)
    (.connect bot "irc.synirc.net")
    (.joinChannel bot "#bottesting"))
  #_(doto (new DircBot "dtmbot")
        (.setVerbose true)
        (.connect "irc.synirc.net")
        (.joinChannel "#bottesting")))
