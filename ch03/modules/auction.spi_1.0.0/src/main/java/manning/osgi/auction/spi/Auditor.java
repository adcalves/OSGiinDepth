package manning.osgi.auction.spi;

import manning.osgi.auction.Participant;

public interface Auditor {
    
    void onAccepted(Auctioneer auctioneer, Participant participant, 
            String item, Float ask, 
            Float acceptedBid, Float [] bids);
    
    void onRejected(Auctioneer auctioneer, Participant participant, 
            String item, Float ask, 
            Float [] rejectedBids);

}
