package ru.purecode.play2.sentry;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import net.kencochrane.raven.event.Event.Level;
import net.kencochrane.raven.event.EventBuilder;
import net.kencochrane.raven.event.interfaces.ExceptionInterface;
import play.Logger;
import play.Play;
import play.mvc.Http.Request;
import ru.purecode.play2.sentry.interfaces.PlayHttpRequestInterface;
import ru.purecode.play2.sentry.interfaces.UserInterface;
import ru.purecode.play2.sentry.utils.UserModel;

public class SentryLogger {
	private static String format(String str, Object... args) {
		try {
            if(args != null && args.length > 0) {
                return String.format(str, args);
            }
            return str;
        } catch (Exception e) {
            return str;
        }
	}
	
	private static String determineCulprit(Throwable throwable) {
		Throwable currentThrowable = throwable;
        String culprit = null;
        
        while (currentThrowable != null) {
            StackTraceElement[] elements = currentThrowable.getStackTrace();
            if (elements.length > 0) {
                StackTraceElement trace = elements[0];
                culprit = trace.getClassName() + "." + trace.getMethodName();
            }
            currentThrowable = currentThrowable.getCause();
        }
        
        return culprit;
	}
	
	private EventBuilder builder;
	
	public SentryLogger() {
		builder = new EventBuilder();
	}
	
	public SentryLogger setLevel(Level level) {
		builder.setLevel(level);
		return this;
	}
	
	public SentryLogger setMessage(String message, Object... args) {
		builder.setMessage(format(message, args));
		return this;
	}
	
	public SentryLogger setCulprit(String culprit) {
		builder.setCulprit(culprit);
		return this;
	}
	
	public SentryLogger setLogger(String logger) {
		builder.setLogger(logger);
		return this;
	}
	
	public SentryLogger withException(Throwable t) {
		if(t != null)
			builder
				.setMessage(t.getMessage())
				.setCulprit(determineCulprit(t))
				.addSentryInterface(new ExceptionInterface(t));
		
		return this;
	}
	
	public SentryLogger withRequest(Request request) {
		builder.addSentryInterface(new PlayHttpRequestInterface(request));
		return this;
	}
	
	public SentryLogger withUser(UserModel user) {
		if(user != null)
			builder.addSentryInterface(new UserInterface(user));
		
		return this;
	}
	
	public SentryLogger addTags(Map<String, String> tags) {
		if(tags != null) {
			for(String tag : tags.keySet()) {
				addTag(tag, tags.get(tag));
			}
		}
		
		return this;
	}
	
	public SentryLogger addTag(String tag, String value) {
		if(tag != null && value != null)
			builder.addTag(tag, value);
		
		return this;
	}
	
	public SentryLogger addExtras(Map<String, Object> extras) {
		if(extras != null) {
			for(String extra : extras.keySet()) {
				builder.addExtra(extra, extras.get(extra));
			}
		}
		
		return this;
	}
	
	public SentryLogger addExtra(String extra, Object value) {
		if(extra != null && value != null)
			builder.addExtra(extra, value);
		
		return this;
	}
	
	public void log() {
		try {
			builder.setServerName(InetAddress.getLocalHost().getHostName());
		} catch(UnknownHostException e) {
			Logger.warn("Can not set SERVER_NAME for Raven client", e);
		}
		
		SentryPlugin plugin = Play.application().plugin(SentryPlugin.class);
		
		if(plugin != null && plugin.enabled())
			SentryPlugin.raven().sendEvent(builder.build());
	}
}
