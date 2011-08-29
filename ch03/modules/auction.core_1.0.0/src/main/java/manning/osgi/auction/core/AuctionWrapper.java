package manning.osgi.auction.core;

import java.util.*;

import manning.osgi.auction.*;
import manning.osgi.auction.spi.*;

public class AuctionWrapper implements Auction {
    
    private Collection<Auditor> auditors;
    private Auctioneer delegate;
    private Map<String,List<Float>> bidsPerItem = 
        new HashMap<String,List<Float>>();
    private Float ask;
    
    class ParticipantWrapper implements Participant {

        private Participant delegate;

        public ParticipantWrapper(Participant delegate) {
            this.delegate = delegate;
        }
        
        public String getName() {
            return delegate.getName();
        }

        public void onAccepted(Auction auction, String item, Float price) {
            delegate.onAccepted(auction, item, price);
            
            Float [] bids = bidsPerItem.get(item).toArray(new Float [0]); 
            for (Auditor auditor : auditors) {
                auditor.onAccepted(AuctionWrapper.this.delegate, delegate, 
                        item, ask, price, bids);
            }
        }

        public void onRejected(Auction auction, String item, 
                Float bestBid) {
            delegate.onRejected(auction, item, bestBid);

            Float [] bids = bidsPerItem.get(item).toArray(new Float [0]);
            for (Auditor auditor : auditors) {
                auditor.onRejected(AuctionWrapper.this.delegate, delegate, item, 
                        ask, bids);
            }
        }
    }

    public AuctionWrapper(Auctioneer delegate, Collection<Auditor> auditors) {
        this.delegate = delegate;
        this.auditors = auditors;
    }

    public Float ask(String item, Float price, Participant seller)
            throws InvalidOfferException {
        ask = price;
        return delegate.getAuction().ask(item, price, 
                new ParticipantWrapper(seller));
    }

    public Float bid(String item, Float price, Participant buyer)
            throws InvalidOfferException {
        List<Float> bids = bidsPerItem.get(item);
        if (bids == null) {
            bids = new LinkedList<Float>();
            bidsPerItem.put(item, bids);
        }
        bids.add(price);

        return delegate.getAuction().bid(item, price, 
                new ParticipantWrapper(buyer));
    }
}
