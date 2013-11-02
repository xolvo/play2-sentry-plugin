package ru.purecode.play2.sentry.utils;

import play.libs.F.Promise;
import play.mvc.Action;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import ru.purecode.play2.sentry.SentryLogger;

public class SentryExceptionsLogger extends Action<LogExceptions> {

	@Override
	public Promise<SimpleResult> call(Context ctx) throws Throwable {
		try {
			return delegate.call(ctx);
		} catch(Throwable t) {
			boolean should_log = true;
			for(Class<? extends Throwable> ex : configuration.exlude()) {
				if(ex.isAssignableFrom(t.getClass())) {
					should_log = false;
				}
			}
			
			if(should_log) {
				UserModel user = null;
				if(ctx.args.get("user") instanceof UserModel)
					user = (UserModel) ctx.args.get("user");
				
				new SentryLogger()
					.withException(t)
					.withRequest(ctx.request())
					.withUser(user)
					.log();
			}
			
			throw t;
		}
	}

}
