package manning.osgi.cmt.provider;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import javax.sql.XAConnection;
import javax.transaction.TransactionManager;
import javax.transaction.xa.XAResource;


import manning.osgi.cmt.Resource;
import manning.osgi.cmt.Transaction;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

public class TransactionExtenderActivator implements BundleActivator, BundleListener {

    TransactionManager tm;

    public void start(BundleContext context) throws Exception {

        tm = getTransactionManagerService(context);

        context.addBundleListener(this);
    }

    public void bundleChanged(BundleEvent event) {
        if (event.getType() == BundleEvent.STARTED) {
            try {
                String transactionalClassName = 
                    (String) event.getBundle().getHeaders().get("Meta-Transaction");

                Class<?> transactionalClass =
                    event.getBundle().loadClass(transactionalClassName);

                Object transactionalObject =
                    transactionalClass.newInstance();

                List<XAResource> resourcesToEnlist =
                    new LinkedList<XAResource>();

                Method [] methods = 
                    transactionalClass.getMethods();

                Method doWorkMethod = null;

                for (Method method : methods) {
                    if (method.getAnnotation(Resource.class) != null) {
                        Object obj =
                            method.invoke(transactionalObject, new Object[]{});

                        if (obj instanceof XAConnection) {
                            resourcesToEnlist.add(((XAConnection) obj).getXAResource());
                        } else if (obj instanceof XAResource) {
                            resourcesToEnlist.add((XAResource) obj);
                        } else {
                            throw new IllegalStateException("Missing Resource annotation");
                        }
                    }

                    if (method.getAnnotation(Transaction.class) != null) {
                        doWorkMethod = method;
                    }
                }

                doTransactionalWork(transactionalObject, resourcesToEnlist,
                        doWorkMethod);
            } catch (Exception e) {
                // Log
            }
        }
    }

    private void doTransactionalWork(Object transactionalObject,
            List<XAResource> resourcesToEnlist, Method doWorkMethod)
    throws Exception {
        if (doWorkMethod != null) {
            tm.begin();
            try {

                javax.transaction.Transaction transaction = tm.getTransaction();

                for (XAResource resource : resourcesToEnlist) {
                    transaction.enlistResource(resource);
                }    

                doWorkMethod.invoke(transactionalObject, new Object[]{});

                tm.commit();
            } catch (Exception e) {
                tm.rollback();
                // re-throw, or consume...
            }
        } else {
            throw new IllegalStateException("Missing Transaction annotation");
        }
    }

    private TransactionManager getTransactionManagerService(
            BundleContext context) {
        return null;
    }

    public void stop(BundleContext context) throws Exception {
    }
}
