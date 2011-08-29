package manning.osgi.auction.auctioneer.sealed;

import junit.framework.TestCase;

import org.osgi.framework.ServiceReference;
import org.springframework.osgi.mock.MockBundleContext;

import manning.osgi.auction.Auction;
import manning.osgi.auction.InvalidOfferException;
import manning.osgi.auction.Participant;
import manning.osgi.auction.spi.Auctioneer;

public class TestSealedFirstPriceAuction extends TestCase {
    
    static class MockParticipant implements Participant {
        
        int numberOfAcceptances = 0;
        
        public String getName() {
            return "participant";
        }

        public void onAccepted(Auction auction, String item, Float price) {
            numberOfAcceptances+=1;
        }

        public void onRejected(Auction auction, String item, Float bestBid) {
            fail("Rejected");
        }
    }
    
    public void testSingleBidAsk() throws InvalidOfferException {
        
        SealedFirstPriceAuction auction = new SealedFirstPriceAuction(1);
        
        MockParticipant participant = new MockParticipant();
        
        auction.ask("book", new Float(50.0), participant);
        auction.bid("book", new Float(50.0), participant);
        
        assertEquals(2, participant.numberOfAcceptances);
    }
    
    public void testAuctionnerActivator() throws Exception {
        
        MockBundleContext mockContext = new 
            MockBundleContext();
        
        SealedFirstPriceAuctioneerActivator activator = 
            new SealedFirstPriceAuctioneerActivator();
        
        activator.start(mockContext);

        ServiceReference reference = 
            mockContext.getServiceReference(Auctioneer.class.getName());
        
        assertNotNull(reference);
    }

}
