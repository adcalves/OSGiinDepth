package manning.osgi.jndi.client;

import java.io.File;
import java.util.Hashtable;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.spi.InitialContextFactory;
import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jndi.JNDIContextManager;

public class JndiActivator implements BundleActivator {

    private BundleContext bundleContext;

    public void start(BundleContext bundleContext) throws Exception {

        this.bundleContext = bundleContext;
        
        // Listing 9.1, 9.2, 9.3, 9.4
        jndiInJEE();
        
        registerContextFactory();
        
        // Listing 9.5
        jndiInOsgi();
        
    }

    public void stop(BundleContext context) throws Exception {

    }

    @SuppressWarnings("unchecked")
    void jndiInJEE() throws NamingException {
        @SuppressWarnings("rawtypes")
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
            "com.sun.jndi.fscontext.RefFSContextFactory");
        
        InitialContext context = new InitialContext(env);

        DataSource ds = 
            (DataSource) context.lookup("java:comp/env/jdbc/AccountDS");

        NamingEnumeration<Binding> bindings =
            context.listBindings("java:comp/env/jdbc");

        while (bindings.hasMore()) {
            Binding bd = (Binding) bindings.next();
            System.out.println("Name = " + bd.getName() + ", Object = " + bd.getObject());
        }

        context.bind("java:comp/env/jdbc/AccountDS", ds);
        
        // Directory search
        
        InitialDirContext dirContext = new InitialDirContext();

        SearchControls control = new SearchControls();

        ds = 
            (DataSource) dirContext.search("java:comp/env/jdbc/AccountDS", 
                    "(ver=1.1)", control);
        
        // Or...

        BasicAttributes attrs = new BasicAttributes();
        attrs.put(new BasicAttribute("ver", "1.1"));

        ds = 
            (DataSource) dirContext.search("java:comp/env/jdbc/AccountDS", attrs);
    }

    @SuppressWarnings("unchecked")
    void jndiInOsgi() throws NamingException {

        ServiceReference ref = 
            bundleContext.getServiceReference(JNDIContextManager.class.getName());

        JNDIContextManager ctxtMgr =
            (JNDIContextManager) bundleContext.getService(ref);

        @SuppressWarnings("rawtypes")
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
            "com.sun.jndi.fscontext.RefFSContextFactory");

        Context context =
            ctxtMgr.newInitialContext(env);

        DataSource ds = 
            (DataSource) context.lookup("java:comp/env/jdbc/AccountDS");

        NamingEnumeration<Binding> bindings =
            context.listBindings("java:comp/env/jdbc");

        while (bindings.hasMore()) {
            Binding bd = (Binding) bindings.next();
            System.out.println("Name = " + bd.getName() + ", Object = " + bd.getObject());
        }
        
    }

    private void registerContextFactory() {
        Object refFSContextFactoryImpl = null; // FIXME Provider!

        bundleContext.registerService(
                new String [] {InitialContextFactory.class.getName(), 
                        "com.sun.jndi.fscontext.RefFSContextFactory"}, 
                        refFSContextFactoryImpl, 
                        null);
    }

    @SuppressWarnings("unchecked")
    void referenceBinding() throws NamingException {
        @SuppressWarnings("rawtypes")
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, 
        "com.sun.jndi.fscontext.RefFSContextFactory");
        InitialContext manningContext = new InitialContext(env);

        manningContext.bind("Books", new Reference(File.class.getName(), 
                "com.manning.FSObjectFactory", null));
    }
}
