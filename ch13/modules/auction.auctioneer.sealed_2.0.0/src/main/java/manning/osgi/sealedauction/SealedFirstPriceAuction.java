package manning.osgi.sealedauction;

import java.util.Dictionary;
import java.util.Hashtable;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import manning.osgi.auction.Auction;
import manning.osgi.auction.InvalidOfferException;
import manning.osgi.auction.Participant;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class SealedFirstPriceAuction implements Auction {

    private String pid;
    private int maxAllowedBids;
    private String type;

    private EventAdmin eventAdmin;
    private EntityManagerFactory emf;
    private EntityManager em;

    public void setPersistentId(String pid) {
        this.pid = pid;
    }

    public String getPersistentId() {
        return pid;
    }

    public EventAdmin getEventAdmin() {
        return this.eventAdmin;
    }

    public void setEventAdmin(EventAdmin eventAdmin) {
        this.eventAdmin = eventAdmin;
    }

    public EntityManagerFactory getEntityManagerFactory() {
        return this.emf;
    }

    public void setEntityManagerFactory(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public int getDuration() {
        return this.maxAllowedBids;
    }

    public void setDuration(int duration) {
        this.maxAllowedBids = duration;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void init() {
        em = emf.createEntityManager();
    }

    public Float ask(String item, float price, Participant seller)
    throws InvalidOfferException {
        if (price <= 0) {
            throw new InvalidOfferException("Ask must be greater than zero.");
        }

        AuctionBook book = new AuctionBook();
        book.setItem(item);
        book.setAsk(price);
        book.setSeller(seller.getName());

        try {
            em.persist(book);
        } catch (EntityExistsException e) {
            throw new InvalidOfferException("Item [" + item + "] already being auctioned.");
        }

        eventAdmin.postEvent(createAskEvent(item, price, seller.getName()));

        return price;
    }


    public Float bid(String item, float price, Participant buyer)
    throws InvalidOfferException {
        if (price <= 0) {
            throw new InvalidOfferException("Bid must be greater than zero.");
        }

        AuctionBook book = em.find(AuctionBook.class, item);

        if (book == null) {
            throw new InvalidOfferException("Item [" + item 
                    + "] is not being offered in this auction.");
        }

        if (price > book.getHighestBid()) {
            book.setHighestBid(price);
            book.setHighestBidder(buyer.getName());
            book.incNumberOfBids();
        }

        eventAdmin.postEvent(createBidEvent(item, price, buyer.getName()));

        if ((book.getNumberOfBids()) == maxAllowedBids) {
            if (book.getHighestBid() >= book.getAsk()) {
                eventAdmin.postEvent(
                        createAcceptedEvent(item, 
                                book.getSeller(),
                                book.getHighestBid(), 
                                book.getHighestBidder()));
            } else {
                eventAdmin.postEvent(createRejectedEvent(item, 
                        book.getSeller(), book.getHighestBid()));
            }

            em.remove(book);
        } else {
            em.refresh(book);
        }

        return null;
    }

    private Event createAskEvent(String item, Float price, String name) {
        Dictionary props = new Hashtable();
        props.put("AUCTION_ID", pid);
        props.put("ITEM", item);
        props.put("PRICE", price);
        props.put("SELLER", name);

        Event event = new Event("manning/auction/ASKS", props);
        return event;
    }

    private Event createBidEvent(String item, Float price, String name) {
        Dictionary props = new Hashtable();
        props.put("AUCTION_ID", pid);
        props.put("ITEM", item);
        props.put("PRICE", price);
        props.put("BUYER", name);

        Event event = new Event("manning/auction/BIDS", props);
        return event;
    }

    private Event createRejectedEvent(String item, String seller, float highestBid) {
        Dictionary props = new Hashtable();
        props.put("AUCTION_ID", pid);
        props.put("ITEM", item);
        props.put("HIGHEST_BID", highestBid);
        props.put("SELLER", seller);
        props.put("VERDICT", "rejected");

        Event event = new Event("manning/auction/" + item, props);
        return event;
    }

    private Event createAcceptedEvent(String item, String seller, float highestBid,
            String highestBidder) {
        Dictionary props = new Hashtable();
        props.put("AUCTION_ID", pid);
        props.put("ITEM", item);
        props.put("HIGHEST_BID", highestBid);
        props.put("SELLER", seller);
        props.put("BUYER", highestBidder);
        props.put("VERDICT", "accepted");

        Event event = new Event("manning/auction/" + item, props);
        return event;
    }
}
