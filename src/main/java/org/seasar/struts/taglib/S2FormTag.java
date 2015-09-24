/*
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.struts.taglib;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.taglib.TagUtils;
import org.apache.struts.taglib.html.Constants;
import org.apache.struts.util.MessageResources;
import org.apache.struts.util.RequestUtils;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.SingletonS2ContainerFactory;
import org.seasar.framework.util.StringUtil;
import org.seasar.struts.util.ActionUtil;
import org.seasar.struts.util.RoutingUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Seasar2用のFormTagです。
 */
public class S2FormTag extends TagSupport {

    private static final long serialVersionUID = 1L;

    // ----------------------------------------------------- Instance Variables

    /**
     * The action URL to which this form should be submitted, if any.
     */
    protected String action = null;

    /**
     * The module configuration for our module.
     */
    protected ModuleConfig moduleConfig = null;

    /**
     * The content encoding to be used on a POST submit.
     */
    protected String enctype = null;

    /**
     * The name of the field to receive focus, if any.
     */
    protected String focus = null;

    /**
     * The index in the focus field array to receive focus.  This only applies if the field
     * given in the focus attribute is actually an array of fields.  This allows a specific
     * field in a radio button array to receive focus while still allowing indexed field
     * names like "myRadioButtonField[1]" to be passed in the focus attribute.
     * @since Struts 1.1
     */
    protected String focusIndex = null;

    /**
     * The line ending string.
     */
    protected static String lineEnd = System.getProperty("line.separator");

    /**
     * The ActionMapping defining where we will be submitting this form
     */
    //protected ActionMapping mapping = null;

    /**
     * The message resources for this package.
     */
    /*
    protected static MessageResources messages =
            MessageResources.getMessageResources(Constants.Package + ".LocalStrings");
*/

    /**
     * The request method used when submitting this form.
     */
    protected String method = null;

    /**
     * The onReset event script.
     */
    protected String onreset = null;

    /**
     * The onSubmit event script.
     */
    protected String onsubmit = null;

    /**
     * Include language attribute in the focus script's &lt;script&gt; element.  This
     * property is ignored in XHTML mode.
     * @since Struts 1.2
     */
    protected boolean scriptLanguage = true;

    /**
     * The ActionServlet instance we are associated with (so that we can
     * initialize the <code>servlet</code> property on any form bean that
     * we create).
     */
    //protected ActionServlet servlet = null;

    /**
     * The style attribute associated with this tag.
     */
    protected String style = null;

    /**
     * The style class associated with this tag.
     */
    protected String styleClass = null;

    /**
     * The identifier associated with this tag.
     */
    protected String styleId = null;

    /**
     * The window target.
     */
    protected String target = null;

    /**
     * The name of the form bean to (create and) use. This is either the same
     * as the 'name' attribute, if that was specified, or is obtained from the
     * associated <code>ActionMapping</code> otherwise.
     */
    protected String beanName = null;

    /**
     * The scope of the form bean to (create and) use. This is either the same
     * as the 'scope' attribute, if that was specified, or is obtained from the
     * associated <code>ActionMapping</code> otherwise.
     */
    protected String beanScope = null;

    /**
     * The type of the form bean to (create and) use. This is either the same
     * as the 'type' attribute, if that was specified, or is obtained from the
     * associated <code>ActionMapping</code> otherwise.
     */
    protected String beanType = null;

    /**
     * The list of character encodings for input data that the server should
     * accept.
     */
    protected String acceptCharset = null;

    /** Controls whether child controls should be 'disabled'. */
    private boolean disabled = false;

    /** Controls whether child controls should be 'readonly'. */
    protected boolean readonly = false;

    // ------------------------------------------------------------- Properties

    /**
     * Return the name of the form bean corresponding to this tag. There is
     * no corresponding setter method; this method exists so that the nested
     * tag classes can obtain the actual bean name derived from other
     * attributes of the tag.
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * Return the action URL to which this form should be submitted.
     */
    public String getAction() {
        return (this.action);
    }

    /**
     * Set the action URL to which this form should be submitted.
     *
     * @param action The new action URL
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * Return the content encoding used when submitting this form.
     */
    public String getEnctype() {
        return (this.enctype);
    }

