package ru.purecode.play2.sentry.interfaces;

import net.kencochrane.raven.event.interfaces.SentryInterface;
import play.mvc.Http.Request;

@SuppressWarnings("serial")
public class PlayHttpRequestInterface implements SentryInterface {
	private final Request request;
	
	public PlayHttpRequestInterface(Request request) {
		this.request = request;
	}

	@Override
	public String getInterfaceName() {
		return "request";
	}
	
	public Request getRequest() {
		return request;
	}
}
