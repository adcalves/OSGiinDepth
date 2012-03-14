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
                    EntityManagerFactory.class.getName(), 
            "(osgi.unit.name=LoginEvent)");

        if (serviceReferences != null) {
            EntityManagerFactory emf = 
                (EntityManagerFactory) context.getService(serviceReferences[0]);

            EntityManager em = emf.createEntityManager();

            persistLoginEvent(em, loginEvent);
            
            loginEvent = retrieveLoginEvent(em, loginEvent.getId());

            em.close();
            emf.close();
        }
        
    }

    private void persistLoginEvent(EntityManager em, LoginEvent loginEvent) {
        em.getTransaction().begin();

        em.persist(loginEvent);

        em.getTransaction().commit();
    }
    
    private LoginEvent retrieveLoginEvent(EntityManager em, int id) {
        em.getTransaction().begin();

        LoginEvent loginEvent = em.find(LoginEvent.class, id);

        em.getTransaction().commit();
        
        return loginEvent;
    }

    public void stop(BundleContext context) throws Exception {
    }

}
