package pl.edu.pb.adserver.managedbean;

import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.Persistence;

import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.UserJpaController;

@ManagedBean
@SessionScoped
public class UserBean {
    
    User current;
    DataModel users;
    int start, end, selectedItemIndex;
    
    UserJpaController ujc;
    
    public UserBean() {
        ujc = new UserJpaController(
                Persistence.createEntityManagerFactory("AdServerPU"));
    }
    
    public DataModel getDataModel()
    {
        //if(users == null)
            users = new ListDataModel(ujc.findUserEntities());
        return users;
    }
    
    public void recreateModel()
    {
        users = new ListDataModel(ujc.findUserEntities());
    }
    
    public User getCurrent()
    {
        if(current == null)
            current = new User();
        return current;
    }
    
    public String prepareEdit()
    {
        current = (User)users.getRowData();
        return "user_edit";
    }
    
    public String prepareView()
    {
        current = (User)users.getRowData();
        return "user_view";
    }
    
    public String prepareDelete()
    {
        current = (User)users.getRowData();
        return "user_delete";
    }
    
    public String prepareRegister()
    {
        current = new User();
        return "user_create";
    }
    
    public String update()
    {
        try {
        ujc.edit(current);
        } catch (Exception e)
        {
            Logger.getLogger("UserBean").warning(e.getMessage());
        }
        return "user_list";
    }
    
    public String delete()
    {
        try {
        ujc.destroy(current.getId());
        } catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
        }
        return "user_list";
    }
    
    public String create()
    {
        try {
        ujc.create(current);
        } catch (Exception e)
        {
            
        }
        return "user_list";
    }
    
    
}
