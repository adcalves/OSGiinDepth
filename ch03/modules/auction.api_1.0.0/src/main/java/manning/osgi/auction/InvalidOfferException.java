package manning.osgi.auction;

public class InvalidOfferException extends Exception {
    
    private static final long serialVersionUID = 6214049499685772520L;

    public InvalidOfferException(String message) {
        super(message);
    }
}
