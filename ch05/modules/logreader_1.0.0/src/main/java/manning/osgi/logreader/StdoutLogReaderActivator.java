package manning.osgi.logreader;

import java.util.Enumeration;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.log.LogEntry;
import org.osgi.service.log.LogListener;
import org.osgi.service.log.LogReaderService;

public class StdoutLogReaderActivator implements BundleActivator, LogListener {

    public void stop(BundleContext arg0) throws Exception {
    }

    @SuppressWarnings("unchecked")
    public void start(BundleContext context) throws Exception {
        ServiceReference serviceReference =
            context.getServiceReference(LogReaderService.class.getName());
        
        LogReaderService logReader =
            (LogReaderService) context.getService(serviceReference);
        
        logReader.addLogListener(this);
        
        LogEntry entry = null;
        Enumeration<LogEntry> logs = logReader.getLog();
        
        while (logs.hasMoreElements()) {
            entry = logs.nextElement();
            
            System.out.println(entry.getBundle().getSymbolicName() + ": " 
                    + entry.getMessage());
        }
    }

    public void logged(LogEntry entry) {
        System.out.println("log entry = " + entry.getMessage());
        
        if (entry.getException() instanceof ConfigurationException) {
            ConfigurationException configExcep = 
                (ConfigurationException) entry.getException();

            System.out.println("config exception = " + configExcep.getMessage());
//            
//            
//            if (configExcep.getProperty().equals("port")) {
//                // ...
//            }
        }
    }

}
