package manning.osgi.auction;

import org.osgi.framework.ServiceReference;
import org.springframework.osgi.test.AbstractConfigurableBundleCreatorTests;
import org.springframework.osgi.test.platform.Platforms;

import manning.osgi.auction.spi.Auctioneer;

public class IntegrationTestAuction extends
        AbstractConfigurableBundleCreatorTests {
    
    public void testSealedFirstPriceAuction() {
        ServiceReference reference = 
            bundleContext.getServiceReference(Auctioneer.class.getName());
        
        Auction auction =
            (Auction) bundleContext.getService(reference);
        
        // Place bids, asks...
    }
    
    protected String getPlatformName() {
        return Platforms.FELIX;
    }
}
