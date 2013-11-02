package ru.purecode.play2.sentry.interfaces;

import ru.purecode.play2.sentry.utils.UserModel;
import net.kencochrane.raven.event.interfaces.SentryInterface;

@SuppressWarnings("serial")
public class UserInterface implements SentryInterface {
	private final UserModel user;
	
	public UserInterface(UserModel user) {
		this.user = user;
	}
	
	@Override
	public String getInterfaceName() {
		return "user";
	}

	public UserModel getUser() {
		return user;
	}
}
