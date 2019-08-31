package playfun.core.controller;

import play.mvc.Catch;
import playfun.core.controller.constraints.ValidationFailException;

public abstract class ViewControllerBase extends ControllerBase{

	@Catch(ValidationFailException.class)
	private static void onValidationFailException(ValidationFailException e) {
		response.status = 400;
//		renderArgs.put("message", e.getMessage());
//		renderTemplate("errors/400.html");
		badRequest(e.getMessage());
	}
	
	
}
