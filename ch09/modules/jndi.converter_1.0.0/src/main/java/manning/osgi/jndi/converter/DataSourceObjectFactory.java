package manning.osgi.jndi.converter;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.spi.ObjectFactory;
import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.log.LogService;

public class DataSourceObjectFactory implements BundleActivator, ObjectFactory {

    private BundleContext bundleContext;
    private LogService logService;

    public void start(BundleContext context) throws Exception {
        bundleContext = context;

        retrieveLogService();

        bundleContext.registerService(ObjectFactory.class.toString(), 
                this, null);
    }
    
    public void stop(BundleContext context) throws Exception {
    }

    public Object getObjectInstance(Object obj, Name name, Context nameCtx,
            Hashtable<?, ?> environment) throws Exception { 

        Object ret = null;

        if (nameCtx.getNameInNamespace().equals("comp/env/jdbc")) {
            if (obj instanceof DataSource) {
                DataSource jeeDS = (DataSource) obj;

                if (logService != null)
                    jeeDS.setLogWriter(createLogWriter());

                ret = jeeDS;
            }
        }

        return ret;
    }

    private void retrieveLogService() {
        ServiceReference logServiceReference =
            bundleContext.getServiceReference(LogService.class.toString());
        logService = (LogService) 
            bundleContext.getService(logServiceReference);
    }
    
    public PrintWriter createLogWriter() {
        return new PrintWriter(new LogServiceWrapper(logService), true);
    }
    
    private class LogServiceWrapper extends StringWriter {
        
        private LogService delegate;

        private LogServiceWrapper(LogService delegate) {
            this.delegate = delegate;
        }

        @Override
        public void close() throws IOException { 
            delegate.log(LogService.LOG_INFO, getBuffer().toString());
            super.close();
        }

        @Override
        public void flush() { 
            delegate.log(LogService.LOG_INFO, getBuffer().toString());
            super.flush();
        }
    }
}
