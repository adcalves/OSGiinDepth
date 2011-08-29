import java.util.List;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleCapability;
import org.osgi.framework.wiring.BundleRequirement;
import org.osgi.framework.wiring.BundleRevision;
import org.osgi.framework.wiring.BundleWire;


public class WiringBundleActivator implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        
        Bundle bundle =
            bundleContext.getBundle();

        // Of the provider bundle
        BundleRevision revision =
            bundle.adapt(BundleRevision.class);

        List<BundleWire> providedWires =
            revision.getWiring().getProvidedWires(BundleRevision.PACKAGE_NAMESPACE);

        for (BundleWire bundleWire : providedWires) {
            Bundle requirerBundle =
                bundleWire.getRequirerWiring().getBundle();

            System.out.println("Requirer's bundle symbolic name = " 
                    + requirerBundle.getSymbolicName());
        }

        List<BundleRequirement> requirements =
            revision.getDeclaredRequirements(BundleRevision.PACKAGE_NAMESPACE);

        for (BundleRequirement requirement : requirements) {
            Map<String, Object> attributes = requirement.getAttributes();
            System.out.println("Package name = " + attributes.get("osgi.wiring.package"));
            System.out.println("Package version = " + attributes.get("version"));
        }
        
        List<BundleCapability> capabilities =
            revision.getDeclaredCapabilities(BundleRevision.PACKAGE_NAMESPACE);

        for (BundleCapability capability : capabilities) {
            Map<String, Object> attributes = capability.getAttributes();
            System.out.println("Package name = " + attributes.get("osgi.wiring.package"));
            System.out.println("Package version = " + attributes.get("version"));
        }
    }

    public void stop(BundleContext context) throws Exception {
    }

}
