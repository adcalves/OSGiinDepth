package manning.osgi.helloworld.anotherclient;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PrinterAnotherClientActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        new PrinterAnotherClient().printAnotherMessage();
    }

    public void stop(BundleContext context) throws Exception {
        // NOP
    }
}
