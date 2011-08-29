package manning.osgi.jndi.launcher;

import java.util.Hashtable;

import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import javax.naming.spi.InitialContextFactoryBuilder;

public class OSGiInitialContextFactoryImpl implements
        InitialContextFactoryBuilder {

    public InitialContextFactory createInitialContextFactory(
            Hashtable<?, ?> env) throws NamingException {

        return null; // Mock implementation
    }

}
