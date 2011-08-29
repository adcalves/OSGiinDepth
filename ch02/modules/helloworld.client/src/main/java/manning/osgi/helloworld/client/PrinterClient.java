package manning.osgi.helloworld.client;

import manning.osgi.helloworld.Printer;

public class PrinterClient {
    
    public void printMyMessage() {
        new Printer().print("Hello World");
    }
}
