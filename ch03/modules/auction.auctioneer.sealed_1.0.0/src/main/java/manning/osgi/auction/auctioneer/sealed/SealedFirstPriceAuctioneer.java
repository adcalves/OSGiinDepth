package manning.osgi.auction.auctioneer.sealed;

import java.util.*;

import manning.osgi.auction.Auction;
import manning.osgi.auction.spi.Auctioneer;

public class SealedFirstPriceAuctioneer implements Auctioneer {
    
    private static final String SEALED_FIRST_PRICE = "Sealed-First-Price";
    private final int DURATION = 3;
    
    private final Dictionary<String, Object> properties = 
        new Hashtable<String, Object>();
    
    private final Auction auction;
    
    public SealedFirstPriceAuctioneer() {
        properties.put(Auction.TYPE, SEALED_FIRST_PRICE);
        properties.put(Auction.DURATION, DURATION);
        auction = new SealedFirstPriceAuction(DURATION);         
    }
    
    public Auction getAuction() {
        return auction;
    }

    public Dictionary<String, Object> getAuctionProperties() {
        return properties;
    }
}