    /**
     * Set the content encoding used when submitting this form.
     *
     * @param enctype The new content encoding
     */
    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    /**
     * Return the focus field name for this form.
     */
    public String getFocus() {
        return (this.focus);
    }

    /**
     * Set the focus field name for this form.
     *
     * @param focus The new focus field name
     */
    public void setFocus(String focus) {
        this.focus = focus;
    }

    /**
     * Return the request method used when submitting this form.
     */
    public String getMethod() {
        return (this.method);
    }

    /**
     * Set the request method used when submitting this form.
     *
     * @param method The new request method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Return the onReset event script.
     */
    public String getOnreset() {
        return (this.onreset);
    }

    /**
     * Set the onReset event script.
     *
     * @param onReset The new event script
     */
    public void setOnreset(String onReset) {
        this.onreset = onReset;
    }

    /**
     * Return the onSubmit event script.
     */
    public String getOnsubmit() {
        return (this.onsubmit);
    }

    /**
     * Set the onSubmit event script.
     *
     * @param onSubmit The new event script
     */
    public void setOnsubmit(String onSubmit) {
        this.onsubmit = onSubmit;
    }

    /**
     * Return the style attribute for this tag.
     */
    public String getStyle() {
        return (this.style);
    }

    /**
     * Set the style attribute for this tag.
     *
     * @param style The new style attribute
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Return the style class for this tag.
     */
    public String getStyleClass() {
        return (this.styleClass);
    }

    /**
     * Set the style class for this tag.
     *
     * @param styleClass The new style class
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * Return the style identifier for this tag.
     */
    public String getStyleId() {
        return (this.styleId);
    }

    /**
     * Set the style identifier for this tag.
     *
     * @param styleId The new style identifier
     */
    public void setStyleId(String styleId) {
        this.styleId = styleId;
    }

    /**
     * Return the window target.
     */
    public String getTarget() {
        return (this.target);
    }

    /**
     * Set the window target.
     *
     * @param target The new window target
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * Return the list of character encodings accepted.
     */
    public String getAcceptCharset() {
        return acceptCharset;
    }

    /**
     * Set the list of character encodings accepted.
     *
     * @param acceptCharset The list of character encodings
     */
    public void setAcceptCharset(String acceptCharset) {
        this.acceptCharset= acceptCharset;
    }

    /** Sets the disabled event handler. */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /** Returns the disabled event handler. */
    public boolean isDisabled() {
        return disabled;
    }

    /** Sets the readonly event handler. */
    public void setReadonly(boolean readonly) {
        this.readonly = readonly;
    }

    /** Returns the readonly event handler. */
    public boolean isReadonly() {
        return readonly;
    }


    // --------------------------------------------------------- Public Methods

    /**
     * Render the beginning of this form.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doStartTag() throws JspException {

        // Look up the form bean name, scope, and type if necessary
        this.lookup();

        // Create an appropriate "form" element based on our parameters
        StringBuffer results = new StringBuffer();

        results.append(this.renderFormStartElement());
        results.append(this.renderToken());

        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        } catch (IOException e) {
            throw new JspException(e);
        }

        // Store this tag itself as a page attribute
        pageContext.setAttribute(Constants.FORM_KEY, this, PageContext.REQUEST_SCOPE);

        this.initFormBean();

        return (EVAL_BODY_INCLUDE);

    }

    /**
     * Generates the opening <code>&lt;form&gt;</code> element with appropriate
     * attributes.
     */
    protected String renderFormStartElement() {

        StringBuilder results = new StringBuilder("<form");

        // render attributes
        renderName(results);

        renderAttribute(results, "method", getMethod() == null ? "post" : getMethod());
        renderAction(results);
        renderAttribute(results, "accept-charset", getAcceptCharset());
        renderAttribute(results, "class", getStyleClass());
        renderAttribute(results, "enctype", getEnctype());
        renderAttribute(results, "onreset", getOnreset());
        renderAttribute(results, "onsubmit", getOnsubmit());
        renderAttribute(results, "style", getStyle());
        renderAttribute(results, "target", getTarget());

        // Hook for additional attributes
        renderOtherAttributes(results);

        results.append(">");
        return results.toString();
    }

