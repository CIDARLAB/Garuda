/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mardian
 */
public class ApplicationTest {
    
    public ApplicationTest() {
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

    /**
     * Test of initiate method, of class Application.
     */
    @Test
    public void testInitiate() {
        //System.out.println("initiate");
        //String username = "";
        //String password = "";
        //String fileurl = "";
        //boolean expResult = false;
        //boolean result = Application.initiate(username, password, fileurl);
        
        String input = "input";
        String output = "output";
        
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        Application.initiate("user" + System.currentTimeMillis(), "pass", inputFile, outputFile);
        
        
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of parseXLSinput method, of class Application.
     */
    //@org.junit.Test
    //public void testParseXLSinput() {
    //    System.out.println("parseXLSinput");
    //    String fileurl = "";
    //    Map expResult = null;
    //    Map result = Application.parseXLSinput(fileurl);
    //    assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
    //    fail("The test case is a prototype.");
    //}

    /**
     * Test of insertData method, of class Application.
     */
    //@org.junit.Test
    //public void testInsertData() {
    //    System.out.println("insertData");
    //   Application.insertData();
        // TODO review the generated test code and remove the default call to fail.
    //    fail("The test case is a prototype.");
    //}
    
}
