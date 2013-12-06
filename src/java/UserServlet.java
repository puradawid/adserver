import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.persistence.Persistence;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.UserJpaController;
import pl.edu.pb.adserver.model.controller.exceptions.NonexistentEntityException;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class UserServlet extends ParseableServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        //same /user here and no matter what is further
        //load users
        UserJpaController ujc = new UserJpaController(
                Persistence.createEntityManagerFactory("AdServerPU"));
        List<User> users = ujc.findUserEntities();
        request.setAttribute("users", users);
        try {
            request.getRequestDispatcher("/WEB-INF/HTML/users_table.jsp").forward(request, response);
        } catch (ServletException e) {
            Logger.getLogger("UserServlet.doGet()").severe(e.getMessage());
        } catch (IOException e) {
            Logger.getLogger("UserServlet.doGet()").severe(e.getMessage());
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        UserJpaController ujc;

        String name = request.getParameter("name");
        String surname = request.getParameter("surname");
        String telephone = request.getParameter("telephone");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        if (name == null || password == null || email == null) {
            try {
                response.sendRedirect("/user");
            } catch (IOException e) {
                Logger.getLogger("UserServlet.doPost()").severe(e.getMessage());
            }
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String[] params = parseParams(request);
        //check user credencials
        if (params[2].equals("id") && params[3] != null) {
            Integer id = Integer.parseInt(params[3]);
            UserJpaController ujc = new UserJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            try {
                ujc.destroy(id);
                //response.sendRedirect("/user");
            } catch (NonexistentEntityException e) {
            }
        }
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        String[] params = parseParams(request);
        //check user credencials
        Object potential_user = request.getSession().getAttribute("user");
        //if( potential_user == null ||
        //        ((User)potential_user).getCredencials().equals("ADM"))
        //{response.setStatus(403); return;}
        if (params[2].equals("id") && params[3] != null) {
            Integer id = Integer.parseInt(params[3]);
            UserJpaController ujc = new UserJpaController(
                    Persistence.createEntityManagerFactory("AdServerPU"));
            response.setStatus(200);

            try {
                User u = ujc.findUser(id);

                Map<String, String> paramMap = buildParamMap(
                        request);

                String email = paramMap.get("email");
                String name = paramMap.get("name");
                String surname = paramMap.get("surname");
                String telephone = paramMap.get("telephone");
                String password = paramMap.get("password");
                String type = paramMap.get("credencials");
                if (email != null) {
                    u.setEmail(email);
                }
                if (name != null) {
                    u.setFirst_name(name);
                }
                if (surname != null) {
                    u.setSecond_name(surname);
                }
                if (telephone != null) {
                    u.setTelephone("telephone");
                }
                if (password != null) {
                    u.setTelephone("password");
                }
                if (type != null) {
                    u.setCredencials(type);
                }
                if (password != null) {
                    u.setPassword(password);
                }
                ujc.edit(u);
                response.getWriter().println(name);
                response.getWriter().println(email);
            } catch (NonexistentEntityException e) {
                response.setStatus(501);
            } catch (Exception e) {
            }
        }
    }
}
