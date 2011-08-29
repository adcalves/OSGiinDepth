package manning.osgi.storagearea;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.service.event.Event;

public class StorageAreaActivator implements BundleActivator {
    
    Event event = null;
    
    int counter = 0;

    public void start(BundleContext bundleContext) throws Exception {

        /// Listing 7.1
        storeData(bundleContext);
        
        // Listing 7.2
        Event retrievedEvent = retrieveData(bundleContext);
        
        System.out.println(retrievedEvent);
        
    }
    
    public void stop(BundleContext context) throws Exception {
        //
    }

    private void storeData(BundleContext bundleContext) throws IOException {
        File eventLog =
            bundleContext.getDataFile("event-" + (counter++) + ".dat");
        
        eventLog.createNewFile();
        
        BufferedWriter writer = 
            new BufferedWriter(new FileWriter(eventLog));
        
        writer.write((String) event.getProperty("userid"));
        writer.newLine();
        writer.write((String) event.getProperty("timestamp"));
        writer.newLine();
        
        writer.close();
    }

    @SuppressWarnings("rawtypes")
    private Event retrieveData(BundleContext context)
            throws FileNotFoundException, IOException {
        File eventLog =
            context.getDataFile("event-0.dat");
        
        BufferedReader reader = 
            new BufferedReader(new FileReader(eventLog));
        
        String userId = reader.readLine();
        String timestamp = reader.readLine();
        
        reader.close();
        
        Dictionary properties = new Properties();
        
        properties.put("userid", userId);
        properties.put("timestamp", timestamp);
        
        Event retrievedEvent = new Event("LoginEvent", properties);
        return retrievedEvent;
    }
  
}
