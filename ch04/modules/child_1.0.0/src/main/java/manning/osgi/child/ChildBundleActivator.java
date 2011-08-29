package manning.osgi.child;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import manning.osgi.child.Child;
import manning.osgi.parent.Parent;

public class ChildBundleActivator implements BundleActivator {

    public void start(BundleContext arg0) throws Exception {
        Parent parent = new Child();
    }

    public void stop(BundleContext arg0) throws Exception {
    }
}
