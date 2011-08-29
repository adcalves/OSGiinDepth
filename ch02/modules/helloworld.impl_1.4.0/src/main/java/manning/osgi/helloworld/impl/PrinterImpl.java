package manning.osgi.helloworld.impl;

import java.text.DateFormat;
import java.util.Date;

import manning.osgi.helloworld.Printer;


public class PrinterImpl implements Printer {
    
    private static final String SEPARATOR = ": ";

    public void print(String message) {
        System.out.println(getCurrentTime() + SEPARATOR + message);
    }

    private String getCurrentTime() {
        return DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
    }  
}
