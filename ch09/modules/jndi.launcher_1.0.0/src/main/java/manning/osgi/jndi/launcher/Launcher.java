package manning.osgi.jndi.launcher;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.ServiceLoader;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;
import javax.sql.DataSource;


import org.osgi.framework.BundleException;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

public class Launcher {

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws BundleException, InterruptedException, NamingException {

        ServiceLoader<FrameworkFactory> services =
            ServiceLoader.load(FrameworkFactory.class);

        FrameworkFactory frameworkFactory = 
            services.iterator().next();

        Map properties = new HashMap();
        properties.put("org.osgi.framework.system.packages.extra", 
        "javax.sql");

        Framework osgiFrw =
            frameworkFactory.newFramework(properties);

        NamingManager.setInitialContextFactoryBuilder(
                new OSGiInitialContextFactoryImpl());

        osgiFrw.start();

        Hashtable env = new Hashtable();
        env.put("osgi.service.jndi.bundleContext", 
                osgiFrw.getBundleContext());

        InitialContext ic = new InitialContext(env);

        DataSource ds =
            (DataSource) ic.lookup("osgi:service/javax.sql.DataSource");

        ds.toString();

        osgiFrw.stop();
        osgiFrw.waitForStop(10000);
    }
}
