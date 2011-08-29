package manning.osgi.publisher;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.EventAdmin;

public class PublisherActivator implements BundleActivator {
    
    public void start(BundleContext context) throws Exception {
        LoginEventPublisher publisher = 
            getPublisher(context);
        
        publisher.sendLoginEvent("anonymous");
        publisher.sendLoginEvent("alex");
    }

    private LoginEventPublisher getPublisher(BundleContext context) {
        ServiceReference ref =
            context.getServiceReference(EventAdmin.class.getName());
        
        LoginEventPublisher publisher = null;
        
        if (ref != null) {
            EventAdmin eventAdmin = 
                (EventAdmin) context.getService(ref);
            publisher = 
                new LoginEventPublisher(eventAdmin);
        }
        
        return publisher;
    }

    public void stop(BundleContext context) throws Exception {
        // ...
    }
}
     