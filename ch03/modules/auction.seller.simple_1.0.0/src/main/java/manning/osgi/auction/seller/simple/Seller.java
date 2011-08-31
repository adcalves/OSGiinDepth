package manning.osgi.auction.seller.simple;

import manning.osgi.auction.Auction;
import manning.osgi.auction.InvalidOfferException;
import manning.osgi.auction.Participant;

public class Seller implements Participant, Runnable {
    
    private final String name;
    private Auction auction;
    
    public Seller(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void ask(Auction auction) {
        this.auction = auction;
        new Thread(this).start();
    }
    
    public void run() {
        try {
            auction.ask("bicycle", 24.0f, this);
        } catch (InvalidOfferException e) {
            e.printStackTrace();
        }
        auction = null;
    }

    public void onAcceptance(Auction auction, String item, float price) {
        System.out.println(this.name + " sold " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        System.out.println("No bidders accepted asked price for " + item + ", best bid was " + bestBid);
    }
}
