package manning.osgi.sealedauction;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
class AuctionBook {
    
    @Id
    private String item;
    
    private float ask;
    private int numberOfBids;
    private float highestBid;

    private String seller;
    private String highestBidder;
    
    public void setItem(String item) {
        this.item = item;
    }
    
    public String getItem() {
        return item;
    }    
    
    public float getAsk() {
        return this.ask;
    }
    
    public void setAsk(float ask) {
        this.ask = ask;
    }
    
    public String getSeller() {
        return this.seller;
    }
    
    public void setSeller(String seller) {
        this.seller = seller;
    }
    
    public float getHighestBid() {
        return this.highestBid;
    }
    
    public void setHighestBid(float highestBid) {
        this.highestBid = highestBid;
    }
    
    public String getHighestBidder() {
        return this.highestBidder;
    }
    
    public void setHighestBidder(String highestBidder) {
        this.highestBidder = highestBidder;
    }
    
    public int getNumberOfBids() {
        return this.numberOfBids;
    }
    
    public void setNumberOfBids(int numberOfBids) {
        this.numberOfBids = numberOfBids;
    }

    public void incNumberOfBids() {
        this.numberOfBids++;
    }
}