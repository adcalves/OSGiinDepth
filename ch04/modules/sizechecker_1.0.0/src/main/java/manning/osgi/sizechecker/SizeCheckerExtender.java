package manning.osgi.sizechecker;

import static org.osgi.framework.BundleEvent.INSTALLED;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;

import org.osgi.framework.*;

public class SizeCheckerExtender implements BundleActivator, BundleListener {

    private static final long MAX_SIZE_OF_1MB = 1024*1024; 

    public void start(BundleContext bundleContext) throws Exception {
        bundleContext.addBundleListener(this);
        
        for (Bundle bundle : bundleContext.getBundles()) {
            if (bundle.getState() != Bundle.UNINSTALLED) {
                checkSize(bundle);
            }
        }
    }
    
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeBundleListener(this);
    }

    public void bundleChanged(BundleEvent bundleEvent) {
        if (bundleEvent.getType() == INSTALLED) {
            checkSize(bundleEvent.getBundle());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void checkSize(Bundle bundle) {
        Enumeration<URL> resources =
            bundle.findEntries("/", "*", true);

        if (resources != null) {
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                
                InputStream inputStream = null;
                
                try {
                    inputStream =
                        resource.openStream();

                    if (inputStream.skip(MAX_SIZE_OF_1MB) == MAX_SIZE_OF_1MB) {
                        System.out.println("Warning: resource " 
                                + resource.toExternalForm() + " has at least 1 MB in size.");
                    } 
                } catch (IOException e) {
                    // log...
                    e.printStackTrace();
                } finally {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // log...
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
