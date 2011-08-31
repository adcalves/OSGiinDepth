package manning.osgi.updater;

import java.util.Calendar;
import java.util.Date;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class UpdateActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        Bundle [] bundles =
            context.getBundles();
        
        for (Bundle bundle : bundles) {
            if (bundle.getSymbolicName().startsWith("manning.osgi.mysystembundle") && 
                    bundle.getBundleId() != context.getBundle().getBundleId()) {
                
                Date lastModifiedDate = new Date(bundle.getLastModified());
                Calendar targetDate = Calendar.getInstance();
                targetDate.add(Calendar.MONTH, -1);
                
                if (lastModifiedDate.before(targetDate.getTime())) {
                    try {
                        bundle.update();
                    } catch (BundleException e) {
                        // log
                    }
                }
            }
        }
    }

    public void stop(BundleContext context) throws Exception {
    }
}
