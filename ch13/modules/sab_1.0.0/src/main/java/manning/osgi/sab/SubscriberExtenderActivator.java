package manning.osgi.sab;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;

public class SubscriberExtenderActivator implements BundleActivator, BundleListener {

    public void start(BundleContext context) throws Exception {
        context.addBundleListener(this);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void bundleChanged(BundleEvent event) {
        try {
            if (event.getType() == BundleEvent.STARTED) {
                String subscriberClause = 
                    (String) event.getBundle().getHeaders().get("Meta-Subscriber");

                if (subscriberClause != null) {
                    List<List<String>> subsInfo =
                        parseSubscriberClause(subscriberClause);

                    for (List<String> subInfo : subsInfo) {
                        Class<?> handlerClass =
                            event.getBundle().loadClass(subInfo.get(0));

                        Object handler =
                            handlerClass.newInstance();
                        assert handler instanceof EventHandler;

                        Dictionary properties = new Hashtable();
                        properties.put(EventConstants.EVENT_TOPIC,
                                subInfo.get(1));

                        event.getBundle().getBundleContext().registerService(
                                EventHandler.class.getName(), handler, properties);
                    }
                }
            } 
        } catch (Exception e) {
            // Log
        }
    }

    public void stop(BundleContext context) throws Exception {
    }

    List<List<String>> parseSubscriberClause(String clause) {
        List<List<String>> subs = new ArrayList<List<String>>();

        StringTokenizer tokenizer = new StringTokenizer(clause, ",");
        while (tokenizer.hasMoreTokens()) {
            String sub = tokenizer.nextToken();

            int index = sub.indexOf(";");
            assert index != -1;

            String className = sub.substring(0, index);
            String topic = sub.substring(index + 1);

            List<String> subInfo = new ArrayList<String>();
            subInfo.add(className);
            subInfo.add(topic);

            subs.add(subInfo);
        }

        return subs;
    }

}
