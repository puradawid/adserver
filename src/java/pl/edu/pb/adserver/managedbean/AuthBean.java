
package pl.edu.pb.adserver.managedbean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.Persistence;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.UserJpaController;

@ManagedBean
@SessionScoped
public class AuthBean {
    User user = null; //anonymous
    String username;
    String password;
    
    UserJpaController ujc;
    
    public AuthBean() {
        ujc = new UserJpaController(
            Persistence.createEntityManagerFactory("AdServerPU"));
    }
    
    public String login()
    {
       User u = null;
       try {
        u = ujc.findUser(username);
       } catch (Exception e)
       {
           return "login";
       }
       if( u != null && u.getPassword().equals(password))
       {
           user = u;
           FacesContext.getCurrentInstance()
                   .getExternalContext().getSessionMap().put("user", u);
           return "main";
       } else
       {
           user = null;
           return "login";
       }
    }
    
    public void setUsername(String username)
    {
        this.username = username;
    }
    
    public void setPassword(String password)
    {
        this.password = password;
    }
    
    public String getUsername()
    {
        return "login";
    }
    
    public String getPassword()
    {
        return "";
    }
    
    public User getLogged()
    {
        return user;
    }
    
    public String logout()
    {
        user = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "main";
    }
}
