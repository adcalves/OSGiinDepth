package manning.osgi.sab;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

@SuppressWarnings({"unchecked","rawtypes"})
public class SubscriberFactory {
    
    private BundleContext bundleContext;
    private String topic;
    private String id;
    private Object target;
    private ServiceRegistration serviceReference;

    public void setId(String id) {
        this.id = id;
    }
    
    public void setBlueprintBundleContext(BundleContext blueprintBundleContext) {
        this.bundleContext = blueprintBundleContext;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void init() {
        Dictionary properties = new Hashtable();
        properties.put("COMPONENT_ID", id);
        properties.put(EventConstants.EVENT_TOPIC, topic);

        serviceReference =
            bundleContext.registerService(
                EventHandler.class.toString(), target, properties);        
    }
    
    public void destroy() {
        if (serviceReference != null) {
            serviceReference.unregister();
        }
    }
}
