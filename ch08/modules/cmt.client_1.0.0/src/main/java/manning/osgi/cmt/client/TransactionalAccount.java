package manning.osgi.cmt.client;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.XAConnection;
import javax.sql.XADataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import manning.osgi.cmt.Resource;
import manning.osgi.cmt.Transaction;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class TransactionalAccount implements BundleActivator {

    double currentA = 100.0;
    double currentB = 0.0;
    double amount = 50.0;

    private XADataSource ds1;
    private XADataSource ds2;

    private UserTransaction ut = null;

    public void start(BundleContext bundleContext) throws Exception {
        ServiceReference serviceReference =
            bundleContext.getServiceReference(
                    UserTransaction.class.getName());

        ut = 
            (UserTransaction) bundleContext.getService(serviceReference);
    }
    
    public void stop(BundleContext context) throws Exception {
    }

    @Resource
    public XAConnection getAccountToBeDebited() throws SQLException {
        return ds1.getXAConnection();
    }

    @Resource
    public XAConnection getAccountToBeCredited() throws SQLException {
        return ds2.getXAConnection();
    }

    @Transaction
    public void doWork() throws SQLException, IllegalStateException, 
        SecurityException, SystemException {
        Connection conn1 = getAccountToBeDebited().getConnection();
        Connection conn2 = getAccountToBeCredited().getConnection();

        conn1.setAutoCommit(false);
        conn2.setAutoCommit(false);

        Statement stat1 =
            conn1.createStatement();

        Statement stat2 =
            conn2.createStatement();

        stat2.execute("UPDATE account SET current = " + (currentB + amount) 
                + " WHERE accountId = 002");

        if (currentA - amount < 0.0)
            ut.rollback();

        stat1.execute("UPDATE account SET current = " + (currentA - amount) 
                + " WHERE accountId = 001");

    }
}
