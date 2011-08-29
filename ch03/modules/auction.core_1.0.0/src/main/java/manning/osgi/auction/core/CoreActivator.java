package manning.osgi.auction.core;

import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.*;

import manning.osgi.auction.Auction;
import manning.osgi.auction.spi.Auctioneer;
import manning.osgi.auction.spi.Auditor;

public class CoreActivator implements BundleActivator, ServiceListener {

    private BundleContext bundleContext;
    private Map<ServiceReference, ServiceRegistration> registeredAuctions = 
        new HashMap<ServiceReference, ServiceRegistration>();
    private Map<ServiceReference, Auditor> registeredAuditors =
        new HashMap<ServiceReference, Auditor>();

    public void start(BundleContext bundleContext) throws Exception {
        this.bundleContext = bundleContext;
        
        String auctionOrAuctioneerFilter = 
            "(|" +
                "(objectClass=" + Auctioneer.class.getName() + ")" +
                "(objectClass=" + Auditor.class.getName() + ")" +
            ")";
        
        ServiceReference [] references =
            bundleContext.getServiceReferences(null, auctionOrAuctioneerFilter);
        if (references != null) {
            for (ServiceReference serviceReference : references) {
                registerService(serviceReference);
            }
        }
                
        bundleContext.addServiceListener(this, 
                auctionOrAuctioneerFilter);
    }

    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        ServiceReference serviceReference = serviceEvent.getServiceReference();
        
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                registerService(serviceReference);
                break;
            }
            case ServiceEvent.UNREGISTERING: {
                String [] serviceInterfaces = 
                    (String[]) serviceReference.getProperty("objectClass");
                if (Auctioneer.class.getName().equals(serviceInterfaces[0])) {
                    unregisterAuctioneer(serviceReference);
                } else {
                    unregisterAuditor(serviceReference);
                }
                bundleContext.ungetService(serviceReference); 
                break;
            }
        }
    }

    private void registerService(ServiceReference serviceReference) {
        Object serviceObject =
            bundleContext.getService(serviceReference);
        
        if (serviceObject instanceof Auctioneer) {
            registerAuctioneer(serviceReference, (Auctioneer) serviceObject);
        } else {
            registerAuditor(serviceReference, (Auditor) serviceObject);
        }
    }

    private void registerAuditor(ServiceReference auditorServiceReference, Auditor auditor) {
        registeredAuditors.put(auditorServiceReference, auditor);
    }

    private void registerAuctioneer(ServiceReference auctioneerServiceReference, 
            Auctioneer auctioneer) {
        Auction auction =
            new AuctionWrapper(auctioneer, registeredAuditors.values());
        
        ServiceRegistration auctionServiceRegistration = 
            bundleContext.registerService(Auction.class.getName(), 
                auction, auctioneer.getAuctionProperties());
        
        registeredAuctions.put(auctioneerServiceReference, auctionServiceRegistration);
    }
    
    private void unregisterAuditor(ServiceReference serviceReference) {
        registeredAuditors.remove(serviceReference);
    }
    
    private void unregisterAuctioneer(ServiceReference auctioneerServiceReference) {
        ServiceRegistration auctionServiceRegistration = 
            registeredAuctions.remove(auctioneerServiceReference);
        
        if (auctionServiceRegistration != null) {
            auctionServiceRegistration.unregister();
        }
    }
}
