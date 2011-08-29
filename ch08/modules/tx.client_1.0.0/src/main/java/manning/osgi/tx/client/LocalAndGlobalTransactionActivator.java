package manning.osgi.tx.client;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;
import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;

public class LocalAndGlobalTransactionActivator implements BundleActivator{

    private int currentA = 100;
    private int amount = 50;
    private int currentB = 0;

    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference [] serviceReferences =
            bundleContext.getServiceReferences(
                    DataSourceFactory.class.toString(), 
                    "(" + DataSourceFactory.OSGI_JDBC_DRIVER_CLASS +
            "=org.apache.derby.jdbc.EmbeddedDriver)");
        
        DataSource ds = null;
        XADataSource ds1 = null;
        XADataSource ds2 = null;

        if (serviceReferences != null) {
            DataSourceFactory dsf = 
                (DataSourceFactory) bundleContext.getService(serviceReferences[0]);

            Properties props = new Properties();
            props.put(DataSourceFactory.JDBC_URL, "jdbc:derby:derbyDB;create=true");

            ds =
                dsf.createDataSource(props);
            
            ds1 =
                dsf.createXADataSource(props);
            
            ds2 =
                dsf.createXADataSource(props);
        }
        
        /* Listing 8.1 */
        nonTransactionalUpdate(ds);
        
        /* Listing 8.2 */
        undoWorkUpdate(ds);
        
        /* Listing 8.3, 8.4 */
        localTransactionalUpdate(ds);
        
        /* Listing 8.5 */
        globalTransactionalUpdate(bundleContext, ds1, ds2);
        
    }

    private void localTransactionalUpdate(DataSource ds) throws SQLException {
        Connection conn =
            ds.getConnection();

        conn.setAutoCommit(false);

        Statement stat =
            conn.createStatement();

        stat.execute("UPDATE account SET current = " + (currentA - amount) 
                + " WHERE accountId = 001");

        stat.execute("UPDATE account SET current = " + (currentB + amount) 
                + " WHERE accountId = 002");
        
        conn.commit();

        stat.close();
        conn.close();
    }
    
    private void globalTransactionalUpdate(BundleContext context, 
            XADataSource ds1, XADataSource ds2) throws SQLException, SystemException, 
            IllegalStateException, RollbackException, NotSupportedException, 
            SecurityException, HeuristicMixedException, HeuristicRollbackException {
        
        ServiceReference serviceReference = 
            context.getServiceReference("javax.transaction.TransactionManager");

        TransactionManager tm = 
            (TransactionManager) context.getService(serviceReference);

        XAConnection xaConn1 =
            ds1.getXAConnection();

        XAConnection xaConn2 =
            ds2.getXAConnection();

        tm.begin();

        Transaction transaction = tm.getTransaction();
        transaction.enlistResource(xaConn1.getXAResource());
        transaction.enlistResource(xaConn2.getXAResource());

        Connection conn1 = xaConn1.getConnection();
        Connection conn2 = xaConn2.getConnection();

        conn1.setAutoCommit(false);
        conn2.setAutoCommit(false);

        Statement stat1 =
            conn1.createStatement();

        Statement stat2 =
            conn2.createStatement();

        stat1.execute("UPDATE account SET current = " + (currentA - amount) 
                + " WHERE accountId = 001");

        stat2.execute("UPDATE account SET current = " + (currentB + amount) 
                + " WHERE accountId = 002"); 

        tm.commit();  
    }

    private void nonTransactionalUpdate(DataSource ds) throws SQLException {
        Connection conn =
            ds.getConnection();

        Statement stat =
            conn.createStatement();

        stat.execute("UPDATE account SET current = " + (currentA - amount) 
                + " WHERE accountId = 001");

        stat.execute("UPDATE account SET current = " + (currentB + amount) 
                + " WHERE accountId = 002");

        stat.close();
        conn.close();
    }
    
    private void undoWorkUpdate(DataSource ds) throws SQLException {
        Connection conn =
            ds.getConnection();

        Statement stat =
            conn.createStatement();

        stat.execute("UPDATE account SET current = " + (currentA - amount) 
                + " WHERE accountId = 001");

        try {
            stat.execute("UPDATE account SET current = " + (currentB + amount) 
              + " WHERE accountId = 002");
        } catch (Throwable e) {
            stat.execute("UPDATE account SET current = " + (currentA + amount) 
                    + " WHERE accountId = 001");
        }

        stat.close();
        conn.close();
    }
    
    public void stop(BundleContext context) throws Exception {
    }

}
