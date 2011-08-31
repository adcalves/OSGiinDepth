package manning.osgi.auction.buyer.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.*;

import manning.osgi.auction.*;

public class BidderServlet implements Servlet, Participant {
        
    private Auction auction;
    private String name;
    private PrintWriter writer;

    public BidderServlet(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }

    public void destroy() {
    }

    public ServletConfig getServletConfig() {
        return null;
    }

    public String getServletInfo() {
        return null;
    }

    public void init(ServletConfig config) throws ServletException {
    }

    public void service(ServletRequest req, ServletResponse resp)
    throws ServletException, IOException {

        String bidValue =
            req.getParameter("bid");
        
        String item =
            req.getParameter("item");

        try {
            if (bidValue == null || item == null) {
                throw new IllegalArgumentException("Invalid bid");
            } else {
                writer = resp.getWriter();
                Float price = new Float(bidValue);
                auction.bid(item, price, this);

                writer.println("Accepted bid of " 
                        + bidValue + " for item " + item);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    } 
    
    public void onAcceptance(Auction auction, String item, float price) {
        writer.println(this.name + " was awarded " + item + " for " + price);
    }

    public void onRejection(Auction auction, String item, float bestBid) {
        writer.println("Bid for " + item + " from " + name + " was rejected");
    }
}
