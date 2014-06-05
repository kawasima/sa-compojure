(ns s2compojure.action-customizer
  (:use [compojure.core :only [ANY routes]])
  (:require [s2compojure.action-mapping :as mapping])
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

(defn- setup-method [component-def]
  (let [action-routes (atom [])
        action-path (ActionUtil/fromActionNameToPath (.getComponentName component-def))
        action-class (.getComponentClass component-def)]
    (loop [clazz action-class]
      (doseq [m (.getDeclaredMethods clazz)]
        (when-let [execute (method-execution m)]
          (println (str action-path "/" (.getName m)))
          (swap! action-routes conj
            (ANY (str action-path "/" (.getName m)) [:as r]
              (let [action (.getComponent component-def)]
                (.invoke m action (object-array [])))))))
      (if (= clazz Object)
        @action-routes
        (recur (.getSuperclass clazz))))))

(defn -customize [this component-def]
  (when-let [action-routes (setup-method component-def)]
    (apply mapping/add-routes action-routes)))