    /**
     * Renders the name of the form.  If XHTML is set to true, the name will
     * be rendered as an 'id' attribute, otherwise as a 'name' attribute.
     */
    protected void renderName(StringBuilder results) {
        if (this.isXhtml()) {
            if (getStyleId() == null) {
                renderAttribute(results, "id", beanName);
            } else {
                renderAttribute(results, "id", getStyleId());
            }
        } else {
            renderAttribute(results, "name", beanName);
            renderAttribute(results, "id", getStyleId());
        }
    }

    private static final String TOKEN_KEY = "net.unit8.sacompojure.TOKEN";
    private static final String TRANSACTION_TOKEN_KEY = "net.unit8.sacompojure.TOKEN";
    /**
     * Generates a hidden input field with token information, if any. The
     * field is added within a div element for HTML 4.01 Strict compliance.
     * @return A hidden input field containing the token.
     * @since Struts 1.1
     */
    protected String renderToken() {
        StringBuffer results = new StringBuffer();
        HttpSession session = pageContext.getSession();

        if (session != null) {
            String token =
                    (String) session.getAttribute(TRANSACTION_TOKEN_KEY);

            if (token != null) {
                results.append("<div><input type=\"hidden\" name=\"");
                results.append(TOKEN_KEY);
                results.append("\" value=\"");
                results.append(token);
                if (this.isXhtml()) {
                    results.append("\" />");
                } else {
                    results.append("\">");
                }
                results.append("</div>");
            }
        }

        return results.toString();
    }

    /**
     * Renders attribute="value" if not null
     */
    protected void renderAttribute(StringBuilder results, String attribute, String value) {
        if (value != null) {
            results.append(" ");
            results.append(attribute);
            results.append("=\"");
            results.append(value);
            results.append("\"");
        }
    }

    /**
     * Render the end of this form.
     *
     * @exception JspException if a JSP exception has occurred
     */
    public int doEndTag() throws JspException {

        // Remove the page scope attributes we created
        pageContext.removeAttribute(BEAN_KEY, PageContext.REQUEST_SCOPE);
        pageContext.removeAttribute(FORM_KEY, PageContext.REQUEST_SCOPE);

        // Render a tag representing the end of our current form
        StringBuilder results = new StringBuilder("</form>");

        // Render JavaScript to set the input focus if required
        if (this.focus != null) {
            results.append(this.renderFocusJavascript());
        }

        // Print this value to our output writer
        JspWriter writer = pageContext.getOut();
        try {
            writer.print(results.toString());
        } catch (IOException e) {
            throw new JspException(messages.getMessage("common.io", e.toString()));
        }

        // Continue processing this page
        return (EVAL_PAGE);

    }

    /**
     * Generates javascript to set the initial focus to the form element given in the
     * tag's "focus" attribute.
     * @since Struts 1.1
     */
    protected String renderFocusJavascript() {
        StringBuffer results = new StringBuffer();

        results.append(lineEnd);
        results.append("<script type=\"text/javascript\"");
        if (!this.isXhtml() && this.scriptLanguage) {
            results.append(" language=\"JavaScript\"");
        }
        results.append(">");
        results.append(lineEnd);

        // xhtml script content shouldn't use the browser hiding trick
        if (!this.isXhtml()) {
            results.append("  <!--");
            results.append(lineEnd);
        }

        // Construct the control name that will receive focus.
        // This does not include any index.
        StringBuffer focusControl = new StringBuffer("document.forms[\"");
        focusControl.append(beanName);
        focusControl.append("\"].elements[\"");
        focusControl.append(this.focus);
        focusControl.append("\"]");

        results.append("  var focusControl = ");
        results.append(focusControl.toString());
        results.append(";");
        results.append(lineEnd);
        results.append(lineEnd);

        results.append("  if (focusControl.type != \"hidden\" && !focusControl.disabled) {");
        results.append(lineEnd);

        // Construct the index if needed and insert into focus statement
        String index = "";
        if (this.focusIndex != null) {
            StringBuffer sb = new StringBuffer("[");
            sb.append(this.focusIndex);
            sb.append("]");
            index = sb.toString();
        }
        results.append("     focusControl");
        results.append(index);
        results.append(".focus();");
        results.append(lineEnd);

        results.append("  }");
        results.append(lineEnd);

        if (!this.isXhtml()) {
            results.append("  // -->");
            results.append(lineEnd);
        }

        results.append("</script>");
        results.append(lineEnd);
        return results.toString();
    }

