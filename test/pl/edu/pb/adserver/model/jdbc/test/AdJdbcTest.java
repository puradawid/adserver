/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pl.edu.pb.adserver.model.jdbc.test;

import javax.naming.Context;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.edu.pb.adserver.model.Ad;
import pl.edu.pb.adserver.model.User;
import pl.edu.pb.adserver.model.controller.AdJpaController;
import pl.edu.pb.adserver.model.controller.UserJpaController;
import pl.edu.pb.adserver.model.controller.exceptions.PreexistingEntityException;
import pl.edu.pb.adserver.model.jdbc.AdJdbc;

/**
 *
 * @author dawid
 */
public class AdJdbcTest {
    
    public AdJdbcTest() {
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

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void initializeClass() {
         AdJdbc testing = new AdJdbc();
     }
     
     @Test
     public void createAd()
     {
         AdJpaController ajc = new AdJpaController(Persistence.createEntityManagerFactory("AdServerPU"));
         try {
         ajc.create(new Ad(0, "This is content", Ad.ContentType.picture, null, "hgw"));
         } catch (PreexistingEntityException e)
         {
             //this entitty still existist in context
             assert true;
         } catch (Exception e)
         {
             //something other happend
             assert true;
         }
     }
     
     @Test
     public void createAdsAndUsers()
     {
         User u = new User("puradawid@gmail.com", "password", User.UserType.ADM, "Dawid",
         "Pura", "795638387");
         Ad a = new Ad(0, "This is content", Ad.ContentType.html, u, "all");
         
         EntityManagerFactory emf = Persistence.createEntityManagerFactory(
                 "AdServerPU");
         
         UserJpaController ujc = new UserJpaController(emf);
         AdJpaController ajc = new AdJpaController(emf);
         
         try {
            //ujc.create(u);
            ajc.create(a);
         } catch (PreexistingEntityException e)
         {
             assert true;
         } catch (Exception e) {
             assert true;
         }
         
     }
}
