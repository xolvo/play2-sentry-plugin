package ru.purecode.play2.sentry.bindings;

import java.io.IOException;

import net.kencochrane.raven.marshaller.json.InterfaceBinding;
import ru.purecode.play2.sentry.interfaces.UserInterface;
import ru.purecode.play2.sentry.utils.UserModel;

import com.fasterxml.jackson.core.JsonGenerator;

public class UserInterfaceBinding implements InterfaceBinding<UserInterface> {

	@Override
	public void writeInterface(JsonGenerator generator, UserInterface userInterface) throws IOException {
		UserModel user = userInterface.getUser();
		
		generator.writeStartObject();
		generator.writeObjectField("id", user._getId());
		generator.writeStringField("username", user.getUsername());
		if(user.getGroupName() != null)
			generator.writeStringField("group", user.getGroupName());
		generator.writeEndObject();
	}

}
