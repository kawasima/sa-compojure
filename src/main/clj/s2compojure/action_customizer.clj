(ns s2compojure.action-customizer
  (:use [compojure.core :only [ANY]])
  (:gen-class
   :name s2compojure.ActionCustomizer
   :init init
   :implements [org.seasar.framework.container.ComponentCustomizer])
  (:import [java.lang.reflect Modifier]
           [org.seasar.struts.annotation Execute]
           [org.seasar.struts.util ActionUtil]))

(defn -init [])

(defn- method-execution [m]
  (when (Modifier/isPublic (.getModifiers m))
    (.getAnnotation m Execute)))

(defn- setup-method [action-class]
  (loop [clazz action-class]
    (doseq [m (.getDeclaredMethods clazz)]
      (when-let [execute (method-execution m)]
        (println (.getName m))))
    (when (= clazz Object)
      (recur (.getSuperclass clazz)))))

(defn -customize [this component-def]
  (let [action-path (ActionUtil/fromActionNameToPath (.getComponentName component-def))]
    (println action-path)
    (setup-method (.getComponentClass component-def))
    #_(ANY path [:as request])))
