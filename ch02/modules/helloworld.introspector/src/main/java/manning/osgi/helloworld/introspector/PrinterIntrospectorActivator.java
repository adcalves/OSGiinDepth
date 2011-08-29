package manning.osgi.helloworld.introspector;

import java.lang.reflect.Method;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PrinterIntrospectorActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        Class<?> printerClientClass = 
            Class.forName("manning.osgi.helloworld.client.PrinterClient");
        Object printerClientInstance = 
            printerClientClass.newInstance();
        Method printMethod = 
            printerClientClass.getMethod("printMyMessage");
        printMethod.invoke(printerClientInstance);
    }

    public void stop(BundleContext context) throws Exception {
        // NOP
    }
}
