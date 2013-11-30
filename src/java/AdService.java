import java.util.LinkedList;
import java.util.List;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.User.UserType;
import pl.edu.pb.adserver.model.controller.AdJpaController;

public class AdService extends ParseableServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        //if there is a client then print list of his ads
        //if there is a admin, then print whole list of ads
        User logged = (User) request.getSession().getAttribute("user");
        if(logged == null) { response.setStatus(403); return; }
        
        List<Ad> ads = new LinkedList<Ad>();
        if(logged.getCredencials().equals(UserType.ADM.toString()))
        {
            AdJpaController ajc = new AdJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            try {
                ads = ajc.findAdEntities();
                request.setAttribute("ads", ads);
            } catch (Exception e)
            {}
            
        }
        try {
            request.getRequestDispatcher("/manage_ads.jsp")
                    .forward(request, response);
        } catch(Exception e)
        {}
        //if there is a partner, then print generator for ads
        
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse resposnse)
    {
            //create new ad if you are client
    }
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse resposnse)
    {
        //update ad if you are client
    }
}
