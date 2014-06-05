(ns s2compojure.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [s2compojure.action-mapping :as mapping])
  (:import [org.seasar.framework.container.factory SingletonS2ContainerFactory]
           [org.seasar.framework.container.deployer ComponentDeployerFactory
            ComponentDeployerFactory$DefaultProvider ExternalComponentDeployerProvider]
           [org.seasar.framework.container.external.servlet HttpServletExternalContext HttpServletExternalContextComponentDefRegister]))


(def initialized? (atom false))

(defn initialize-s2container []
  (when (instance? ComponentDeployerFactory$DefaultProvider (ComponentDeployerFactory/getProvider) )
    (ComponentDeployerFactory/setProvider (ExternalComponentDeployerProvider.)))
  (let [ext-ctx (HttpServletExternalContext.)]
    ;; TODO setApplication
    (SingletonS2ContainerFactory/setExternalContext ext-ctx))
  (SingletonS2ContainerFactory/setExternalContextComponentDefRegister
   (HttpServletExternalContextComponentDefRegister.))
  (SingletonS2ContainerFactory/init))

(defn create-app []
  (when-not @initialized?
    (initialize-s2container)
    (reset! initialized? true))
  (handler/site @mapping/action-routes))

(def app (create-app))

(defn -main [& args]
  (initialize-s2container))
