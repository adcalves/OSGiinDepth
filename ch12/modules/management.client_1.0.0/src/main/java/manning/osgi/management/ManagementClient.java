package manning.osgi.management;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.JMX;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.TabularData;
import javax.management.openmbean.TabularDataSupport;
import javax.management.relation.MBeanServerNotificationFilter;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.osgi.jmx.JmxConstants;
import org.osgi.jmx.framework.BundleStateMBean;
import org.osgi.jmx.framework.FrameworkMBean;
import org.osgi.jmx.framework.PackageStateMBean;
import org.osgi.jmx.framework.ServiceStateMBean;
import org.osgi.jmx.service.cm.ConfigurationAdminMBean;

public class ManagementClient {
    
    private long myAppId;
    private long[] serviceIds;

    public void start() throws Exception {

        JMXServiceURL url =
            new JMXServiceURL("service:jmx:rmi:///jndi/rmi://:9999/jmxrmi");
        JMXConnector connector = JMXConnectorFactory.connect(url, null);
        MBeanServerConnection msc = connector.getMBeanServerConnection();

        BundleStateMBean bundleStateMBean = manageBundles(msc);

        String[] packages = manageServices(msc, bundleStateMBean);

        managePackages(msc, bundleStateMBean, packages);

        manageFramework(msc);
        
        manageConfiguration(msc);
    }

    private void manageFramework(MBeanServerConnection msc)
            throws MalformedObjectNameException, IOException {
        ObjectName mbeanName;
        mbeanName = new ObjectName("osgi.core:type=framework,version=1.5");

        FrameworkMBean frameworkMBean = 
            JMX.newMBeanProxy(msc, mbeanName, FrameworkMBean.class);

        String [] bundleLocations = 
        {"bundle/bundleA.jar", "bundle/bundleB.jar", "bundle/bundleC.jar"};

        CompositeData batchResult =
            frameworkMBean.installBundles(bundleLocations);

        Long [] installedBundleIDs =
            (Long[]) batchResult.get(FrameworkMBean.COMPLETED);

        for (int i = 0; i < installedBundleIDs.length; i++) {
            System.out.println("Bundle '" + bundleLocations[i] + 
                    "' was installed successfully and its ID is " + installedBundleIDs[i]);
        }

        if (((Boolean) batchResult.get(FrameworkMBean.SUCCESS)) == false) {
            String culpritBundleLocation =
                (String) batchResult.get(FrameworkMBean.BUNDLE_IN_ERROR);

            String reason =
                (String) batchResult.get(FrameworkMBean.ERROR);

            System.out.println("Bundle '" + culpritBundleLocation + 
                    "' failed to install because of: " + reason);

            String [] remainingBundleLocations =
                (String []) batchResult.get(FrameworkMBean.REMAINING);

            System.out.println("The remaining bundles still need to be installed: ");
            for (String remaingBundleLocation : remainingBundleLocations)
                System.out.println(remaingBundleLocation);
        }
    }

    private void managePackages(MBeanServerConnection msc,
            BundleStateMBean bundleStateMBean, String[] packages)
            throws MalformedObjectNameException, IOException {
        ObjectName mbeanName;
        mbeanName = new ObjectName("osgi.core:type=packageState,version=1.5");

        PackageStateMBean packageStateMBean = 
            JMX.newMBeanProxy(msc, mbeanName, PackageStateMBean.class);

        for (String osgiPackage: packages) {
            int sepIndex = osgiPackage.indexOf(";");
            String pkg = osgiPackage.substring(0, sepIndex);
            String ver = osgiPackage.substring(sepIndex + 1);

            long [] bundleIds =
                packageStateMBean.getImportingBundles(pkg, ver, myAppId);

            System.out.println("The package '" + osgiPackage 
                    + "' is being used by bundles :");

            for (long bundleId : bundleIds) {
                System.out.println(
                        bundleStateMBean.getSymbolicName(bundleId));
            }
        }
    }

    private String[] manageServices(MBeanServerConnection msc,
            BundleStateMBean bundleStateMBean)
            throws MalformedObjectNameException, IOException {
        ObjectName mbeanName;
        mbeanName = new ObjectName("osgi.core:type=serviceState,version=1.5");

        ServiceStateMBean serviceStateMBean =
            JMX.newMBeanProxy(msc, mbeanName, ServiceStateMBean.class);

        for (long serviceId : serviceIds) {
            long [] bundleIds = 
                serviceStateMBean.getUsingBundles(serviceId);

            System.out.println("The service '" + 
                    serviceStateMBean.getObjectClass(serviceId) 
                    + "' is being used by bundles :");

            for (long bundleId : bundleIds) {
                System.out.println(
                        bundleStateMBean.getSymbolicName(bundleId));
            }
        }

        String [] packages =
            bundleStateMBean.getExportedPackages(myAppId);
        return packages;
    }

