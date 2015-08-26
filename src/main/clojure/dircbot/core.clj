(ns dircbot.core
  (:gen-class)
  (:import (com.dumptruckman.dircbot DIRCBot)))

(defn -main
  []
  (let [bot (proxy [DIRCBot] ["dtmbot"]
              (onMessage [channel sender login hostname message]
                (.sendMessage this channel "testing")))]
    (.setVerbose bot true)
    (.connect bot "irc.synirc.net")
    (.joinChannel bot "#bottesting"))
  #_(doto (new DIRCBot "dtmbot")
        (.setVerbose true)
        (.connect "irc.synirc.net")
        (.joinChannel "#bottesting")))
