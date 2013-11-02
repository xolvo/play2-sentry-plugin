package ru.purecode.play2.sentry;

import net.kencochrane.raven.DefaultRavenFactory;
import net.kencochrane.raven.Raven;
import net.kencochrane.raven.dsn.Dsn;
import net.kencochrane.raven.marshaller.Marshaller;
import net.kencochrane.raven.marshaller.json.JsonMarshaller;
import ru.purecode.play2.sentry.bindings.PlayHttpRequestInterfaceBinding;
import ru.purecode.play2.sentry.bindings.UserInterfaceBinding;
import ru.purecode.play2.sentry.interfaces.PlayHttpRequestInterface;
import ru.purecode.play2.sentry.interfaces.UserInterface;

public class CustomRavenFactory extends DefaultRavenFactory {
	@Override
	public Raven createRavenInstance(Dsn dsn) {
		Raven raven = new Raven();
        raven.setConnection(createConnection(dsn));
        
        return raven;
	}
	
	@Override
	protected Marshaller createMarshaller(Dsn dsn) {
		JsonMarshaller marshaller = (JsonMarshaller) super.createMarshaller(dsn);
		
		marshaller.addInterfaceBinding(PlayHttpRequestInterface.class, new PlayHttpRequestInterfaceBinding());
		marshaller.addInterfaceBinding(UserInterface.class, new UserInterfaceBinding());
		
		return marshaller;
	}
}
