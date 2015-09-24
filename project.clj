(defproject net.unit8/sa-compojure "0.1.0-SNAPSHOT"
  :repositories [["seasar2" "http://maven.seasar.org/maven2"]]
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.signaut/ring-jetty9-adapter "1.2.0"
                  :exclusions [javax.servlet/servlet-api]]
                 [javax.servlet/jstl "1.2"]
                 [org.seasar.container/s2-framework "2.4.47"]
                 [org.seasar.container/s2-extension "2.4.47"]
                 [org.seasar.container/s2-tiger "2.4.47"]
                 [org.apache.geronimo.specs/geronimo-ejb_3.0_spec "1.0"]
                 [org.apache.geronimo.specs/geronimo-jta_1.1_spec "1.0"]
                 [org.apache.geronimo.specs/geronimo-jpa_3.0_spec "1.0"]
                 [org.apache.tomcat/tomcat-jasper "7.0.54" :scope "provided"]
                 [net.unit8.struts/struts-taglib-compatible "1.2.9"]
                 [compojure "1.1.8"]]
  :source-paths ["src/main/clj"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/java"]
  :resource-paths ["src/main/resources"]
  :plugins [[lein-ring "0.8.10"]]
  :aot [s2compojure.action-customizer]
  :ring {:handler s2compojure.core/app}
  :main s2compojure.core
  ;:jvm-opts ["-verbose:class"]
  :profiles {:dev {:dependencies [[junit "4.11"]]}})
