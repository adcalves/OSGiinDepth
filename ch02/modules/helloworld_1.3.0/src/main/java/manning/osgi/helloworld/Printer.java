package manning.osgi.helloworld;

import java.text.DateFormat;
import java.util.Date;

public class Printer {
    
    private static final String SEPARATOR = " : ";

    public void print(String message) {
        System.out.println(getCurrentTime() + SEPARATOR + message);
    }

    private String getCurrentTime() {
        return DateFormat.getTimeInstance(DateFormat.LONG).format(new Date());
    }

}
