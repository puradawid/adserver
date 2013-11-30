
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

public class ParseableServlet extends HttpServlet {
    private Map<String, String> buildParamMap(HttpServletRequest request) throws IOException {
        Map<String, String> params = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(
                new InputStreamReader(request.getInputStream()));
        String data = br.readLine();
        HttpEntity entity = new StringEntity(data, ContentType.APPLICATION_FORM_URLENCODED);
        List<NameValuePair> list = URLEncodedUtils.parse(entity);
        for (NameValuePair pair : list) {
            params.put(pair.getName(), pair.getValue());
        }
        return params;
    }
    
    protected String[] parseParams(HttpServletRequest request)
    {
        String[] params = (String[])(request.getAttribute("evolve"));
        if(params.length == 2) //there is no action
            { params = new String[3]; params[2] = "default"; }
        return params;
    }
}
