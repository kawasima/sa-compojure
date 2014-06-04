(ns s2compojure.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler])
  (:import [org.seasar.framework.container.factory SingletonS2ContainerFactory]
           [org.seasar.framework.container.deployer ComponentDeployerFactory
            ComponentDeployerFactory$DefaultProvider ExternalComponentDeployerProvider]
           [org.seasar.framework.container.external.servlet HttpServletExternalContext HttpServletExternalContextComponentDefRegister]))


(defroutes app-routes
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))

(defn initialize-s2container []
  (when (instance? ComponentDeployerFactory$DefaultProvider (ComponentDeployerFactory/getProvider) )
    (ComponentDeployerFactory/setProvider (ExternalComponentDeployerProvider.)))
  (let [ext-ctx (HttpServletExternalContext.)]
    ;; TODO setApplication
    (SingletonS2ContainerFactory/setExternalContext ext-ctx))
  (SingletonS2ContainerFactory/setExternalContextComponentDefRegister
   (HttpServletExternalContextComponentDefRegister.))
  (SingletonS2ContainerFactory/init))

(defn -main [& args]
  (initialize-s2container))
