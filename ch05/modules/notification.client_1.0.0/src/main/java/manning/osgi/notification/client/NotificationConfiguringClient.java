package manning.osgi.notification.client;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

public class NotificationConfiguringClient implements BundleActivator {

    public void start(BundleContext bundleContext) throws Exception {
        
        ServiceReference serviceReference =
            bundleContext.getServiceReference(ConfigurationAdmin.class.getName());
        
        ConfigurationAdmin configAdmin =
            (ConfigurationAdmin) bundleContext.getService(serviceReference);
        

        /**
         * Managed Service
         */
        Configuration configuration =
            configAdmin.getConfiguration("manning.osgi.notification.broker", 
                    null);
        
        Dictionary<String, Object> configProperties = 
            new Hashtable<String, Object>();
        
        configProperties.put("port", 10);

        ServiceReference [] serviceReferences = 
            bundleContext.getServiceReferences(null, //MetaTypeProvider.class.getName(), 
                    "(" + Constants.SERVICE_PID + "=" 
                    + "manning.osgi.notification.broker" + ")");
        
        MetaTypeProvider metaTypeProvider = 
            (MetaTypeProvider) bundleContext.getService(serviceReferences[0]);
        
        ObjectClassDefinition ocd =
            metaTypeProvider.getObjectClassDefinition("manning.osgi.notification.broker", 
                    null);
        
        AttributeDefinition [] attrDefs = 
            ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
        
        for (AttributeDefinition attrDef : attrDefs) {
            Object configPropertyValue = configProperties.get(attrDef.getName());
            
            if (configPropertyValue != null) {
                String validationMessage = attrDef.validate(configPropertyValue.toString());
                
                if (!validationMessage.equals("")) {
                    throw new IllegalArgumentException(validationMessage);
                }
            }
        }

        configuration.update(configProperties);
        
//        Configuration configuration =
//            configAdmin.createFactoryConfiguration("frameworks.notification.broker", 
//                null);
//        
//        Dictionary<String, Object> configProperties = 
//            new Hashtable<String, Object>();
//
//        configProperties.put("port", 8080);
//
//        configuration.update(configProperties);
//        
//        System.out.println("Create 1st config");
//        
//        configuration =
//            configAdmin.createFactoryConfiguration("frameworks.notification.broker", 
//                null);
//        
//        configProperties = 
//            new Hashtable<String, Object>();
//
//        configProperties.put("port", 8081);
//
//        configuration.update(configProperties);
//        
//        System.out.println("Create 2nd config");
//        
//        configProperties.remove("port");
//
//        configuration.update(configProperties);
//        
//        System.out.println("Update 2nd config");
//        
//        System.out.println("Factory PID = " + configuration.getFactoryPid() 
//                + ", PID = " + configuration.getPid());
    }

    public void stop(BundleContext arg0) throws Exception {
    }

}
