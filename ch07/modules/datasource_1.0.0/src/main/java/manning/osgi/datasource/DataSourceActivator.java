package manning.osgi.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

import org.osgi.service.jdbc.DataSourceFactory;

public class DataSourceActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        
        // Listing 7.3
        persistUsingDataSource(context);
       
    }

    private void persistUsingDataSource(BundleContext bundleContext) throws SQLException, InvalidSyntaxException {
        ServiceReference [] serviceReferences =
            bundleContext.getServiceReferences(
                    DataSourceFactory.class.toString(), 
                    "(" + DataSourceFactory.OSGI_JDBC_DRIVER_CLASS +
            "=org.apache.derby.jdbc.EmbeddedDriver)");

        if (serviceReferences != null) {
            DataSourceFactory dsf = 
                (DataSourceFactory) bundleContext.getService(serviceReferences[0]);

            Properties props = new Properties();
            props.put(DataSourceFactory.JDBC_URL, "jdbc:derby:derbyDB;create=true");

            DataSource ds =
                dsf.createDataSource(props);

            Connection conn =
                ds.getConnection();

            Statement stat =
                conn.createStatement();
            
            String userId = "alex";
            String timestamp = "...";

            stat.execute("INSERT INTO event-log VALUES (" +
                    userId + ", " + timestamp + ")");

            stat.close();
            conn.close();
        }
    }

    public void stop(BundleContext context) throws Exception {
    }
}
