(ns sa-compojure.action-customizer
  (:use [compojure.core :only [ANY routes]])
  (:require [sa-compojure.action-mapping :as mapping]
            [clojure.string :as string])
  (:gen-class
   :name s2compojure.ActionCustomizer
   :init init
   :implements [org.seasar.framework.container.ComponentCustomizer])
  (:import [java.lang.reflect Modifier]
           [org.seasar.struts.annotation Execute]
           [org.seasar.struts.util ActionUtil]
           [org.seasar.framework.container.factory SingletonS2ContainerFactory]
           [net.unit8.sacompojure.servlet JspHandler MockResponse RequestFactory CompojureExecuteConfig]))

(def jsp-handler (JspHandler.))
(def request-factory (RequestFactory.))
(defn -init []
  (.setConfig jsp-handler (.getConfig request-factory)))

(defn- method-execution [m]
  (when (Modifier/isPublic (.getModifiers m))
    (.getAnnotation m Execute)))

(defn- get-action-path [component-name]
  (when-not (.endsWith component-name "Action")
    (throw (IllegalArgumentException. component-name)))
  (if (= component-name "indexAction") "/"
    (let [component-name (if (.endsWith component-name "indexAction")
                           (.substring component-name 0 (- (.length component-name) 12))
                           (.substring component-name 0 (- (.length component-name) 6)))]
      (str "/" (string/replace component-name #"_" "/") "/"))))

(defn- execute [component-def method request]
  (let [action (.getComponent component-def)
        http-request  (.create request-factory request (:uri request))
        http-response (MockResponse. http-request)]
    (.. (SingletonS2ContainerFactory/getContainer)
      (getExternalContext)
      (setRequest http-request))
    (.. (SingletonS2ContainerFactory/getContainer)
      (getExternalContext)
      (setResponse http-response))
    (let [path (.invoke method action (object-array []))]
      (println (str "/WEB-INF/view" (get-action-path (.getComponentName component-def))
                 path))
      (.setServletPath http-request
        (str "/WEB-INF/view" (get-action-path (.getComponentName component-def)) path))
      (.handle jsp-handler
        http-request
        http-response
        (str "/WEB-INF/view" (get-action-path (.getComponentName component-def)) path)
        false)
      {:status (.getStatus http-response)
       :headers {"Content-Type"  "text/html; charset=UTF-8"}
       :body (.getResponseString http-response)})))

(defn- create-execute-config [m]
  (let [execute (.getAnnotation m Execute)
        execute-config (CompojureExecuteConfig.)]
    (doto execute-config
      (.setUrlPattern (.urlPattern execute)))
    execute-config))

(defn- setup-method [component-def]
  (let [action-routes (atom [])
        action-path (ActionUtil/fromActionNameToPath (.getComponentName component-def))
        action-class (.getComponentClass component-def)]
    (loop [clazz action-class]
      (doseq [m (.getDeclaredMethods clazz)]
        (when-let [execution (method-execution m)]
          (println (str action-path "/" (.getName m)))
          (swap! action-routes conj
            (ANY (str action-path "/" (.getName m)) [:as request]
              (execute component-def m request)))
          (when (= (.getName m) "index")
            (swap! action-routes conj
              (ANY (str action-path "/") [:as request]
                (execute component-def m request))))))

      (if (= clazz Object)
        @action-routes
        (recur (.getSuperclass clazz))))))

(defn -customize [this component-def]
  (when-let [action-routes (setup-method component-def)]
    (apply mapping/add-routes action-routes)))
