package manning.osgi.remoteinterfacedesign;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceException;
import org.osgi.framework.ServiceReference;

public class LocalClient implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        // TODO Auto-generated method stub
        
        ServiceReference servRef =
            bundleContext.getServiceReference(RemoteCustomerRegistry.class.toString());

        if (servRef != null) {
            RemoteCustomerRegistry registry =
                (RemoteCustomerRegistry) bundleContext.getService(servRef);

            try {
                registry.updateCustomerAddress("Alex", "Updated Address!");
            } catch (ServiceException e) {
                Thread.sleep(2000); // wait for two seconds and try again

                registry.updateCustomerAddress("Alex", "Updated Address!");
            }
        }

    }

    public void stop(BundleContext context) throws Exception {
    }

}
