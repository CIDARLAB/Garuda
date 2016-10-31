/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import org.cidarlab.main.thomoclotho.RunBlast.Filters;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
        
        String user = "user123";
        String pass = "pass";
        
        String input = "input";
        String output = "output";
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        ApplicationInit app = new ApplicationInit(user);
        System.out.println(app.init(user, pass, inputFile, outputFile));
        
        //query for testing
        //String sequence = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        //app.contains ();
        
        String seqA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        String seqB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
        //app.containsAnd (seqA, seqB);
        app.containsSpatial (seqA, seqB, true); //true for object A comes before object B
        
        //BLAST search
        //String blastpath = "/Users/mardian/Documents/CIDAR/Garuda/Spring/Garuda";
        //System.out.println (new RunBlast (false, Filters.SUBSEQ_ID, blastpath).init());
        
        
        //test for collaborative filtering
        /*String cfFile = "resources/cf_test.csv";
        CollaborativeFiltering.start (cfFile);*/
        
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
