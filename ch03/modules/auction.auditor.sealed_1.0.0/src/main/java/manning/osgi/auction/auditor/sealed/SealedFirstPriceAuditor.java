package manning.osgi.auction.auditor.sealed;

import manning.osgi.auction.Auction;
import manning.osgi.auction.Participant;
import manning.osgi.auction.spi.Auctioneer;
import manning.osgi.auction.spi.Auditor;

public class SealedFirstPriceAuditor implements Auditor {
    
    public void onAccepted(Auctioneer auctioneer, Participant participant, 
            String item, Float ask,
            Float acceptedBid, Float[] bids) {
        verify(auctioneer, participant, bids);
    }

    public void onRejected(Auctioneer auctioneer, Participant participant, 
            String item, Float ask, Float[] rejectedBids) {
        verify(auctioneer, participant, rejectedBids);
    }

    private void verify(Auctioneer auctioneer, Participant participant,
            Float[] bids) {
        if ("Sealed-First-Price".equals(
                auctioneer.getAuctionProperties().get(Auction.TYPE))) {
            for (int i = 0; i < bids.length - 1; i++) {
                if ((bids[i + 1] - bids[i]) <= 1.0) {
                    System.out.println("Warning to '" + participant.getName() 
                            + "': bids (" + bids[i] + ", " 
                            + bids[i+1] + ") are too close together, possible disclosure may have happened");
                }
            }
        }
    }

}
