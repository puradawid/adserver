/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.LinkedList;
import java.util.List;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import pl.edu.pb.adserver.model.Category;
import pl.edu.pb.adserver.model.controller.CategoryJpaController;

/**
 *
 * @author dawid
 */
public class CategoryTest {
    
    public CategoryTest() {
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
    // @Test
    // public void hello() {}
    
    @Test
    public void test()
    {
        CategoryJpaController cjc = 
                new CategoryJpaController(
                        Persistence.createEntityManagerFactory("AdServerPU"));
        try {
        Category root = new Category("all");
        Category another = new Category("another");
        
        cjc.create(root);
        
        cjc.attachCategory("all", "another2");
        
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
        Category data_root = cjc.getRootCategory();
        Category data_another = data_root.getChilds().get(0);
        
        if(data_root != null && data_another != null) assert true;
        else assert false;
    }
}
