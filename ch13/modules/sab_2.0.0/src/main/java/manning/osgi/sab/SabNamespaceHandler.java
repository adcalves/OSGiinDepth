package manning.osgi.sab;

import java.net.URL;
import java.util.Set;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.osgi.service.blueprint.container.ComponentDefinitionException;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.RefMetadata;
import org.osgi.service.blueprint.reflect.ValueMetadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SabNamespaceHandler implements NamespaceHandler {
    
    public SabNamespaceHandler() {
    }

    public ComponentMetadata decorate(Node node, ComponentMetadata component,
            ParserContext context) {
        return null;
    }

    public URL getSchemaLocation(String schema) {
        return getClass().getResource("schemas/sab.xsd");
    }     

    public ComponentMetadata parse(Element element, ParserContext context) {
        if (element.getLocalName().equals("subscriber")) {
            final String id = element.getAttribute("id");
            final String topic = element.getAttribute("topic");
            final String ref = element.getAttribute("ref");

            MutableBeanMetadata factoryMetadata = 
                new MutableBeanMetadata();

            factoryMetadata.setScope("singleton");
            factoryMetadata.setClassName(SubscriberFactory.class.toString());
            factoryMetadata.setInitMethod("init");
            factoryMetadata.setDestroyMethod("destroy");

            factoryMetadata.addProperty("id", 
                    new ValueMetadata() {
                public String getType() {
                    return String.class.toString();
                }
                public String getStringValue() {
                    return id;
                }
            });

            factoryMetadata.addProperty("topic", 
                    new ValueMetadata() {
                public String getType() {
                    return String.class.toString();
                }
                public String getStringValue() {
                    return topic;
                }
            });

            factoryMetadata.addProperty("target",
                    new RefMetadata(){
                public String getComponentId() {
                    return ref;
                }});

            factoryMetadata.addProperty("blueprintBundleContext",
                    new RefMetadata(){
                public String getComponentId() {
                    return "blueprintBundleContext";
                }});

            return factoryMetadata;

        } else
            throw new ComponentDefinitionException("Illegal use of blueprint SAB namespace");
    }
   
    @SuppressWarnings("rawtypes")
    public Set<Class> getManagedClasses() {
        return null;
    }

}
