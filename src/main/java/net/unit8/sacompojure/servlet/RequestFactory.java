package net.unit8.sacompojure.servlet;

import clojure.lang.*;
import org.apache.tomcat.InstanceManager;

import javax.servlet.ServletConfig;
import java.io.UnsupportedEncodingException;

/**
 * @author kawasima
 */
public class RequestFactory {
    private MockServletContext context;
    private MockServletConfig  config;

    public RequestFactory() {
        config = new MockServletConfig();
        context = new MockServletContext();
        context.setAttribute(InstanceManager.class.getName(), new SAInstansManager());
        context.setInitParameter("sastruts.VIEW_PREFIX", "/WEB-INF/view");
        config.setServletContext(context);
    }

    public MockRequest create(IPersistentMap requestMap, String uri) {
        MockRequest request = new MockRequest(context, uri);
        IPersistentMap headers = (IPersistentMap) requestMap.valAt(Keyword.find("headers"));
        for (Object headerObj : headers) {
            MapEntry entry = (MapEntry) headerObj;
            request.addHeader(
                    RT.printString(entry.key()),
                    RT.printString(entry.val()));
        }
        try {
            request.setCharacterEncoding(RT.printString(requestMap.valAt(Keyword.find("character-encoding"))));
        } catch(UnsupportedEncodingException e) {
            // ignore
        }

        Object contentLength = requestMap.valAt(Keyword.find("content-length"));
        if (contentLength != null)
            request.setContentLength(RT.intCast(contentLength));
        request.setContentType(RT.printString(requestMap.valAt(Keyword.find("content-type"))));
        request.setMethod(RT.printString(requestMap.valAt(Keyword.find("request-method"))));
        request.setScheme(RT.printString(requestMap.valAt(Keyword.find("scheme"))));
        request.setQueryString(RT.printString(requestMap.valAt(Keyword.find("query-string"))));

        IPersistentMap params = (IPersistentMap)requestMap.valAt(Keyword.find("params"));
        for (Object paramObj : params) {
            MapEntry entry = (MapEntry) paramObj;
            Object valObj = entry.val();
            Object keyObj = entry.key();
            String name = keyObj instanceof Keyword ?
                    ((Keyword) keyObj).getName() :
                    RT.printString(keyObj);

            if (valObj instanceof IPersistentCollection) {
                ISeq seq = ((IPersistentCollection) valObj).seq();
                SeqIterator iter = new SeqIterator(seq);
                String[] values = new String[seq.count()];
                for (int i=0; iter.hasNext(); i++) {
                    values[i] = RT.printString(iter.next());
                }
                request.setParameter(name, values);
            } else {
                request.setParameter(name, RT.printString(valObj));
            }
        }

        return request;
    }

    public ServletConfig getConfig() {
        return config;
    }
}
