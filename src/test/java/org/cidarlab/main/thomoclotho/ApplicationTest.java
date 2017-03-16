/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import org.cidarlab.main.garuda.RecommendationEngine;
import java.util.Map;
import java.util.Random;
import org.cidarlab.main.garuda.PythonRunner;
import org.cidarlab.main.ml.CollaborativeFake;
import org.cidarlab.main.ml.CollaborativeFiltering;
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
        
    /*    String input = "input";
        String output = "output";
        
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        */
        
    //    SpreadsheetParser.initiate(inputFile);
    //    CollaborativeFiltering.start("resources/cf_test.csv");
        
        String folderName = "20161108";
        RecommendationEngine recEngine = new RecommendationEngine (folderName);
        
    /*    Random rand = new Random();
    
        for (int i=0; i<1000; i++) {
            System.out.println(rand.nextGaussian()*20+50);
        }*/
        
    //    PythonRunner pyrun = new PythonRunner();
        
        //BLAST search
        //String blastpath = "/Users/mardian/Documents/CIDAR/Garuda/Master/Garuda";
        //System.out.println (new RunBlast (false, RunBlast.Filters.SUBSEQ_ID, blastpath).init());
        
    }
}
