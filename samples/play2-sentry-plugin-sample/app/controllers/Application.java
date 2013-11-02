package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import ru.purecode.play2.sentry.utils.LogExceptions;
import exceptions.SystemException;

@With(Auth.class)
@LogExceptions
public class Application extends Controller {

	public static Result index() throws SystemException {
		Integer i = 2;
		
		if(i == 1)
			throw new RuntimeException("Oops... Error!");
		
		if(i == 2)
			throw new SystemException();
		
		return ok();
	}
	
	public static Result index2() {
		throw new RuntimeException("Error from index2");
	}

}
