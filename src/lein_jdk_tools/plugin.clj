(ns lein-jdk-tools.plugin
  "Provide a hook to add the JDK tools.jar and sa-jdi.jar files to the
  classpath."
  (:use
   [clojure.java.io :only [file]]
   [robert.hooke :only [add-hook]]))

;;; Locate JDK extra jars
(defn jpda-jars
  []
  (let [libdir (file (System/getProperty "java.home") ".." "lib")]
    (for [j ["tools.jar" "sa-jdi.jar"]
          :when (.exists (file libdir j))]
      (.getCanonicalPath (file libdir j)))))

;; (defn add-jpda-jars
;;   "JPDA is in the JDK's tools.jar and sa-jdi.jar. Add them to the classpath."
;;   [f project]
;;   (concat (f project) (jpda-jars)))


;; (defn hooks
;;   []
;;   (println "Adding tools.jar")
;;   (add-hook
;;    #'leiningen.core.classpath/get-classpath add-jpda-jars))

(defn middleware
  [project]
  (update-in project [:resource-paths] concat (jpda-jars)))
