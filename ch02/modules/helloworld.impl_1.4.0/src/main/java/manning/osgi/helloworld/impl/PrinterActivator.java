package manning.osgi.helloworld.impl;

import manning.osgi.helloworld.Printer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;


public class PrinterActivator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        serviceRegistration = 
            bundleContext.registerService(
                    Printer.class.getName(), 
                    new PrinterImpl(), 
                    null);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
    }

}
