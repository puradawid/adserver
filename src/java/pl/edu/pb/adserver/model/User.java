package pl.edu.pb.adserver.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Class represents user in database.
 */
@Entity(name = "users")
public class User implements Serializable {
    
    public enum UserType
    {
        ADM, //admin
        CLI, //client
        PAR  // partner
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected int id;
    protected String first_name;
    protected String second_name;
    protected String email;
    protected String telephone;
    protected String password; //it might be not used
    protected UserType type;
    
    
    public User (int id, String email, String password, UserType type, String first_name,
            String second_name, String telephone)
    {
        //creates user
        this.id = id;
        this.email = email;
        this.type = type;
        this.first_name = first_name;
        this.password = password;
        this.second_name = second_name;
        this.telephone = telephone;
    }
    
    public User (String email, String password, UserType type, String first_name,
            String second_name, String telephone)
    {
        //creates user
        this.email = email;
        this.type = type;
        this.first_name = first_name;
        this.password = password;
        this.second_name = second_name;
        this.telephone = telephone;
    }
    
    public User() {}
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public void setSecond_name(String second_name) {
        this.second_name = second_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getCredencials()
    {
        return type.toString();
    }
    public void setCredencials(String type)
    {
        this.type = UserType.valueOf(type);
    }
}
