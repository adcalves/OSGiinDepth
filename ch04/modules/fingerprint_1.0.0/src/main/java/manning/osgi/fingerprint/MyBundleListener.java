package manning.osgi.fingerprint;

import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class MyBundleListener implements BundleListener {

    public void bundleChanged(BundleEvent bundleEvent) {
        if (bundleEvent.getType() == BundleEvent.INSTALLED) {
            // ...
        }
    }
}
