import java.util.logging.Logger;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Ad.ContentType;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.User.UserType;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.CategoryJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;

public class AdService extends ParseableServlet {
    
    CategoryJpaController cjc;
    AdJpaController ajc;
    UserJpaController ujc;
    
    @Override
    public void init(ServletConfig sc)
    {
            EntityManagerFactory emf = Persistence
                    .createEntityManagerFactory("AdServerPU");
            cjc = new CategoryJpaController(emf);
            ajc = new AdJpaController(emf);
            ujc = new UserJpaController(emf);   
    }
    
    protected void putError(HttpServletResponse response, int code)
    {
        response.setStatus(code);
    }
    
    protected void handleParnership(HttpServletRequest request, 
            HttpServletResponse response)
    {
        String[] params = parseParams(request);
        if(params.length == 3 && request.getParameterMap().size() == 0) //if get is without parameteres then run adgenerator
        {
            try {
            request.getRequestDispatcher("/WEB-INF/HTML/ad_generator.jsp")
                    .forward(request, response);
            return; // application loaded
            } catch (Exception e)
            {putError(response, 501);}
        } else
        {
            try {
            
            request.getRequestDispatcher("WEB-INF/XML/ad_link.jsp")
                    .forward(request, response);
                
            return; // application loaded
            } catch (Exception e)
            {putError(response, 501);}
        }
    }
            
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        //if there is a client then print list of his ads
        //if there is a admin, then print whole list of ads
        User logged = (User) request.getSession().getAttribute("user");
        if(logged == null) { response.setStatus(403); return; }
        
        if(logged.getCredencials().equals("PAR"))
        {
            handleParnership(request, response);
            return;
        }
        
        try
        {
            //get categories
            List<Category> categories = cjc.findCategoryEntities();
            List<User> users = ujc.findUserEntities();
            
            request.setAttribute("users", users);
            request.setAttribute("rootCategory", cjc.getRootCategory());
            request.setAttribute("categories", categories);
        } catch (Exception e)
        {putError(response, 501);}
        
        List<Ad> ads = new LinkedList<Ad>();
        if(logged.getCredencials().equals(UserType.ADM.toString()))
        {
            try {
                ads = ajc.findAdEntities();
                request.setAttribute("ads", ads);
            } catch (Exception e)
            {putError(response, 501);}
            
        }if (logged.getCredencials().equals(UserType.ADM.toString()))
        {
            try {
                ads = ajc.getUserAds(logged);
                request.setAttribute("ads", ads);
            } catch (Exception e)
            {putError(response, 501);}
        }
        try {
            request.getRequestDispatcher("/WEB-INF/HTML/manage_ads.jsp")
                    .forward(request, response);
        } catch(Exception e)
        {putError(response, 501);}
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
            Category c = cjc.findCategory(category);
            User u = ujc.findUser(user);
            Ad ad = new Ad(0, content, ContentType.parse(contentType), u, c);
            
            ajc.create(ad);
            response.sendRedirect(
                    request.getServletContext().getContextPath() + "/ad");
        } catch (Exception e)
        {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
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
            Ad a = ajc.findAd(Integer.parseInt(params[3]));
            
            String content = parameters.get("content");
            User u = ujc.findUser(parameters.get("user"));
            Category category = cjc.findCategory(parameters.get("category"));
            if(u != null) a.setUser(u);
            if(content != null) a.setContent(content);
            if(category != null) a.setCategory(category);
            
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
            ajc.destroy(Integer.parseInt(params[3]));
        } catch (Exception e)
        {response.setStatus(501); return;}
        
    }
}
