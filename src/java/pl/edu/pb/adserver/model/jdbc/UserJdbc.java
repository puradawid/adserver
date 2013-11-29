package pl.edu.pb.adserver.model.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import pl.edu.pb.adserver.model.User;
import static pl.edu.pb.adserver.model.jdbc.Jdbc.executeQuery;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class UserJdbc extends Jdbc {
    public static List<User> getAllUsers()
    {
        List<User> list = new LinkedList<User>();
        String select_all = "SELECT * FROM users";
        ResultSet rs = Jdbc.executeQuery(select_all);
        try {
        while(rs.next())
        {
            User u = new User(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("password"),
                User.UserType.CLI,
                rs.getString("name"),
                rs.getString("second_name"),
                rs.getString("telephone"));
            list.add(u);
        }
        return list;
        } catch(SQLException e)
        {
            return null;
        }
    }
    
    public static User getUser(int id)
    {
        try{
            String get_query = "SELECT * FROM users WHERE id = " + id + ";";
            ResultSet rs = executeQuery(get_query);
            if(rs.next())
            {
                User u = new User(
                    rs.getInt("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    User.UserType.CLI,
                    rs.getString("name"),
                    rs.getString("second_name"),
                    rs.getString("telephone"));
                return u;
            }
            return null;
        } catch (SQLException e)
        {
            return null;
        }
        
    }
    
    public static void updateUser(User u)
    {
        String statement = "UPDATE user SET name='" + u.getFirst_name() +
                "', second_name='" + u.getSecond_name() +
                "', email='" + u.getEmail() +
                "', telephone='" + u.getTelephone() +
                "' WHERE id = " + u.getId() + ";";
        executeUpdate(statement);
    }
    
    public static void removeUser(User u)
    {
        String statement = "DELETE FROM user WHERE ";
        //not implemented yet
        throw new NotImplementedException();
    }
}
