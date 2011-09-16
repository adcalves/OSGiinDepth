package manning.osgi.sab;

import java.util.List;

import org.osgi.service.blueprint.reflect.BeanMetadata;
import org.osgi.service.blueprint.reflect.Metadata;
import org.osgi.service.blueprint.reflect.Target;

@SuppressWarnings("rawtypes")
public class MutableBeanMetadata implements BeanMetadata {
    
    private List arguments;
    private String className;
    private String destroyMethod;
    private Target factoryComponent;
    private String factoryMethod;
    private String initMethod;
    private List properties;
    private String scope;
    private int activation;
    private List dependsOn;
    private String id;
    
    @SuppressWarnings("unchecked")
    public List getArguments() {
        return this.arguments;
    }
    
    public void setArguments(List arguments) {
        this.arguments = arguments;
    }
    
    public String getClassName() {
        return this.className;
    }
    
    public void setClassName(String className) {
        this.className = className;
    }
    
    public String getDestroyMethod() {
        return this.destroyMethod;
    }
    
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }
    
    public Target getFactoryComponent() {
        return this.factoryComponent;
    }
    
    public void setFactoryComponent(Target factoryComponent) {
        this.factoryComponent = factoryComponent;
    }
    
    public String getFactoryMethod() {
        return this.factoryMethod;
    }
    
    public void setFactoryMethod(String factoryMethod) {
        this.factoryMethod = factoryMethod;
    }
    
    public String getInitMethod() {
        return this.initMethod;
    }
    
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }
    
    @SuppressWarnings("unchecked")
    public List getProperties() {
        return this.properties;
    }
    
    public void setProperties(List properties) {
        this.properties = properties;
    }
    
    public String getScope() {
        return this.scope;
    }
    
    public void setScope(String scope) {
        this.scope = scope;
    }
    
    public void setActivation(int activation) {
        this.activation = activation;
    }
    
    @SuppressWarnings("unchecked")
    public List getDependsOn() {
        return this.dependsOn;
    }
    
    public void setDependsOn(List dependsOn) {
        this.dependsOn = dependsOn;
    }
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public int getActivation() {
        return activation;
    }
    
    public void addProperty(String propertyName, Metadata value) {
    }
}
