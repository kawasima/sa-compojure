package tutorial.action;

public class UploadAction {
    /*

	@ActionForm
	@Resource
	protected UploadForm uploadForm;

	@Resource
	protected HttpServletRequest request;

	@Resource
	protected ServletContext application;

	@Execute(validator = false)
	public String index() {
		UploadUtil.checkSizeLimit(request);
		return "index.jsp";
	}

	@Execute(input = "index.jsp")
	public String upload() {
		upload(uploadForm.formFile);
		for (FormFile file : uploadForm.formFiles) {
			upload(file);
		}
		return "index.jsp";
	}

	protected void upload(FormFile file) {
		String path = application.getRealPath("/WEB-INF/work/"
			+ file.getFileName());
		UploadUtil.write(path, file);
	}
	*/
}