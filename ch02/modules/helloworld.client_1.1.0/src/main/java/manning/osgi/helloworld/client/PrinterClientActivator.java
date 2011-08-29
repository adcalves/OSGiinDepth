package manning.osgi.helloworld.client;

import manning.osgi.helloworld.Printer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;


public class PrinterClientActivator implements BundleActivator, ServiceListener {

    private RunnablePrinterClient runnablePrinterClient = new RunnablePrinterClient();
    private BundleContext bundleContext;
    private ServiceReference serviceReference;

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        
        serviceReference = 
            bundleContext.getServiceReference(Printer.class.getName());
        
        if (serviceReference != null) {
            Printer printer = (Printer)
                bundleContext.getService(serviceReference);
            
            if (printer != null) {
                runnablePrinterClient.setPrinterService(printer);
                runnablePrinterClient.start();
            }
        }
        
        bundleContext.addServiceListener(this, 
                "(objectClass=" + Printer.class.getName() + ")");
    }
    
    public void stop(BundleContext bundleContext) throws Exception {
        runnablePrinterClient.stop();
        if (serviceReference != null) {
            bundleContext.ungetService(serviceReference);
        }
        
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        switch (serviceEvent.getType()) {
            case ServiceEvent.UNREGISTERING: {
                runnablePrinterClient.stop();
                bundleContext.ungetService(serviceEvent.getServiceReference());
                break;
            }
            case ServiceEvent.REGISTERED: {
                Printer printer = (Printer) 
                    bundleContext.getService(serviceEvent.getServiceReference());

                if (printer != null) {
                    runnablePrinterClient.setPrinterService(printer);
                    runnablePrinterClient.start();
                }
                break;
            }
        }
    }
}
