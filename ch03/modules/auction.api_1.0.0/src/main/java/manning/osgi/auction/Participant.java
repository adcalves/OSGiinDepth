package manning.osgi.auction;

public interface Participant {
    
    String getName();
    
    void onAccepted(Auction auction, String item, Float price);

    void onRejected(Auction auction, String item, Float bestBid);

}