    @SuppressWarnings({"unchecked","unused"})
    private BundleStateMBean manageBundles(MBeanServerConnection msc)
            throws MalformedObjectNameException, IOException, MBeanException,
            AttributeNotFoundException, InstanceNotFoundException,
            ReflectionException {
        ObjectName mbeanName = new ObjectName("osgi.core:type=bundleState,version=1.5");

        BundleStateMBean bundleStateMBean =
            JMX.newMBeanProxy(msc, mbeanName, BundleStateMBean.class);

        if (bundleStateMBean.getState(0).equals(BundleStateMBean.ACTIVE)) {
            System.out.println("OSGi Framework is Ready for Service");
        }
        
        String state =
            (String) msc.getAttribute(mbeanName, "state");

        TabularData bundlesTable =
            bundleStateMBean.listBundles();

        Collection<CompositeData> bundles =
            (Collection<CompositeData>) bundlesTable.values();

        for (CompositeData bundleInfo : bundles) {
            if (bundleInfo.get(BundleStateMBean.SYMBOLIC_NAME).
                    equals("manning.enterpriseosgi.myapp")) {
                System.out.println("Application state = " +
                        bundleInfo.get(BundleStateMBean.STATE));
            }
        }

        myAppId = -1;

        for (CompositeData bundleInfo : bundles) {
            if (bundleInfo.get(BundleStateMBean.SYMBOLIC_NAME).
                    equals("manning.enterpriseosgi.myapp")) {
                myAppId = 
                    (Long) bundleInfo.get(BundleStateMBean.IDENTIFIER);
            }
        }

        serviceIds = 
            bundleStateMBean.getRegisteredServices(myAppId);
        return bundleStateMBean;
    }

    public class BundleEventListener implements NotificationListener {

        public void handleNotification(Notification notification,
                Object handback) {

            CompositeData bundleEvent =
                (CompositeData) notification.getUserData();

            String bundleSymbolicName =
                (String) bundleEvent.get(BundleStateMBean.SYMBOLIC_NAME);

            Integer bundleState =
                (Integer) bundleEvent.get(BundleStateMBean.EVENT);

            if (bundleState == 1) // Installed 
                System.out.println("Bundle '" + bundleSymbolicName + "' has been installed!");

            if (bundleState == 16) // Uninstalled
                System.out.println("Bundle '" + bundleSymbolicName + "' has been un-installed!");
        }
    }

    public class ServiceEventListener implements NotificationListener {

        public void handleNotification(Notification notification,
                Object handback) {

            CompositeData serviceEvent =
                (CompositeData) notification.getUserData();

            String [] serviceInterfaces =
                (String []) serviceEvent.get(ServiceStateMBean.OBJECT_CLASS);

            Integer serviceState =
                (Integer) serviceEvent.get(ServiceStateMBean.EVENT);

            if (serviceState == 1) // Registered 
                System.out.println("Service '" + serviceInterfaces[0] + "' has been registered!");

            if (serviceState == 16) // Unregistering
                System.out.println("Service '" + serviceInterfaces[0] + "' is being unregistered!");
        }
    }

    public void notificationHandler(ObjectName mbeanName, MBeanServerConnection msc) throws Exception {

        mbeanName = new ObjectName("osgi.core:type=bundleState,version=1.5");

        BundleStateMBean bundleStateMBean =
            JMX.newMBeanProxy(msc, mbeanName, BundleStateMBean.class, true);


        MBeanServerNotificationFilter filter = new MBeanServerNotificationFilter();
        filter.enableObjectName(new ObjectName("osgi.core:type=bundleState,version=1.5"));

        ((NotificationEmitter) bundleStateMBean).addNotificationListener(new BundleEventListener(),
                null, null);

        mbeanName = new ObjectName("osgi.core:type=serviceState,version=1.5");

        ServiceStateMBean serviceStateMBean =
            JMX.newMBeanProxy(msc, mbeanName, ServiceStateMBean.class);

        ((NotificationEmitter) serviceStateMBean).addNotificationListener(new ServiceEventListener(),
                null, null);

    }

    private void manageConfiguration(MBeanServerConnection msc) throws Exception {

        ObjectName mbeanName = new ObjectName("osgi.compendium:service=cm,version=1.3");

        ConfigurationAdminMBean cmBean =
            JMX.newMBeanProxy(msc, mbeanName, ConfigurationAdminMBean.class);

        TabularData properties = new TabularDataSupport(JmxConstants.PROPERTIES_TYPE);

        Map<String, Object> propertyValue = new HashMap<String, Object>();
        propertyValue.put(JmxConstants.KEY, "port");
        propertyValue.put(JmxConstants.VALUE, 9000);
        propertyValue.put(JmxConstants.TYPE, JmxConstants.INTEGER);

        CompositeData property = new CompositeDataSupport(JmxConstants.PROPERTY_TYPE, propertyValue); 
        properties.put(property);

        cmBean.update("manning.osgi.notification.broker", properties);

        TabularData newProperties =
            cmBean.getProperties("manning.osgi.notification.broker");

        CompositeData portProperty =
            newProperties.get(new String[] {"port"});

        System.out.println("The value of the new port configuration is: " + 
                portProperty.get(JmxConstants.VALUE));
    }
}