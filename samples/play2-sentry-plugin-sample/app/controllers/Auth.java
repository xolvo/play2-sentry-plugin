package controllers;

import models.User;
import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;

public class Auth extends Action.Simple {

	@Override
	public Promise<SimpleResult> call(Context ctx) throws Throwable {
		User user = new User();
		user.id = 1L;
		user.username = "xolvo";
		ctx.args.put("user", user);
		
		return delegate.call(ctx);
	}

}
