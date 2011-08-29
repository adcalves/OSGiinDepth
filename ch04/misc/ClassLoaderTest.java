public class ClassLoaderTest {

    /**
     * @param args
     * @throws ClassNotFoundException 
     */
    public static void main(String[] args) throws ClassNotFoundException  {
        ClassLoader firstClassLoader = ClassLoaderTest.class.getClassLoader();
        ClassLoader secondClassLoader = null;
        
        Object instanceOfClassA = Class.forName("ClassA", true, firstClassLoader);
        Object anotherInstanceOfClassA = Class.forName("ClassA", true, secondClassLoader);
        
        assert !instanceOfClassA.getClass().equals(
                anotherInstanceOfClassA.getClass()); 
        
        
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
        } catch (ClassNotFoundException e) {
            // log
        }
    }

}
