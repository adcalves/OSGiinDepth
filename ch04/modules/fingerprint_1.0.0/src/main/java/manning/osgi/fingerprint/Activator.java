package manning.osgi.fingerprint;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

public class Activator 
    implements BundleActivator {

    private Object MD5FingerprintServiceImpl;
    private ServiceRegistration registration;
    private Object MD4FingerprintServiceImpl;
    private Object machineImpl;

    public void start(BundleContext bundleContext) throws Exception {
        
        BundleListener bundleListener = new MyBundleListener();
        
        bundleContext.addBundleListener(bundleListener);

        Dictionary<String, Object> md5Properties = 
            new Hashtable<String, Object>(); 

        md5Properties.put("PERFORMANCE", "SLOW");
        md5Properties.put("ALGORITHM", "MD5");
        md5Properties.put("SECURITY_LEVEL", "HIGH");
        md5Properties.put(Constants.SERVICE_RANKING, 10);
        
        registration = bundleContext.registerService(
                FingerprintService.class.getName(),
                MD5FingerprintServiceImpl,
                md5Properties 
        );
        
        Dictionary<String, Object> md4Properties = 
            new Hashtable<String, Object>();
        
        md4Properties.put("PERFORMANCE", "FAST");
        md4Properties.put("ALGORITHM", "MD4");
        md4Properties.put("SECURITY_LEVEL", "MEDIUM");
        md4Properties.put(Constants.SERVICE_RANKING, 5);
        
        registration = bundleContext.registerService(
                FingerprintService.class.getName(),
                MD4FingerprintServiceImpl,
                md4Properties 
        );
        
        Dictionary<String, Object> properties = 
            new Hashtable<String, Object>();
        
        properties.put(Constants.SERVICE_PID, "10.0.0.1");
        
        registration = bundleContext.registerService(
                NetworkMachine.class.getName(),
                machineImpl,
                properties 
        );
        
        registration = bundleContext.registerService(
                TelnetService.class.getName(),
                new TelnetServiceFactory(), null);
        
//        String filter = 
//            "(&(objectClass=" + FingerprintService.class.getName() + ")" +
//            "(PERFORMANCE=FAST))";
//        
        String filter = 
            "(" + Constants.SERVICE_PID + "=10.0.0.1)";
        
        ServiceReference[] serviceReferences = 
            bundleContext.getServiceReferences(NetworkMachineService.class.getName(), filter);
        
        NetworkMachineService service = 
            (NetworkMachineService) bundleContext.getService(serviceReferences[0]);
        
        ServiceReference serviceReference = 
            bundleContext.getServiceReference(TelnetService.class.getName());
        
        TelnetService telnetService = 
            (TelnetService) bundleContext.getService(serviceReference);
        
        assert telnetService != null;
    }

    public void stop(BundleContext arg0) throws Exception {
    }

}