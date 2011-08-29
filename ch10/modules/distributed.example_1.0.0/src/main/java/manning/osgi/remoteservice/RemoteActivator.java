package manning.osgi.remoteservice;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.EventHandler;

public class RemoteActivator  implements BundleActivator {

    private Object subscriber;

    @SuppressWarnings("unchecked")
    public void start(BundleContext bundleContext) throws Exception {
        @SuppressWarnings("rawtypes")
        Dictionary props = new Hashtable();

        props.put("service.exported.interfaces", "*");
        props.put("service.exported.configs", "org.apache.cxf.ws");

        props.put("org.apache.cxf.ws.address", "http://localhost:9090/eventhandler");

        bundleContext.registerService(EventHandler.class.getName(), 
                subscriber, props);
    }

    public void stop(BundleContext context) throws Exception {
    }

}
