package ru.purecode.play2.sentry;

import java.io.IOException;

import net.kencochrane.raven.Raven;
import net.kencochrane.raven.RavenFactory;
import net.kencochrane.raven.dsn.Dsn;
import play.Application;
import play.Logger;
import play.Plugin;

public class SentryPlugin extends Plugin {
	private static Raven raven;
	
	private final Application application;
	
	public SentryPlugin(final Application application) {
		this.application = application;
	}
	
	@Override
	public void onStart() {
		String dsn_string = application.configuration().getString("sentry.dsn");
		if(dsn_string == null) {
			Logger.error("Can not load Sentry plugin. Define `sentry.dsn` in configuration!");
			return;
		}
		
		RavenFactory.registerFactory(new CustomRavenFactory());
		
		Dsn dsn = new Dsn(dsn_string);
		raven = RavenFactory.ravenInstance(dsn);
    }
	
	@Override
	public void onStop() {
		if(raven != null) {
			try {
				raven.getConnection().close();
				raven = null;
			} catch (IOException e) {
				Logger.error("Can not close Raven", e);
			}
		}
	}
	
	@Override
	public boolean enabled() {
		return application.configuration().getBoolean("sentry.enabled", true);
	}
	
	public static Raven raven() {
		return raven;
	}
}
