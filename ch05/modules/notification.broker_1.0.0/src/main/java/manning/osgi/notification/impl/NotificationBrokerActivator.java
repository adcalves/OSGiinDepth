package manning.osgi.notification.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.ObjectClassDefinition;

import manning.osgi.notification.NotificationBroker;

public class NotificationBrokerActivator implements BundleActivator, 
    ManagedService, MetaTypeProvider {

    private ObjectClassDefinition configurationSchema;
    private ServiceRegistration registration;
    private BundleContext context;
    private NotificationBrokerImpl broker;
    private ServiceRegistration brokerRegistration;

    public void start(BundleContext bundleContext) throws Exception {
        this.context = bundleContext;
        
        System.out.println("starting notification broker");
        
        initializeSchema();
        
        Dictionary<String, Object> properties = 
            new Hashtable<String, Object>(); 

        properties.put(Constants.SERVICE_PID, 
            "manning.osgi.notification.broker");
        
        System.out.println("register 1");
        
        registration = bundleContext.registerService(
                new String [] {ManagedService.class.getName(),
                  MetaTypeProvider.class.getName()  
                },
                this,
                properties 
        ); 
        
//        System.out.println("register 2");
//        
//        registration = bundleContext.registerService(
//                new String [] {Object.class.getName()},
//                new Object(),
//                properties 
//        );
    } 

    private void initializeSchema() {
        configurationSchema = new ObjectClassDefinition() {
            
            AttributeDefinition [] requiredAttrs = 
                new AttributeDefinition [] {
                    new AttributeDefinition() {
                        
                        String [] defaultValues = 
                            new String[] {"8080"};

                        public int getCardinality() {
                            return 0;
                        }

                        public String[] getDefaultValue() {
                            return defaultValues;
                        }

                        public String getDescription() {
                            return "Port for remote publishers.";
                        }

                        public String getID() {
                            return "manning.osgi.notification.broker" + ".port";
                        }

                        public String getName() {
                            return "port";
                        }

                        public String[] getOptionLabels() {
                            return null;
                        }

                        public String[] getOptionValues() {
                            return null;
                        }

                        public int getType() {
                            return AttributeDefinition.INTEGER;
                        }

                        public String validate(String value) {
                            Integer portValue = null;
                            
                            try {
                                portValue = 
                                    Integer.valueOf(value);
                            } catch (NumberFormatException e) {
                                return "Not a valid integer.";
                            }
                            
                            if (portValue < 1000 || portValue > 10000) {
                                return "Port must be set in the range [1000, 10000].";
                            } else {
                                return ""; // valid;
                            }
                        }}
            };

            public AttributeDefinition[] getAttributeDefinitions(int filter) {
                return (filter == ObjectClassDefinition.ALL || 
                    filter == ObjectClassDefinition.REQUIRED) ? requiredAttrs : null;
            }

            public String getDescription() {
                return "Configuration for Notification Broker service";
            }

            public String getID() {
                return "manning.osgi.notification.broker";
            }

            public InputStream getIcon(int arg0) throws IOException {
                return null;
            }

            public String getName() {
                return getID();
            }};
    }

    @SuppressWarnings("unchecked")
    public void updated(Dictionary configuration) throws ConfigurationException {
        if (configuration != null) {
            Integer port = 
                (Integer) configuration.get("port");
            
            if (port != null) {
                stopBrokerService();
                
                updateBrokerService(port);
            }
            
        } else {
            stopBrokerService();
        }
    }
    
    private void stopBrokerService() {
        if (broker != null) {
            broker.shutdown();
        }
        
        if (brokerRegistration != null) {
            brokerRegistration.unregister();
        }
    }
    
    @SuppressWarnings("unchecked")
    private void updateBrokerService(int port) {
        broker = new NotificationBrokerImpl(port);
        
        Dictionary serviceProperties = 
            new Hashtable();
        serviceProperties.put("port", port);
        
        brokerRegistration = context.registerService(
                NotificationBroker.class.getName(),
                broker,
                serviceProperties
        );
    }
    

    public void stop(BundleContext arg0) throws Exception {
        if (registration != null) {
            registration.unregister();
        }
        
        if (brokerRegistration != null) {
            brokerRegistration.unregister();
        }
    }

    public String[] getLocales() {
        return null;
    }

    public ObjectClassDefinition getObjectClassDefinition(String pid,
            String locale) {
        
        if (configurationSchema.getID().equals(pid)) {
            return configurationSchema;
        } else {
            return null;
        }
    }
}
