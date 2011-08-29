package manning.osgi.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class JPAActivator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        LoginEvent loginEvent = new LoginEvent();
        
        // Set login event...
        loginEvent.setUserid("alex");

        ServiceReference [] serviceReferences =
            context.getServiceReferences(
                    EntityManagerFactory.class.toString(), 
            "osgi.unit.name=LoginEvent");

        if (serviceReferences != null) {
            EntityManagerFactory emf = 
                (EntityManagerFactory) context.getService(serviceReferences[0]);

            EntityManager em = emf.createEntityManager();

            em.getTransaction().begin();

            em.persist(loginEvent);

            em.getTransaction().commit();

            em.close();
            emf.close();
        }
    }

    public void stop(BundleContext context) throws Exception {
    }

}
