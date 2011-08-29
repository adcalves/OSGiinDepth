package manning.osgi.subscriber;

import org.osgi.framework.BundleEvent;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class SimpleSubscriber implements EventHandler {
    
    public void handleEvent(Event event) {
        
        BundleEvent payload =
            (BundleEvent) event.getProperty(EventConstants.EVENT);
        
        System.out.println("Installed bundle = " +
                payload.getBundle().getSymbolicName());
        
        System.out.println("Installed bundle = " + 
                event.getProperty(EventConstants.BUNDLE_SYMBOLICNAME));
        
    }
        
//        System.out.println("Installed bundle = " +
//                EventConstants.BUNDLE_SYMBOLICNAME);
//        
//        System.out.println("Received event on topic = " + event.getTopic());
//        for (String propertyName : event.getPropertyNames()) {
//            System.out.println("\t" + propertyName + " = " + event.getProperty(propertyName));
//        }
//    }

}
