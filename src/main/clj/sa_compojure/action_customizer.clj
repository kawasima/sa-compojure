(ns sa-compojure.action-customizer
  (:require [compojure.core :refer [ANY routes]]
            (sa-compojure [action-mapping :as mapping]
                          [beans :refer [wrap-dyna-bean] :as beans])
            [clojure.string :as string])
  (:gen-class
   :name s2compojure.ActionCustomizer
   :init init
   :implements [org.seasar.framework.container.ComponentCustomizer])
  (:import [java.lang.reflect Modifier]
           [org.seasar.struts.annotation Execute ActionForm]
           [org.seasar.struts.util ActionUtil]
           [org.seasar.framework.beans.factory BeanDescFactory]
           [org.seasar.framework.container SingletonS2Container]
           [org.seasar.framework.container.factory SingletonS2ContainerFactory]
           [net.unit8.sacompojure.servlet JspHandler MockResponse RequestFactory]))

(defonce jsp-handler (JspHandler.))
(defonce request-factory (RequestFactory.))
(defonce action-config (atom {}))

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

(defn find-action-config [component-def]
  (get @action-config component-def))

(defn- execute [component-def method request]
  (let [action (.getComponent component-def)
        action-config (find-action-config component-def)
        http-request  (.create request-factory request (:uri request))
        http-response (MockResponse. http-request)]
    (.. (SingletonS2ContainerFactory/getContainer)
      (getExternalContext)
      (setRequest http-request))
    
    (.. (SingletonS2ContainerFactory/getContainer)
      (getExternalContext)
      (setResponse http-response))

    (.. (SingletonS2ContainerFactory/getContainer)
      (getExternalContext)
      (setApplication (.getServletContext http-request)))

    (let [form-field (:action-form-field action-config)
          form (.newInstance (.getType form-field))]
      (beans/populate form (:params request))
      (.set form-field action form))
    (let [path (.invoke method action (object-array []))]
      (.setAttribute http-request
                     (get-in action-config [:action-mapping :name])
                     (wrap-dyna-bean (.get (:action-form-field action-config) action)))
      (beans/export-properties action http-request)
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

(defn- action-form-field [action-class]
  (let [bean-desc (BeanDescFactory/getBeanDesc action-class)
        field-size (.getFieldSize bean-desc)]
    (->> (range field-size)
         (map #(.getField bean-desc %))
         (filter #(.getAnnotation % ActionForm))
         first)))

(defn- create-action-config [component-def]
  (let [action-class (.getComponentClass component-def)]
    {:action-mapping {:name (str (.getComponentName component-def) "Form")}
     :action-form-field (action-form-field action-class)}))

(defn- setup-method [component-def]
  (let [action-routes (atom [])
        action-path (ActionUtil/fromActionNameToPath (.getComponentName component-def))
        action-class (.getComponentClass component-def)]
    (loop [clazz action-class]
      (doseq [m (.getDeclaredMethods clazz)]
        (when-let [execution (method-execution m)]
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
  (swap! action-config assoc component-def (create-action-config component-def)) 
  (when-let [action-routes (setup-method component-def)]
    (apply mapping/add-routes action-routes)))


