package net.unit8.sacompojure.servlet;

import org.seasar.framework.mock.servlet.MockRequestDispatcherImpl;

import javax.servlet.*;
import javax.servlet.descriptor.JspConfigDescriptor;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ServletContext
 */
public class MockServletContext implements ServletContext {
    private static final int MAJOR_VERSION = 3;
    private static final int MINOR_VERSION = 0;
    private static final String SERVER_INFO = "sa-compojure/1.0";
    private static final Logger logger = Logger.getLogger(MockServletContext.class.getName());

    private final Map<String, String> mimeTypes = new HashMap<String, String>();
    private final Map<String, String> initParameters = new HashMap<String, String>();
    private final Map<String, Object> attributes = new HashMap<String, Object>();


    private String path;
    private File webappDirectory;

    public MockServletContext() {
        this("");
    }

    public MockServletContext(String path) {
        this.path = path;
        webappDirectory = new File("src/main/webapp");
    }

    public void setWebapp(File webapp) {
        this.webappDirectory = webapp;
    }

    @Override
    public String getContextPath() {
        return path;
    }

    @Override
    public ServletContext getContext(String s) {
        throw new UnsupportedOperationException("getContext");
    }

    @Override
    public int getMajorVersion() {
        return MAJOR_VERSION;
    }

    @Override
    public int getMinorVersion() {
        return MINOR_VERSION;
    }

    public int getEffectiveMajorVersion() {
        return MAJOR_VERSION;
    }

    public int getEffectiveMinorVersion() {
        return MINOR_VERSION;
    }

    @Override
    public String getMimeType(String file) {
        return mimeTypes.get(file);
    }

    @Override
    public Set<String> getResourcePaths(String path) {
        URL resource = getResource(path);
        if (resource == null)
            return null;

        try {
            File dir = new File(resource.toURI());
            return new HashSet<String>(Arrays.asList(dir.list()));
        } catch(URISyntaxException e) {
            return null;
        }
    }

    @Override
    public URL getResource(String path) {
        File resource = new File(webappDirectory, path);
        if (resource.exists()) {
            try {
                return resource.toURI().toURL();
            } catch (MalformedURLException e) {
                return null;
            }
        }
        return null;
    }

    @Override
    public String getRealPath(String path) {
        URL url = getResource(path);
        if (url == null)
            return null;

        try {
            return new File(url.toURI()).getAbsolutePath();
        } catch(URISyntaxException e) {
            return null;
        }
    }

    @Override
    public String getServerInfo() {
        return SERVER_INFO;
    }

    @Override
    public String getInitParameter(String name) {
        return initParameters.get(name);
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        final Iterator<String> iter = initParameters.keySet().iterator();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public String nextElement() {
                return iter.next();
            }
        };
    }

    public boolean setInitParameter(String name, String value) {
        initParameters.put(name, value);
        return true;
    }

    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        final Iterator<String> iter = attributes.keySet().iterator();
        return new Enumeration<String>() {
            @Override
            public boolean hasMoreElements() {
                return iter.hasNext();
            }

            @Override
            public String nextElement() {
                return iter.next();
            }
        };
    }

    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    @Override
    public String getServletContextName() {
        return "";
    }

    public ServletRegistration.Dynamic addServlet(String s, String s2) {
        throw new UnsupportedOperationException("addServlet");
    }

    public ServletRegistration.Dynamic addServlet(String s, Servlet servlet) {
        throw new UnsupportedOperationException("addServlet");
    }

    public ServletRegistration.Dynamic addServlet(String s, Class<? extends Servlet> aClass) {
        throw new UnsupportedOperationException("addServlet");
    }

    public <T extends Servlet> T createServlet(Class<T> tClass) throws ServletException {
        throw new UnsupportedOperationException("createServlet");
    }

    public ServletRegistration getServletRegistration(String s) {
        throw new UnsupportedOperationException("getServletRegistration");
    }

    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        throw new UnsupportedOperationException("getServletRegistrations");
    }

    public FilterRegistration.Dynamic addFilter(String s, String s2) {
        throw new UnsupportedOperationException("addFilter");
    }

    public FilterRegistration.Dynamic addFilter(String s, Filter filter) {
        throw new UnsupportedOperationException("addFilter");
    }

    public FilterRegistration.Dynamic addFilter(String s, Class<? extends Filter> aClass) {
        throw new UnsupportedOperationException("addFilter");
    }

    public <T extends Filter> T createFilter(Class<T> tClass) throws ServletException {
        throw new UnsupportedOperationException("createFilter");
    }

    public FilterRegistration getFilterRegistration(String s) {
        throw new UnsupportedOperationException("getFilterRegistration");
    }

    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        throw new UnsupportedOperationException("getFilterRegistrations");
    }

    public SessionCookieConfig getSessionCookieConfig() {
        throw new UnsupportedOperationException("getSessionCookieConfig");
    }

    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        throw new UnsupportedOperationException("setSessionTrackingModes");
    }

    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        throw new UnsupportedOperationException("getDefaultSessionTrackingModes");
    }

    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        throw new UnsupportedOperationException("getEffectiveSessionTrackingModes");
    }

    public void addListener(String s) {
        throw new UnsupportedOperationException("addListener");
    }

    public <T extends EventListener> void addListener(T t) {
        throw new UnsupportedOperationException("addListener");
    }

    public void addListener(Class<? extends EventListener> aClass) {
        throw new UnsupportedOperationException("addListener");
    }

    public <T extends EventListener> T createListener(Class<T> tClass) throws ServletException {
        throw new UnsupportedOperationException("createListener");
    }

    public JspConfigDescriptor getJspConfigDescriptor() {
        throw new UnsupportedOperationException("getJspConfigDescriptor");
    }

    @Override
    public InputStream getResourceAsStream(String path) {
        URL url = getResource(path);
        if (url == null)
            return null;
        try {
            return url.openStream();
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String s) {
        return new MockRequestDispatcherImpl();
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String name) {
        throw new UnsupportedOperationException("getNamedDispatcher");
    }

    public Servlet getServlet(String s) throws ServletException {
        throw new UnsupportedOperationException("getServlet");
    }

    public Enumeration<Servlet> getServlets() {
        throw new UnsupportedOperationException("getServlets");
    }

    public Enumeration<String> getServletNames() {
        throw new UnsupportedOperationException("getServletNames");
    }

    public void log(String msg) {
        logger.log(Level.INFO, msg);
    }

    public void log(Exception e, String msg) {
        logger.log(Level.WARNING, msg, e);
    }

    public void log(String msg, Throwable throwable) {
        logger.log(Level.WARNING, msg, throwable);
    }

    public ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public void declareRoles(String... strings) {

    }
}
