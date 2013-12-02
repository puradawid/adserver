import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.Persistence;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Ad.ContentType;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.User.UserType;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;

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
            
        }if (logged.getCredencials().equals(UserType.ADM.toString()))
        {
           AdJpaController ajc = new AdJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            try {
                ads = ajc.getUserAds(logged);
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
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
            //create new ad if you are client
        try
        {
            User logged = (User) request.getSession().getAttribute("user");
            Map<String, String> params = buildParamMap(request);
            String content = params.get("content");
            String user = params.get("user");
            if(logged.getCredencials().equals(UserType.CLI.toString()))
                user = logged.getEmail(); //ad user overload
            String category = params.get("category");
            String contentType = params.get("content_type");
            UserJpaController ujc = new UserJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            User u = ujc.findUser(user);
            Ad ad = new Ad(0, content, ContentType.parse(contentType), u, category);
            
            AdJpaController ajc = new AdJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            ajc.create(ad);
            response.sendRedirect(
                    request.getServletContext().getContextPath() + "/ad");
        } catch (Exception e)
        {
            
        }
    }
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        //update ad if you are client
        String[] params = parseParams(request);
        Map<String, String> parameters = null;
        try { parameters = buildParamMap(request); }
        catch (IOException e) {}
        User logged = (User) request.getSession().getAttribute("user");
        if(logged == null || logged.getCredencials().equals("PAR"))
        {
            response.setStatus(403); return;
        }
        
        try
        {
            AdJpaController ajc = new AdJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            Ad a = ajc.findAd(Integer.parseInt(params[3]));
            
            String content = parameters.get("content");
            if(content != null) a.setContent(content);
            
            ajc.edit(a);
        } catch (Exception e)
        {
            response.setStatus(403); return;
        }
        
    }
    
    @Override
    public void doDelete(
            HttpServletRequest request, HttpServletResponse response)
    {
        //delete if you are owner of ad or admin
        String[] params = parseParams(request);
        User logged = (User) request.getSession().getAttribute("user");
        if(logged == null || logged.getCredencials().equals("PAR"))
        {
            response.setStatus(403); return;
        }
        try
        {
            AdJpaController ajc = new AdJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            ajc.destroy(Integer.parseInt(params[3]));
        } catch (Exception e)
        {response.setStatus(501); return;}
        
    }
}
