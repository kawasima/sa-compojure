package tutorial.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

public class TokenAction {

	@Resource
	protected HttpServletRequest request;

    /*
	@Execute(validator = false)
	public String index() {
		TokenProcessor.getInstance().saveToken(request);
		return "index.jsp";
	}

	@Execute(validator = false, validate = "validate", input = "index.jsp")
	public String result() {
		return "result.jsp";
	}

	public ActionMessages validate() {
		ActionMessages errors = new ActionMessages();
		if (!TokenProcessor.getInstance().isTokenValid(request, true)) {
			errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"errors.invalid",
				"Token"));
		}
		return errors;
	} */
}