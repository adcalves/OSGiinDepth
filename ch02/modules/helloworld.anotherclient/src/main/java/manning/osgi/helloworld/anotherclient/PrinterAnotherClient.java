package manning.osgi.helloworld.anotherclient;

import manning.osgi.helloworld.Printer;

public class PrinterAnotherClient {
    
    public void printAnotherMessage() {
        new Printer().println(System.out, "Another Hello World");
    }
}
