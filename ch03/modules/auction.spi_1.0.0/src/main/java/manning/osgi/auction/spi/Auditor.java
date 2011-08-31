package manning.osgi.auction.spi;

import manning.osgi.auction.Participant;

public interface Auditor {
    
    void onAcceptance(Auctioneer auctioneer, Participant participant, 
            String item, float ask, 
            float acceptedBid, Float [] bids);
    
    void onRejection(Auctioneer auctioneer, Participant participant, 
            String item, float ask, 
            Float [] rejectedBids);

}