    // ------------------------------------------------------ Protected Methods


    /**
     * Returns true if this tag should render as xhtml.
     */
    private boolean isXhtml() {
        return TagUtils.getInstance().isXhtml(this.pageContext);
    }

    /**
     * Returns the focusIndex.
     * @return String
     */
    public String getFocusIndex() {
        return focusIndex;
    }

    /**
     * Sets the focusIndex.
     * @param focusIndex The focusIndex to set
     */
    public void setFocusIndex(String focusIndex) {
        this.focusIndex = focusIndex;
    }

    /**
     * Gets whether or not the focus script's &lt;script&gt; element will include the
     * language attribute.
     * @return true if language attribute will be included.
     * @since Struts 1.2
     */
    public boolean getScriptLanguage() {
        return this.scriptLanguage;
    }

    /**
     * Sets whether or not the focus script's &lt;script&gt; element will include the
     * language attribute.
     * @since Struts 1.2
     */
    public void setScriptLanguage(boolean scriptLanguage) {
        this.scriptLanguage = scriptLanguage;
    }


    /**
     * onkeypressのイベントを定義します。
     */
    protected String onkeypress;

    /**
     * onkeyupのイベントを定義します。
     */
    protected String onkeyup;

    /**
     * onkeydownのイベントを定義します。
     */
    protected String onkeydown;

    /**
     * onkeypressのイベント定義を返します。
     * 
     * @return onkeypressのイベント定義
     */
    public String getOnkeypress() {
        return onkeypress;
    }

    /**
     * onkeypressのイベント定義を設定します。
     * 
     * @param onkeypress
     *            onkeypressのイベント定義
     */
    public void setOnkeypress(String onkeypress) {
        this.onkeypress = onkeypress;
    }

    /**
     * onkeyupのイベント定義を返します。
     * 
     * @return onkeyupのイベント定義
     */
    public String getOnkeyup() {
        return onkeyup;
    }

    /**
     * onkeyupのイベント定義を設定します。
     * 
     * @param onkeyup
     *            onkeyupのイベント定義
     */
    public void setOnkeyup(String onkeyup) {
        this.onkeyup = onkeyup;
    }

    /**
     * onkeydownのイベント定義を返します。
     * 
     * @return onkeydownのイベント定義
     */
    public String getOnkeydown() {
        return onkeydown;
    }

    /**
     * onkeydownのイベント定義を設定します。
     * 
     * @param onkeydown
     *            onkeydownのイベント定義
     */
    public void setOnkeydown(String onkeydown) {
        this.onkeydown = onkeydown;
    }

