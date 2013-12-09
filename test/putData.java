import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.CategoryJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;

/**
 *
 * @author dawid
 */
public class putData {
    
    public putData() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   @Test
   public void initializeData()
   {
       
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("AdServerPU");
       CategoryJpaController cjc = new CategoryJpaController(emf);
       AdJpaController ajc = new AdJpaController(emf);
       UserJpaController ujc = new UserJpaController(emf);
       
       //create users
       User adm = new User(
               "admin", "admin", User.UserType.ADM, "Adminstrator", "", "");
       User cli = new User(
               "client", "client", User.UserType.CLI, "Joshua", "Manderlbort", "3423423");
       User par = new User(
               "partner", "partner", User.UserType.PAR, "John", "Smith", "5435345");
       try
       {
           ujc.create(adm);
           ujc.create(cli);
           ujc.create(par);
       } catch (Exception e) {}
       
       //create categories
       
       Category all = new Category("all");
       
       try
       {
           cjc.create(all);
           cjc.attachCategory("all", "buildings");
           cjc.attachCategory("buildings", "towers");
           cjc.attachCategory("all", "IT");
           cjc.attachCategory("all", "transport");
       } catch (Exception e) { }
       
       //create ADSSS
       
       Ad computer = new Ad(0, "Buy some computers man", "http://google.com", Ad.ContentType.html, cli, all);
       Ad shirt = new Ad(0, "Free shirts only in here", "http://google.com", Ad.ContentType.html, cli, all);
       Ad transit = new Ad(0, "Out trucks go anywhere you want!", "http://google.com", Ad.ContentType.html, cli, all);
       
       try
       {
           ajc.create(computer);
           ajc.create(shirt);
           ajc.create(transit);
       } catch (Exception e) {Logger.getLogger("putData").severe(e.getMessage());}
   }
}
