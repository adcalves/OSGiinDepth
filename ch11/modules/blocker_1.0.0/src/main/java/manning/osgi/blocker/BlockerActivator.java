package manning.osgi.blocker;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class BlockerActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
       System.out.println("Starting blocker");
        
       while (true);
    }

    public void stop(BundleContext context) throws Exception {
    }
}
