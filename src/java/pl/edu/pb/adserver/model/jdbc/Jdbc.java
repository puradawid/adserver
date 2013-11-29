package pl.edu.pb.adserver.model.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Class for handling jdbc connection
 */
public class Jdbc {
    protected final static String jdbcUrl = 
            "jdbc:mysql://localhost/test?user=root&password=root";
    protected static Connection getConnection()
    {
        try {
            return DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e)
        {
            Logger.getLogger("pl.edu.pb.adserver.model.jdbc.Jdbc")
                    .severe(e.getMessage());
            return null;
        }
    }
    protected static ResultSet executeQuery(String query)
    {
       try
       {
          return getConnection().createStatement().executeQuery(query);
       } catch (SQLException e)
       {
          Logger.getLogger("pl.edu.pb.adserver.model.jdbc.Jdbc")
                  .severe(e.getMessage());
          return null;
       }
    }
    
    protected static int executeUpdate(String query)
    {
       try
       {
          return getConnection().createStatement().executeUpdate(query);
       } catch (SQLException e)
       {
          Logger.getLogger("pl.edu.pb.adserver.model.jdbc.Jdbc")
                  .severe(e.getMessage());
          return 0;
       }
    }
}
