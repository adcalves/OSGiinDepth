package manning.osgi.auction.buyer.http;

import org.osgi.framework.*;

import manning.osgi.auction.Auction;

public class BuyerActivator implements BundleActivator, ServiceListener {

    private static final int BUYER_SERVER_PORT = 9090;
    
    private BundleContext bundleContext;
    private BidderServer server = new BidderServer("Buyer 1");

    public void start(BundleContext bundleContext) throws Exception {
        
        this.bundleContext = bundleContext;
        
        String filter = 
            "(&(objectClass=" + Auction.class.getName() 
            + ")(" + Auction.TYPE + "=Sealed-First-Price))";
        
        ServiceReference[] serviceReferences = 
            bundleContext.getServiceReferences(null, filter);
        
        if (serviceReferences != null) {
            start(serviceReferences[0]);
        } else {
            bundleContext.addServiceListener(this, filter);
        }
    }

    public void stop(BundleContext bundleContext) throws Exception {
        server.stop();
        bundleContext.removeServiceListener(this);
    }

    public void serviceChanged(ServiceEvent serviceEvent) {
        switch (serviceEvent.getType()) {
            case ServiceEvent.REGISTERED: {
                start(serviceEvent.getServiceReference());
                break;
            }
            case ServiceEvent.UNREGISTERING: {
                server.stop();
                break;
            }
        }
    }
    
    private void start(ServiceReference serviceReference) {
        Auction auction = (Auction)
            bundleContext.getService(serviceReference);

        if (auction != null) {
            server.setAuction(auction);
            server.start(BUYER_SERVER_PORT);
        }
    }
}
