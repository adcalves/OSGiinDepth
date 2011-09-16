package manning.osgi.sab;

import java.net.URL;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.apache.aries.blueprint.NamespaceHandler;
import org.apache.aries.blueprint.ParserContext;
import org.osgi.service.blueprint.reflect.ComponentMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class TxNamespaceHandler implements NamespaceHandler {
    
    private UserTransaction ut;

    public void setUserTransaction(UserTransaction ut) {
        this.ut = ut;
    }

    public URL getSchemaLocation(String arg0) {
        return getClass().getResource("schemas/tx.xsd");
    }

    public ComponentMetadata decorate(Node node, ComponentMetadata component,
            ParserContext context) {
        if (node.getLocalName().equals("transaction")) {
            TxInterceptor txInterceptor = new TxInterceptor();
            txInterceptor.setUserTransaction(ut);
            txInterceptor.setMethod(((Element) node).getAttribute("method"));
            
            context.getComponentDefinitionRegistry().
                registerInterceptorWithComponent(component, txInterceptor);
        }
        
        return null;
    }

    public Metadata parse(Element element, ParserContext context) {
        return null;
    }
    
    @SuppressWarnings("rawtypes")
    public Set<Class> getManagedClasses() {
        return null;
    }
}
