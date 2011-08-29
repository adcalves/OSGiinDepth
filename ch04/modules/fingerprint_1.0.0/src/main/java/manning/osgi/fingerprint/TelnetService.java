package manning.osgi.fingerprint;

import java.net.InetAddress;

public interface TelnetService {
    
    void open(InetAddress target, int port);
    
    void close();
    
    String send(String text);

}