    @Override
    protected void lookup() throws JspException {
        moduleConfig = TagUtils.getInstance().getModuleConfig(pageContext);
        if (moduleConfig == null) {
            JspException e = new JspException(messages
                    .getMessage("formTag.collections"));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        if (action == null) {
            action = ActionUtil.calcActionPath();
        } else if (!action.startsWith("/")) {
            action = ActionUtil.calcActionPath() + action;
        }
        String path = action;
        String queryString = "";
        int index = action.indexOf('?');
        if (index >= 0) {
            path = action.substring(0, index);
            queryString = action.substring(index);
        }
        String[] names = StringUtil.split(path, "/");
        S2Container container = SingletonS2ContainerFactory.getContainer();
        StringBuilder sb = new StringBuilder(50);
        for (int i = 0; i < names.length; i++) {
            if (container.hasComponentDef(sb + names[i] + "Action")) {
                String actionPath = RoutingUtil.getActionPath(names, i);
                S2ActionMapping s2mapping = (S2ActionMapping) moduleConfig
                        .findActionConfig(actionPath);
                String paramPath = RoutingUtil.getParamPath(names, i + 1);
                if (StringUtil.isEmpty(paramPath)) {
                    mapping = s2mapping;
                    action = s2mapping.getPath() + "/" + queryString;
                    break;
                }
                S2ExecuteConfig executeConfig = s2mapping
                        .findExecuteConfig(paramPath);
                if (executeConfig != null) {
                    mapping = s2mapping;
                    break;
                }
            }
            if (container.hasComponentDef(sb + "indexAction")) {
                String actionPath = RoutingUtil.getActionPath(names, i - 1)
                        + "/index";
                String paramPath = RoutingUtil.getParamPath(names, i);
                S2ActionMapping s2mapping = (S2ActionMapping) moduleConfig
                        .findActionConfig(actionPath);
                if (StringUtil.isEmpty(paramPath)) {
                    mapping = s2mapping;
                    action = RoutingUtil.getActionPath(names, i - 1)
                            + queryString;
                    break;
                }
                S2ExecuteConfig executeConfig = s2mapping
                        .findExecuteConfig(paramPath);
                if (executeConfig != null) {
                    mapping = s2mapping;
                    break;
                }
            }
            sb.append(names[i] + "_");
        }
        if (mapping == null && container.hasComponentDef(sb + "indexAction")) {
            String actionPath = RoutingUtil.getActionPath(names,
                    names.length - 1)
                    + "/index";
            mapping = (ActionMapping) moduleConfig.findActionConfig(actionPath);
            action = RoutingUtil.getActionPath(names, names.length - 1) + "/";
        }
        if (mapping == null) {
            JspException e = new JspException(messages.getMessage(
                    "formTag.mapping", action));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        FormBeanConfig formBeanConfig = moduleConfig.findFormBeanConfig(mapping
                .getName());
        if (formBeanConfig == null) {
            JspException e = new JspException(messages.getMessage(
                    "formTag.formBean", mapping.getName(), action));
            pageContext.setAttribute(Globals.EXCEPTION_KEY, e,
                    PageContext.REQUEST_SCOPE);
            throw e;
        }
        beanName = mapping.getAttribute();
        beanScope = mapping.getScope();
        beanType = formBeanConfig.getType();
    }

    protected void initFormBean() throws JspException {
        int scope = PageContext.SESSION_SCOPE;
        if ("request".equalsIgnoreCase(beanScope)) {
            scope = PageContext.REQUEST_SCOPE;
        }

        Object bean = pageContext.getAttribute(beanName, scope);
        if (bean == null) {
            bean = RequestUtils.createActionForm(
                    (HttpServletRequest) pageContext.getRequest(), mapping,
                    moduleConfig, servlet);
            if (bean == null) {
                throw new JspException(messages.getMessage("formTag.create",
                        beanType));
            }
            pageContext.setAttribute(beanName, bean, scope);
        }
        pageContext.setAttribute(Constants.BEAN_KEY, bean,
                PageContext.REQUEST_SCOPE);
    }

    protected void renderAction(StringBuilder results) {
        HttpServletRequest request = (HttpServletRequest) pageContext
                .getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext
                .getResponse();
        results.append(" action=\"");
        String contextPath = request.getContextPath();
        StringBuffer value = new StringBuffer();
        if (contextPath.length() > 1) {
            value.append(contextPath);
        }
        value.append(action);
        results.append(response.encodeURL(value.toString()));
        results.append("\"");
    }

    @Override
    public void release() {
        super.release();
        action = null;
        moduleConfig = null;
        enctype = null;
        disabled = false;
        focus = null;
        focusIndex = null;
        mapping = null;
        method = null;
        onreset = null;
        onsubmit = null;
        readonly = false;
        servlet = null;
        style = null;
        styleClass = null;
        styleId = null;
        target = null;
        acceptCharset = null;
        onkeypress = null;
        onkeyup = null;
        onkeydown = null;
    }

    protected void renderOtherAttributes(StringBuilder results) {
        renderAttribute(results, "onkeypress", onkeypress);
        renderAttribute(results, "onkeyup", onkeyup);
        renderAttribute(results, "onkeydown", onkeydown);
    }

}