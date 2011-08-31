package manning.osgi.mysystembundle;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class SystemBundleActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        System.out.println("My Application Framework is ready.");
    }

    public void stop(BundleContext context) throws Exception {
    }
}
