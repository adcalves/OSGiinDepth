package manning.osgi.auction.spi;

import java.util.Dictionary;

import manning.osgi.auction.Auction;

public interface Auctioneer {
    
    Auction getAuction();
    
    Dictionary<String, Object> getAuctionProperties();

}
