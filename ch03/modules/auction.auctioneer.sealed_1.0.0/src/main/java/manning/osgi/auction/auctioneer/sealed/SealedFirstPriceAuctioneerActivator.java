package manning.osgi.auction.auctioneer.sealed;

import manning.osgi.auction.auctioneer.sealed.SealedFirstPriceAuctioneer;

import org.osgi.framework.*;

import manning.osgi.auction.spi.Auctioneer;

public class SealedFirstPriceAuctioneerActivator implements BundleActivator {

    private ServiceRegistration serviceRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        SealedFirstPriceAuctioneer auctioneer = 
            new SealedFirstPriceAuctioneer();
        
        serviceRegistration = 
            bundleContext.registerService(
                    Auctioneer.class.getName(), 
                    auctioneer, auctioneer.getAuctionProperties()); 
    }

    public void stop(BundleContext bundleContext) throws Exception {
        serviceRegistration.unregister();
    }
}
