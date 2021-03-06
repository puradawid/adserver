
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Filter is designed for handling simple REST with path execution.
 * @author dawid
 */
@WebFilter(filterName = "RESTFilter", urlPatterns = {"/*"})
public class RESTFilter implements Filter {
    
    FilterConfig fc;
    Map<String, String> mapping;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.fc = filterConfig;
        mapping = new HashMap<String, String>();
        
        //hardcoded for now
        mapping.put("user", "/userservlet");
        mapping.put("login", "/WEB-INF/HTML/login.jsp");
        mapping.put("logout", "/WEB-INF/HTML/logout.jsp");
        mapping.put("register", "/WEB-INF/HTML/register.jsp");
        mapping.put("ad", "/adservice");
        mapping.put("category", "/WEB-INF/HTML/category_manager.jsp");
        
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    
        String[] elements = ((HttpServletRequest)request).getServletPath().split("/");
        
        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;
        
        if(elements.length < 2)
        {
            req.getRequestDispatcher("/WEB-INF/HTML/index.jsp").forward(req, res);
            return;
        }
        
        if(elements.length >= 2 && mapping.containsKey(elements[1]))
        {
            //pass argument
            req.setAttribute("evolve", elements);
            req.getRequestDispatcher(mapping.get(elements[1]))
                    .forward(request, response);
        } else
        {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {
    }
    
    
}
