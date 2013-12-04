package pl.edu.pb.adserver.model.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Category;

/**
 * Class with access methods for 
 * @author dawid
 */
public class AdJdbc extends Jdbc {
    public static List<Ad> getAllAds()
    {
        List<Ad> list = new LinkedList<Ad>();
        String select_all = "SELECT * FROM ads";
        ResultSet rs = Jdbc.executeQuery(select_all);
        try {
        while(rs.next())
        {
            Ad a = new Ad(
                rs.getInt("id"),
                rs.getString("content"),
                Ad.ContentType.parse(rs.getString("contentType")),
                UserJdbc.getUser(rs.getInt("client")),
                new Category(rs.getString("category"))
            );
            //setting category
            list.add(a);
        }
        return list;
        } catch(SQLException e)
        {
            return null;
        }
    }
    
    public static Ad getAd(int id)
    {
        try{
            String get_query = "SELECT * FROM users WHERE id = " + id + ";";
            ResultSet rs = executeQuery(get_query);
            if(rs.next())
            {
                Ad a = new Ad();
                a.setId(rs.getInt("id"));
                a.setContent(rs.getString("content"));
                return a;
            }
            return null;
        } catch (SQLException e)
        {
            return null;
        }
        
    }
    
    public static void updateAd(Ad u)
    {
        String statement = "UPDATE ads SET content='" + u.getContent() +
                "' WHERE id = " + u.getId() + ";";
        executeUpdate(statement);
    }
    
    public static List<Ad> getAdByUser(String username)
    {
        String statement = "SELECT * FROM ads WHERE user = '" + username + "';";
        ResultSet rs = executeQuery(statement);
        List<Ad> ads = new LinkedList<Ad>();
        try
        {        while(rs.next())
        {
            Ad a = new Ad();
            a.setContent(rs.getString("content"));
        } 
            return ads;
        } catch(SQLException e) {
            return null;
        }
    }
    
    public static void createAd(Ad a)
    {
        //validate first
        
        //create statement
        String statement = "INSERT INTO ads(content, client, content_category, category)"+
                 "VALUES ";
    }
}
