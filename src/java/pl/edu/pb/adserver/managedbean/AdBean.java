
package pl.edu.pb.adserver.managedbean;

import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.persistence.Persistence;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.CategoryJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;

@ManagedBean
@SessionScoped
public class AdBean {
    
    protected Ad current;
    protected AdJpaController ajc;
    protected UserJpaController ujc;
    protected CategoryJpaController cjc;
    protected int start, end, selectedItemIndex;
    
    
    protected DataModel dataModel;
    
    public AdBean() {
        ajc = new AdJpaController(
                Persistence.createEntityManagerFactory("AdServerPU")
        );
        ujc = new UserJpaController(
                Persistence.createEntityManagerFactory("AdServerPU")
        );
        cjc = new CategoryJpaController(
                Persistence.createEntityManagerFactory("AdServerPU")
        );
    }
    
    protected AuthBean getAuthBean()
    {
        FacesContext context = FacesContext.getCurrentInstance();
        AuthBean bean = (AuthBean) context.getApplication().evaluateExpressionGet(context, "#{authBean}", AuthBean.class);
        return bean;
    }
    
    public DataModel getDataModel()
    {
        AuthBean bean = getAuthBean();
        String credencials = bean.getLogged().getCredencials();
        String username = bean.getLogged().getEmail();
        if(dataModel == null)
        {
            if(credencials.equals("ADM"))
                dataModel = new ListDataModel(ajc.findAdEntities());
            if(credencials.equals("CLI"))
                dataModel = new ListDataModel(ajc.getUserAds(bean.getLogged()));
        }
        return dataModel;
    }
    
    public void recreateModel()
    {
        dataModel = null;
        dataModel = getDataModel();
    }
    
    public Ad getCurrent()
    {
        if (current == null) current = new Ad();
        return current;
    }
    
    public String prepareEdit()
    {
        current = (Ad)dataModel.getRowData();
        return "ad_edit";
    }
    
    public String prepareDelete()
    {
        current = (Ad)dataModel.getRowData();
        return "ad_delete";
    }
    
    public String prepareView()
    {
        current = (Ad)dataModel.getRowData();
        return "ad_view";
    }
    
    public String prepareCreate()
    {
        current = new Ad();
        current.setUser(getAuthBean().getLogged());
        return "ad_create";
    }
    
    public String create()
    {
        try {
            User u = ujc.findUser(current.getUser().getEmail());
            current.setUser(u);
            Category c = cjc.findCategory(current.getCategory().getName());
            current.setCategory(c);
            ajc.create(current);
        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).severe(e.getMessage());
        }
        current = null;
        recreateModel();
        return "ad_list";
    }
    public String delete()
    {
        try {
            ajc.destroy(current.getId());
        } catch (Exception e) {}
        current = null;
        recreateModel();
        return "ad_list";
    }
    public String update()
    {
        try {
            ajc.edit(current);
        } catch (Exception e) {}
        current = null;
        recreateModel();
        return "ad_list";
    }
    
    public String toList()
    {
        current = null;
        return "ad_list";
    }
}
