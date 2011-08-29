package manning.osgi.helloworld.introspector;

import java.lang.reflect.Method;

public class PrinterIntrospectorMain {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        Class<?> printerClientClass = 
            Class.forName("manning.osgi.helloworld.client.PrinterClient");
        Object printerClientInstance = 
            printerClientClass.newInstance();
        Method printMethod = 
            printerClientClass.getMethod("printMyMessage");
        printMethod.invoke(printerClientInstance);
    }

}
