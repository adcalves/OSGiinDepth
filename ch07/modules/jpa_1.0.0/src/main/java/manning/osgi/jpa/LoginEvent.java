package manning.osgi.jpa;

import javax.persistence.*;

@Entity
public class LoginEvent {
    
    @Id
    @GeneratedValue
    private int id;
    
    private String userid;
    private long timestamp;
    
    public int getId() {
        return this.id;
    }
    
    public String getUserid() {
        return this.userid;
    }
    
    public void setUserid(String userid) {
        this.userid = userid;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    
}
