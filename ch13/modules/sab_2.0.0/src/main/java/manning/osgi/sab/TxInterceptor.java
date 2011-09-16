package manning.osgi.sab;

import java.lang.reflect.Method;

import javax.transaction.UserTransaction;

import org.apache.aries.blueprint.Interceptor;
import org.osgi.service.blueprint.reflect.ComponentMetadata;

public class TxInterceptor implements Interceptor {
    
    private UserTransaction ut;
    private String targetMethod;

    public int getRank() {
        return 0;
    }
    
    public void setUserTransaction(UserTransaction ut) {
        this.ut = ut;
    }
    
    public void setMethod(String methodName) {
        targetMethod = methodName;
    }

    public Object preCall(ComponentMetadata metadata, Method m, Object... args)
    throws Throwable {
        if (m.getName().equals(targetMethod)) {
            try {
                ut.begin();
            } catch (Exception e) {
                // Handle TM exception...
            }
        }
        
        return null;
    }

    public void postCallWithException(ComponentMetadata metadata, Method m,
            Throwable t, Object correlator) throws Throwable {
        if (m.getName().equals(targetMethod)) {
            try {
                ut.rollback();
            } catch (Exception e) {
                // Handle TM exception...
            }
        }
    }

    public void postCallWithReturn(ComponentMetadata metadata, Method m,
            Object ret, Object correlator) throws Throwable {
        if (m.getName().equals(targetMethod)) {
            try {
                ut.commit();
            } catch (Exception e) {
                // Handle TM exception...
            }
        }
    }
}
