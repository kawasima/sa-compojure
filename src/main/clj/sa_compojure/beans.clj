(ns sa-compojure.beans
  (:import [java.lang.reflect Array]
           [java.util Map List Arrays]
           [org.apache.commons.beanutils DynaBean DynaClass DynaProperty]
           [org.seasar.framework.beans.factory BeanDescFactory]
           [org.seasar.framework.util ClassUtil ModifierUtil]
           [org.seasar.struts.action WrapperUtil]))

(defn get-property [bean name]
  (let [bean-desc (BeanDescFactory/getBeanDesc (class bean))]
    (.getPropertyDesc bean-desc name)))

(defn wrap-dyna-property [prop-desc]
  (proxy [DynaProperty] [(.getPropertyName prop-desc)]
    (getName [] (.getPropertyName prop-desc))
    (getType [] (.getPropertyType prop-desc))
    (isIndexed [] false)
    (isMapped [] false)))

(defn- create-prop-map [bean-class]
  (let [bean-desc (BeanDescFactory/getBeanDesc bean-class)]
    (->> (range (.getPropertyDescSize bean-desc))
         (map #(.getPropertyDesc bean-desc %))
         (reduce #(assoc %1 (.getPropertyName %2) (wrap-dyna-property %2)) {}))))

(defn wrap-dyna-class [bean-class]
  (let [prop-map (create-prop-map bean-class)]
    (reify DynaClass
      (getDynaProperties [this]
        (into-array DynaProperty (vals prop-map)))
      (getDynaProperty [this name]
        (get prop-map name))
      (getName [this]
        (.getName bean-class))
      (newInstance [this]
        (.newInstance bean-class)))))

(defn wrap-dyna-bean [bean]
  (reify DynaBean
    (getDynaClass [this]
      (wrap-dyna-class (class bean)))
    (get [this ^String name]
      (.getValue (get-property bean name) bean))
    
    (contains [this name key]
      (throw (UnsupportedOperationException. "contains")))
    (get [this ^String name ^int index]
      (let [value (.getValue (get-property bean name) bean)]
        (cond
          (nil? value) (throw (IllegalStateException.
                               (str "This value of property(" name ") is null")))
          (.isArray (class value)) (Array/get value index)
          (instance? List value) (.get value index)
          :else (throw (IllegalStateException.
                        (str "This value of property(" name ") is not mapped"))))))
    (get [this ^String name ^String key]
      (let [value (.getValue (get-property bean name) bean)]
        (cond
          (nil? value) (throw (IllegalStateException.
                               (str "This value of property(" name ") is null")))
          (instance? Map value) (.getValue value key)
          :else (throw (IllegalStateException.
                        (str "This value of property(" name ") is not mapped"))))))
    (remove [this name key]
      (throw (UnsupportedOperationException. "remove"))
      nil)
    
    (^void set [this ^String name value]
      (throw (UnsupportedOperationException. "set")))
    (^void set [this ^String name ^int index value]
      (throw (UnsupportedOperationException. "set")))
    (^void set [this ^String name ^String key value]
      (throw (UnsupportedOperationException. "set")))))

(defn string-array? [v]
  (and (not (nil? v))
       (.isArray (class v))
       (= (.getComponentType (class v)) String)))

(defn set-map-property [m k v]
  (if (string-array? v)
    (.put m k (if (> (.-length v) 0) (aget v 0) nil))
    (.put m k v)))

(defn set-simple-property [bean k v]
  (if (instance? Map bean)
    (set-map-property bean k v)
    (let [bean-desc (BeanDescFactory/getBeanDesc (class bean))]
      (when (.hasPropertyDesc bean-desc k)
        (let [prop-desc (.getPropertyDesc bean-desc k)]
          (when (.isWritable prop-desc)
            (cond
              (.. prop-desc getPropertyType isArray)
              (.setValue prop-desc bean v)

              (.isAssignableFrom List (.getPropertyType prop-desc))
              (let [list (if (ModifierUtil/isAbstract (.getPropertyType prop-desc))
                           (java.util.ArrayList.)
                           (ClassUtil/newInstance (.getPropertyType prop-desc)))]
                (.addAll list (Arrays/asList v))
                (.setValue prop-desc bean list))

              (nil? v)
              (.setValue prop-desc bean nil)

              (string-array? v)
              (.setValue prop-desc bean (if (> (.-length v) 0) (aget v 0) nil))

              :else
              (.setValue prop-desc bean v))))))))

(defn set-property [bean k v]
  (when bean
    (let [nested-index  (.indexOf k ".")
          indexed-index (.indexOf k "[")
          mapped-index  (.indexOf k "(")]
      (if (and (< nested-index 0) (< indexed-index 0) (< mapped-index 0))
        (set-simple-property bean k v)
        ;; TODO List, Map
        ))))

(defn populate [form params]
  (doseq [[k v] params]
    (set-property form (name k) v)))


(defn exportable? [pd]
  (not
   (or
    (.. pd getPropertyType getName (startsWith "javax.servlet"))
    (= (.getPropertyName pd) "requestScope")
    (= (.getPropertyName pd) "sessionScope")
    (= (.getPropertyName pd) "applicationScope"))))

(defn export-properties [bean request]
  (let [bean-desc (BeanDescFactory/getBeanDesc (class bean))
        prop-descs (->> (range (.getPropertyDescSize bean-desc))
                        (map #(.getPropertyDesc bean-desc %))
                        (filter #(and (.isReadable %) (exportable? %))))]
    (doseq [pd prop-descs]
      (let [value (WrapperUtil/convert (.getValue pd bean))]
        (when value
          (.setAttribute request (.getPropertyName pd) value))))))
