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
import org.cidarlab.main.garuda.RWR_RecEngine;
import org.cidarlab.main.garuda.RWR_RecEngine_New;
import org.cidarlab.main.ml.CollaborativeFake;
import org.cidarlab.main.ml.CollaborativeFiltering;
import org.cidarlab.main.ml.ExpectationMaximization;
import org.cidarlab.main.ml.GaussJordan;
import org.cidarlab.main.ml.MulticollinearityCheck;
import org.cidarlab.main.ml.MultipleLinearRegression;
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
    
    //@Test
    public void testOLSRegression() {
        RWR_RecEngine.mRegression("resources/rob.xlsx");
    }
    
    //@Test
    public void featureGenerator() {
        RWR_RecEngine_New.mRegression_simplified("resources/rob.xlsx");
    }
     
    @Test
    public void multicollinearityCheck() {
        
        int num_of_constructs = 702;
        int num_of_parts = 49;
        String filename = "features.csv";
        
        new MulticollinearityCheck(num_of_constructs, num_of_parts, filename);
    }
     
    //@Test
    public void jvRegression() {
        
        RWR_RecEngine_New.mRegression_python();
    }
    
    //@Test
    public void gaussian() {
        double[][] input = new double[][] {
            {2, 0, 4, 2, 9},
            {3, 4, 0, 0, -7},
            {0, 1, 0, 5, 0}};
        GaussJordan gj = new GaussJordan(input);
        System.out.println(gj);
        gj.eliminate();
        System.out.println(gj);
    }
    
    //@Test
    public void testRegression() {
        double[][] x = {
                { 1, 1, 1, 1 },
                { 1, 0, 1, 1 },
                { 1, 0, 1, 0 },
                { 1, 0, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 0 },
                { 1, 0, 1, 1 },
                { 1, 1, 1, 1 },
                { 1, 1, 0, 1 },
                { 1, 0, 1, 1 },
                { 0, 0, 1, 0 },
                { 1, 0, 1, 1 },
                { 1, 1, 0, 1 },
                { 0, 1, 1, 0 },
                { 0, 1, 1, 0 },
                { 1, 1, 0, 1 },
                { 0, 1, 1, 0 },
                { 1, 1, 0, 1 },
                { 1, 0, 1, 1 },
                { 1, 1, 1, 1 }
            };
        
        double[] y = {1.29059717, 0.70544841, 0.75630101, 0.97549496, 0.43153817, 0.44917647,
                0.99678689, 0.90981844, 0.80678932, 0.83838037, 1.34614472, 1.43276749, 0.93183209,
                0.89969879, 0.98147421, 0.66831309, 1.28875777, 0.958625, 0.66133257,  0.71738615};
        
        MultipleLinearRegression mlr = new MultipleLinearRegression(x, y);
        System.out.println(mlr.beta(1));
        System.out.println(mlr.beta(2));
        System.out.println(mlr.beta(3));
    }
    
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
