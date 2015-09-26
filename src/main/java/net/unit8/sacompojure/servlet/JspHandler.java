package net.unit8.sacompojure.servlet;

import org.apache.jasper.EmbeddedServletOptions;
import org.apache.jasper.Options;
import org.apache.jasper.compiler.JspRuntimeContext;
import org.apache.jasper.compiler.Localizer;
import org.apache.jasper.security.SecurityUtil;
import org.apache.jasper.servlet.JspServlet;
import org.apache.jasper.servlet.JspServletWrapper;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author kawasima
 */
public class JspHandler {
    private final transient Log log = LogFactory.getLog(JspHandler.class);

    private JspRuntimeContext runtimeContext;
    private Options options;
    private ServletConfig config;
    private ServletContext context;

    public JspHandler() {
    }

    public void setConfig(ServletConfig config) {
        this.config = config;
        context = config.getServletContext();
        options = new EmbeddedServletOptions(config, context);
        runtimeContext = new JspRuntimeContext(context, options);
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, String jspUri, boolean precompile)
            throws IOException, ServletException {
        JspServletWrapper wrapper = runtimeContext.getWrapper(jspUri);
        if (wrapper == null) {
            synchronized (this) {
                wrapper = runtimeContext.getWrapper(jspUri);
                if (wrapper == null) {
                    if (context.getResource(jspUri) == null) {
                        handleMissingResource(request, response, jspUri);
                        return;
                    }
                    wrapper = new JspServletWrapper(config, options, jspUri, runtimeContext);
                    runtimeContext.addWrapper(jspUri, wrapper);
                }
            }
        }

        try {
            wrapper.service(request, response, precompile);
        } catch (FileNotFoundException e) {
            handleMissingResource(request, response, jspUri);
        }
    }

    private void handleMissingResource(HttpServletRequest request, HttpServletResponse response, String jspUri)
            throws ServletException, IOException {
        String includeRequestUri =
                (String) request.getAttribute("javax.servlet.include.request_uri");
        if (includeRequestUri != null) {
            String msg = Localizer.getMessage("jsp.error.file.not.found", jspUri);
            throw new ServletException(SecurityUtil.filter(msg));
        } else {
            try {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, request.getRequestURI());
            } catch (IllegalStateException e) {
                log.error(Localizer.getMessage("jsp.error.file.not.found", jspUri));
            }
        }
    }
}
