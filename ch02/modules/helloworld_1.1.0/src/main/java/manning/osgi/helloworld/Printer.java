package manning.osgi.helloworld;

import java.io.PrintStream;

public class Printer {
    
    public void println(PrintStream stream, String message) {
        stream.println(message);
    }
}
