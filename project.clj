(defproject net.unit8/sa-compojure "0.1.0-SNAPSHOT"
  :repositories [["seasar2" "http://maven.seasar.org/maven2"]]
  :dependencies [[org.clojure/clojure "1.7.0"]
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
                 [net.unit8.struts/struts-taglib-compatible "1.2.9" :exclusions [commons-fileupload]]
                 [compojure "1.4.0"]]
  :source-paths ["src/main/clj"]
  :java-source-paths ["src/main/java"]
  :test-paths ["src/test/java"]
  :resource-paths ["src/main/resources"]
  :plugins [[lein-ring "0.9.6"]]
  :aot [sa-compojure.action-customizer]
  :ring {:handler sa-compojure.core/app}
  :main sa-compojure.core
  :javac-options ["-target" "1.7" "-source" "1.7" "-Xlint:-options"]
  :pom-plugins [[org.apache.maven.plugins/maven-compiler-plugin "3.3"
                 {:configuration ([:source "1.7"] [:target "1.7"])}]]
  
  :profiles {:dev {:dependencies [[junit "4.11"]]}})
