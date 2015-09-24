package net.unit8.sacompojure.servlet;

import org.junit.Test;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * @author kawasima
 */
public class JspHandlerTest {
    @Test
    public void test() throws IOException, ServletException {
        MockServletConfig config = new MockServletConfig();
        MockServletContext context = new MockServletContext();
        config.setServletContext(context);
        MockRequest request = new MockRequest(context, "/WEB-INF/view/index.jsp");
        MockResponse response = new MockResponse(request);
        JspHandler jspHandler = new JspHandler();
        jspHandler.handle(request, response, "/WEB-INF/view/index.jsp", false);
        System.out.println(response.getResponseString());
    }
}
