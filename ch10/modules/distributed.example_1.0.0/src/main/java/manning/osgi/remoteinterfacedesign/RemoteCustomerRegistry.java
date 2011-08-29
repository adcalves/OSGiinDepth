package manning.osgi.remoteinterfacedesign;

public interface RemoteCustomerRegistry { /* remote interface */
    
    public Customer findCustomer(String name);
    
    public void updateCustomerAddress(String name, String address); 

}
