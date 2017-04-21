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
import org.cidarlab.main.ml.ExpectationMaximization;
import org.cidarlab.main.ml.PrincipleComponentAnalysis;
import org.cidarlab.main.ml.SupportVectorMachine;
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
    
    public void testSVM() {
        SupportVectorMachine.svmTest();
    }
    
    public void testPCA() {
        
        double[][] input = {
            {0.69, 0.49},
            {-1.31, -1.21},
            {0.39, 0.99},
            {0.09, 0.29},
            {1.29, 1.09},
            {0.49, 0.79},
            {0.19, -0.31},
            {-0.81, -0.81},
            {-0.31, -0.31},
            {-0.71, -1.01},
        };
        
        double[][] output = PrincipleComponentAnalysis.pca(input, 1);
        
        for (int i=0; i<output.length; i++) {
            for (int j=0; j<output[0].length; j++) {
                System.out.print (output[i][j] + "\t");
            }
            System.out.println();
        }
    }
    
    public void testEM() {
        new ExpectationMaximization(2, true, true);
    }
    
    @Test
    public void testRecEngine() {
        
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
