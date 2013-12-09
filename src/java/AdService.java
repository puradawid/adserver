
import java.util.logging.Logger;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Ad.ContentType;
import pl.edu.pb.adserver.model.Ad.Orientation;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.User.UserType;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.CategoryJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;
import pl.edu.pb.adserver.model.util.CategoryBuilder;

public class AdService extends ParseableServlet {

    CategoryJpaController cjc;
    AdJpaController ajc;
    UserJpaController ujc;

    @Override
    public void init(ServletConfig sc) {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("AdServerPU");
        cjc = new CategoryJpaController(emf);
        ajc = new AdJpaController(emf);
        ujc = new UserJpaController(emf);
    }

    protected void putError(HttpServletResponse response, int code) {
        response.setStatus(code);
    }

    protected void handleParnership(HttpServletRequest request,
            HttpServletResponse response) {
        String[] params = parseParams(request);
        loadCategories(request, response);
        if (params.length == 3 && request.getParameterMap().size() == 0) //if get is without parameteres then run adgenerator
        {
            try {
                request.getRequestDispatcher("/WEB-INF/HTML/ad_generator.jsp")
                        .forward(request, response);
                return; // application loaded
            } catch (Exception e) {
                putError(response, 501);
            }
        } else {
            try {

                request.getRequestDispatcher("WEB-INF/XML/ad_link.jsp")
                        .forward(request, response);

                return; // application loaded
            } catch (Exception e) {
                putError(response, 501);
            }
        }
    }

    protected void handleQuery(HttpServletRequest request,
            HttpServletResponse response) {

        Ad randomed = null;
        
        List<Ad> adBunch = new LinkedList<Ad>(); //all taken entites
        
        Category category = null;
        Ad.Orientation orientation = null;
        Ad.ContentType media = null;
        
        String categoryName = request.getParameter("category");
        String mediaName = request.getParameter("media");
        String orientationName = request.getParameter("orientation");
        
        if(categoryName == null) categoryName = "all";

        try
        {
            media = ContentType.parse(mediaName);
            category = cjc.findCategory(categoryName);
            orientation = Orientation.parse(orientationName);
            CategoryBuilder cb = new CategoryBuilder(category, "");
            List<Category> categories = (List<Category>)cb.getResultObject();
            for(Category cat : categories)
                adBunch.addAll(ajc.findAds(cat, ContentType.parse(mediaName),
                    orientation));
            Random r = new Random();
            randomed = adBunch.get(r.nextInt(adBunch.size()));
        } catch (Exception e)
        {
            Logger.getLogger("AdService").severe(e.getMessage());
        }
        
        
        
        try {
            randomed.setViews(randomed.getViews() + 1);
            ajc.edit(randomed);
            request.setAttribute("ad", randomed); //pass advertisement to JSP
            request.getRequestDispatcher("WEB-INF/XML/generate_ad.jsp")
                    .forward(request, response);
        } catch (ServletException e) {

        } catch (IOException e) {
            
        } catch(Exception e) {
            
        } finally {

        }
    }

    protected void loadCategories(HttpServletRequest request, HttpServletResponse response) {
        try {
            //get categories
            List<Category> categories = cjc.findCategoryEntities();
            List<User> users = ujc.findUserEntities();

            request.setAttribute("users", users);
            request.setAttribute("rootCategory", cjc.getRootCategory());
            request.setAttribute("categories", categories);
        } catch (Exception e) {
            putError(response, 501);
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        //if there is a client then print list of his ads
        //if there is a admin, then print whole list of ads
        User logged = (User) request.getSession().getAttribute("user");
        if (logged == null && request.getParameterMap().isEmpty()) {
            response.setStatus(403);
            return;
        }
        if(request.getParameter("action") != null
                && request.getParameter("action").equals("redirect"))
        {
            int id = Integer.parseInt(request.getParameter("id"));
            Ad a = ajc.findAd(id);
            try {
            a.setClicks(a.getClicks() + 1);
            ajc.edit(a);
            response.sendRedirect(a.getReferer());
            return;
            } catch (Exception e) {}
        }
        
        if (request.getParameter("action") != null &&
                request.getParameter("action").equals("getAd")) {
            handleQuery(request, response);
            return;
        }

        loadCategories(request, response);

        if (logged != null && logged.getCredencials().equals("PAR")) {
            handleParnership(request, response);
            return;
        }

        List<Ad> ads = new LinkedList<Ad>();
        if (logged != null && 
                logged.getCredencials().equals(UserType.ADM.toString())) {
            try {
                ads = ajc.findAdEntities();
                request.setAttribute("ads", ads);
            } catch (Exception e) {
                putError(response, 501);
            }

        }
        if (logged != null && 
                logged.getCredencials().equals(UserType.CLI.toString())) {
            try {
                ads = ajc.getUserAds(logged);
                request.setAttribute("ads", ads);
            } catch (Exception e) {
                putError(response, 501);
            }
        }
        try {
            request.getRequestDispatcher("/WEB-INF/HTML/manage_ads.jsp")
                    .forward(request, response);
        } catch (Exception e) {
            putError(response, 501);
        }
        //if there is a partner, then print generator for ads

    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        //create new ad if you are client
        try {
            User logged = (User) request.getSession().getAttribute("user");
            Map<String, String> params = buildParamMap(request);
            String content = params.get("content");
            String referer = params.get("referer");
            String user = params.get("user");
            if (logged.getCredencials().equals(UserType.CLI.toString())) {
                user = logged.getEmail(); //ad user overload
            }
            String category = params.get("category");
            String contentType = params.get("content_type");
            Category c = cjc.findCategory(category);
            User u = ujc.findUser(user);
            Ad ad = new Ad(0, content, referer, ContentType.parse(contentType), u, c);

            ajc.create(ad);
            response.sendRedirect(
                    request.getServletContext().getContextPath() + "/ad");
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        //update ad if you are client
        String[] params = parseParams(request);
        Map<String, String> parameters = null;
        try {
            parameters = buildParamMap(request);
        } catch (IOException e) {
        }
        User logged = (User) request.getSession().getAttribute("user");
        if (logged == null || logged.getCredencials().equals("PAR")) {
            response.setStatus(403);
            return;
        }

        try {
            Ad a = ajc.findAd(Integer.parseInt(params[3]));

            String content = parameters.get("content");
            User u = ujc.findUser(parameters.get("user"));
            Category category = cjc.findCategory(parameters.get("category"));
            String referer = parameters.get("referer");
            ContentType media = Ad.ContentType.parse(parameters.get("media"));
            Ad.Orientation orientation = Ad.Orientation.parse(
                parameters.get("orientation"));
            
            if (u != null) 
                a.setUser(u);
            if (content != null)
                a.setContent(content);
            if (category != null)
                a.setCategory(category);            
            if(orientation != null)
                a.setOrientation(orientation);
            if(referer != null)
                a.setReferer(referer);
            if(media != null)
                a.setContentType(media);
            
            ajc.edit(a);
        } catch (Exception e) {
            response.setStatus(403);
            return;
        }

    }

    @Override
    public void doDelete(
            HttpServletRequest request, HttpServletResponse response) {
        //delete if you are owner of ad or admin
        String[] params = parseParams(request);
        User logged = (User) request.getSession().getAttribute("user");
        if (logged == null || logged.getCredencials().equals("PAR")) {
            response.setStatus(403);
            return;
        }
        try {
            ajc.destroy(Integer.parseInt(params[3]));
        } catch (Exception e) {
            response.setStatus(501);
            return;
        }

    }
}
