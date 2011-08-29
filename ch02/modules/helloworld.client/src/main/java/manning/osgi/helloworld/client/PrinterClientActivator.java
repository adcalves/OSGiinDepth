package manning.osgi.helloworld.client;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PrinterClientActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        new PrinterClient().printMyMessage();
    }

    public void stop(BundleContext context) throws Exception {
        // NOP
    }
}
