package manning.osgi.subscriber;

import java.util.Dictionary;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class SubscriberActivator implements BundleActivator {
    
    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        SimpleSubscriber subscriber = new SimpleSubscriber();
        
        Dictionary dict = new Properties();
        dict.put(EventConstants.EVENT_TOPIC, "manning/osgi/login");
        dict.put(EventConstants.EVENT_FILTER, "(userid=alex)");

        context.registerService(EventHandler.class.getName(), 
                subscriber, dict);
        
//        Dictionary dict = new Properties();
//        dict.put(EventConstants.EVENT_TOPIC, "manning/osgi/travelagent/hotel");
//        dict.put(EventConstants.EVENT_FILTER, "(price<100)");
//
//        context.registerService(EventHandler.class.getName(), 
//                subscriber, dict);
    }


    public void stop(BundleContext context) throws Exception {
        // ...
    }
}
     