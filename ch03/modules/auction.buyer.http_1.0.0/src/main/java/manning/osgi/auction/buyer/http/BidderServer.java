package manning.osgi.auction.buyer.http;

import java.io.*;
import java.net.*;
import java.util.regex.*;

import manning.osgi.auction.Auction;
import manning.osgi.auction.Participant;

public class BidderServer implements Runnable, Participant {
    
    private BufferedReader is;
    private OutputStreamWriter os;
    private boolean stopped;
    private Auction auction;
    private String name;
    private ServerSocket serverSocket;
    private Socket connSocket;
    
    public BidderServer(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }

    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    
    public void onAccepted(Auction auction, String item, Float price) {
        System.out.println(this.name + " was awarded " + item + " for " + price);
    }

    public void onRejected(Auction auction, String item, Float bestBid) {
        System.out.println("Bid for " + item + " from " + name + " was rejected");
    }
    
    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            new Thread(this).start();
        } catch (BindException e) {
            System.out.println("Port " + port + " already in use, choose a different port.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void stop() {
        stopped = true;
    }
   
    public void run() {
        try {
            System.out.println("Buyer http ready to accept bids: http://localhost:" 
                    + serverSocket.getLocalPort() + "/bid=price");
            
            while (!stopped) {
                connSocket = serverSocket.accept();
                is = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
                os = new OutputStreamWriter(connSocket.getOutputStream(), "ISO-8859-1");

                String input = is.readLine();
                os.write("HTTP/1.1 204 No Response\015\012\015\012");
                os.flush();

                handleRequest(input);

                is.close();
                os.close();
                connSocket.close();
            }
            
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(String input) {
        try {
            Pattern pattern =
                Pattern.compile("bid=(\\w+)");
            Matcher matcher =
                pattern.matcher(input);
   
            if (matcher.find()) {
                String price = matcher.group(1);
                    assert auction != null;
                    auction.bid("bicycle", new Float(price), this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
