/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.pb.adserver.model;

import javax.persistence.Persistence;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;

/**
 * Class describes user acting
 * @author dawid
 */
public class UserActions {
    
    protected UserJpaController ujc;
    
    public UserActions()
    {
        ujc = new UserJpaController(
                Persistence.createEntityManagerFactory("AdServerPU"));
    }
    
    public User login(String username, String password)
    {
        try
        {
            User u = ujc.findUser(username);
            if(u.password.equals(password)) 
                return u; 
            else 
                return null;
        } catch (Exception e)
        {
            //report
            return null;
        }
    }
    
    public boolean register(User u)
    {
        try
        {
            ujc.create(u);
            return true;
        } 
        catch (Exception e)
        {
            return false;
        }
        finally
        {
            
        }
    }
    
}
