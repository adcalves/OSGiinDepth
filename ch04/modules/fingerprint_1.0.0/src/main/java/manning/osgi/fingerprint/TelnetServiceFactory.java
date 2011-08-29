package manning.osgi.fingerprint;

import org.osgi.framework.ServiceRegistration;
import org.osgi.framework.Bundle;

import org.osgi.framework.ServiceFactory;

public class TelnetServiceFactory implements ServiceFactory {

    public Object getService(Bundle bundle, ServiceRegistration registration) {
        return new TelnetImpl();
    }

    public void ungetService(Bundle bundle, ServiceRegistration registration, 
            Object serviceImplementation) {
    }
}
