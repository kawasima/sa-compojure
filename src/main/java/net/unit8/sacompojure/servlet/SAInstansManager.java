package net.unit8.sacompojure.servlet;

import org.apache.tomcat.InstanceManager;

import javax.naming.NamingException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author kawasima
 */
public class SAInstansManager implements InstanceManager {
    private ClassLoader loader;

    public SAInstansManager() {
        loader = Thread.currentThread().getContextClassLoader();
    }
    @Override
    public Object newInstance(Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException {
        return clazz.newInstance();
    }

    @Override
    public Object newInstance(String className) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return loader.loadClass(className).newInstance();
    }

    @Override
    public Object newInstance(String fqcn, ClassLoader classLoader) throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException {
        return classLoader.loadClass(fqcn).newInstance();
    }

    @Override
    public void newInstance(Object o) throws IllegalAccessException, InvocationTargetException, NamingException {

    }

    @Override
    public void destroyInstance(Object o) throws IllegalAccessException, InvocationTargetException {

    }
}
