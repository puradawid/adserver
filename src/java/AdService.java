import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdService extends ParseableServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse resposnse)
    {
        //if there is a client then print list of his ads
        //if there is a parnet, then print generator for ads
        //if there is a admin, then print whole list of ads
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
