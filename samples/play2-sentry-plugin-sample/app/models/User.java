package models;

import ru.purecode.play2.sentry.utils.UserModel;

public class User implements UserModel {
	public Long id;
	public String username;
	
	
	@Override
	public Object _getId() {
		return id;
	}

	@Override
	public String getGroupName() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

}
