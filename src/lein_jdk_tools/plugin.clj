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
  (-> project
      (update-in [:resource-paths] concat (jpda-jars))
      ;; Adding the jars to :resource-paths will result in their being included
      ;; in any jars you create. Since there's never really a reason to
      ;; include one jar in another, we'll just exclude these two jars by
      ;; their regular name (which is the only way :jar-exclusions will properly
      ;; catch and exclude them).
      (update-in [:jar-exclusions] concat [#"tools.jar" #"sa-jdi.jar"])))
