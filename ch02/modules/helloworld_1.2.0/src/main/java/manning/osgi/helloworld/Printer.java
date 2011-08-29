package manning.osgi.helloworld;

import java.io.PrintStream;
import java.util.Date;

public class Printer {
    
    public void println(PrintStream stream, String message, Date dateOfMessage) {
        stream.println(dateOfMessage.toString() + ':' + message);
    }
}
