/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.sf.json.JSONArray;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.util.InitConstructs;
import org.cidarlab.main.util.InitGeneratedData;
import org.cidarlab.main.util.InitMetadata;
import org.cidarlab.main.util.InitParts;
import org.cidarlab.main.util.InitQC;
import org.cidarlab.main.util.InitRNASeq;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.clothocad.model.BioDesign;
import org.clothocad.model.Feature;
import org.clothocad.model.Person;

/**
 *
 * @author mardian
 */
public class Application {
    
    public static List<Feature> partsID;
    public static List<Feature> constructsID;
    public static List<String> transcriptID;
    public static List<BioDesign> genDataID;
    
    public static void initiate (String username, String password, String xlsInput, String jsonOutput) {
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        Clotho clothoObject = new Clotho(conn);
        
        /////////create user/////////
        clothoObject.createUser(username, password);
        
        /////////login/////////
        Object loginRet = clothoObject.login(username, password);
        
        if (loginRet == null) {
            conn.closeConnection();
            return;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            return;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            return;
        }
        
        //query if there has already been a person instance with the same username
        /////////fix me/////////
        Person user = new Person (username);
        
        XLSReader(xlsInput, jsonOutput, clothoObject, user);
        
        //query for testing
        //String query = "medium1";
        //containsExact (query, clothoObject);
        
        //String sequence = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        //contains (sequence, clothoObject);
        
        String seqA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        String seqB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
        containsAnd (seqA, seqB, clothoObject);
        //containsSpatial (seqA, seqB, clothoObject, true); //true for object A comes before object B
        
    }
    
    public static void XLSReader (String inputUrl, String outputUrl, Clotho clothoObject, Person user) {
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            
            //initiation of static lists that hold shared variables
            partsID = new ArrayList<Feature> ();
            constructsID = new ArrayList<Feature> ();
            transcriptID = new ArrayList<String> ();
            genDataID = new ArrayList<BioDesign> ();
            
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
            //for (int i=0; i<4; i++) {
                System.out.println("------" + i + "-----");
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                //String outputFileUrl = outputUrl + sheetName + ".txt";
                
                switch (sheetName) {
                    case "Metadata":
                    case "metadata":
                        InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                    case "parts":
                        InitParts.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Constructs":
                    case "constructs":
                        InitConstructs.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Generated Data":
                    case "generated data":
                        InitGeneratedData.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "RNASeq":
                    case "rnaseq":
                        InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "QC":
                    case "qc":
                        InitQC.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    default:
                        break;
                }
            }
            inputFile.close();
        } 
        catch (FileNotFoundException e) {
            System.out.println ("File not found! Please enter the correct file name or url!");
        }
        catch (Exception ex) {
            ex.printStackTrace ();
        }
    }
        
    public static void contains (String sequence, Clotho clothoObject) {
        
        int numOfAnno = 5;
        Set<String> results = new HashSet<String>();
        
        Map queryMap1 = new HashMap();
        queryMap1.put("sequence", sequence);
        JSONArray queryResults1 = (JSONArray) clothoObject.query (queryMap1);
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMap2 = new HashMap();
            queryMap2.put("sequence" + i, sequence);
            JSONArray queryResults2 = (JSONArray) clothoObject.query (queryMap2);
            
            for (int j=0; j<queryResults2.size(); j++) {
                Map queryResult = (Map) queryResults2.get(j);
                results.add((String) queryResult.get("name"));
            }
        }
        
        for (String name : results)
            System.out.println("------- Construct name: " + name + " -------");
        
        System.out.println("========== Found total: " + (queryResults1.size() + results.size()) + " objects satisfying your query! ==========");
        
    }
    
    public static void containsExact (String query, Clotho clothoObject) {
        
        Map queryMap = new HashMap();
        
        //queryMap.put("Vector", "pLac861");
        //queryMap.put("Measurement Conditions", "LCMS");
        queryMap.put("name", query);
        
        JSONArray queryResults = (JSONArray) clothoObject.query (queryMap);
        //System.out.println (queryResults);
        
    }
    
    public static void containsAnd (String sequenceA, String sequenceB, Clotho clothoObject) {
        
        int numOfAnno = 5;
        
        Set<String> resultsA = new HashSet<String>();
        Set<String> resultsB = new HashSet<String>();
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapA = new HashMap();
            queryMapA.put("sequence" + i, sequenceA);
            JSONArray queryResultsA = (JSONArray) clothoObject.query (queryMapA);
            for (int j=0; j<queryResultsA.size(); j++) {
                Map queryResultA = (Map) queryResultsA.get(j);
                resultsA.add((String) queryResultA.get("name"));
            }
        }
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapB = new HashMap();
            queryMapB.put("sequence" + i, sequenceB);
            JSONArray queryResultsB = (JSONArray) clothoObject.query (queryMapB);
            for (int j=0; j<queryResultsB.size(); j++) {
                Map queryResultB = (Map) queryResultsB.get(j);
                resultsB.add((String) queryResultB.get("name"));
            }
        }
        
        Set<String> finalResults = intersection(resultsA, resultsB);
        
        for (String name : finalResults)
            System.out.println("------- Construct name: " + name + " -------");
        
        System.out.println("========== Found total: " + (finalResults.size()) + " objects satisfying your query! ==========");
        
    }
    
    public static void containsSpatial(String sequenceA, String sequenceB, Clotho clothoObject, boolean aPrecede) {
        
        int numOfAnno = 5;
        
        Set<String> nameA = new HashSet<String>();
        Set<String> nameB = new HashSet<String>();
        Set<Map> resultsA = new HashSet<Map>();
        Set<Map> resultsB = new HashSet<Map>();
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapA = new HashMap();
            queryMapA.put("sequence" + i, sequenceA);
            JSONArray queryResultsA = (JSONArray) clothoObject.query (queryMapA);
            for (int j=0; j<queryResultsA.size(); j++) {
                Map queryResultA = (Map) queryResultsA.get(j);
                queryResultA.put("index", i);
                nameA.add((String) queryResultA.get("name"));
                resultsA.add(queryResultA);
            }
        }
        
        Iterator<Map> resA = resultsA.iterator();
        while (resA.hasNext()) {
            Map map = resA.next();
            if(!nameA.contains(map.get("name")))
                resA.remove();
        }
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapB = new HashMap();
            queryMapB.put("sequence" + i, sequenceB);
            JSONArray queryResultsB = (JSONArray) clothoObject.query (queryMapB);
            for (int j=0; j<queryResultsB.size(); j++) {
                Map queryResultB = (Map) queryResultsB.get(j);
                queryResultB.put("index", i);
                nameB.add((String) queryResultB.get("name"));
                resultsB.add(queryResultB);
            }
        }
        
        Iterator<Map> resB = resultsB.iterator();
        while (resB.hasNext()) {
            Map map = resB.next();
            if(!nameB.contains(map.get("name")))
                resB.remove();
        }
        
        Set<String> tempName = intersection(nameA, nameB);
        Set<Map> joinResults = unionMap(resultsA, resultsB);
        
        Iterator<Map> it = joinResults.iterator();
        while (it.hasNext()) {
            Map map = it.next();
            if(!tempName.contains(map.get("name")))
                it.remove();
        }
        
        List<Map> joinList = new ArrayList(joinResults);
        Set<String> finalResults = new HashSet<String>();
        
        //System.out.println(joinResults.size() + "  " + tempName.size() + "  " + resultsA.size() + "  " + resultsB.size() + "  " + nameA.size() + "  " + nameB.size());
            
        for(int i=0; i<joinList.size()-1; i++) {
            
            Map candidate = joinList.get(i);
            int cidx = (int) candidate.get("index");
            //int cstart = (int) candidate.get("start" + cidx);
            int cend = (int) candidate.get("end" + cidx);
            
            Map benchmark = joinList.get(i+1);
            int bidx = (int) benchmark.get("index");
            int bstart = (int) benchmark.get("start" + bidx);
            //int bend = (int) benchmark.get("end" + bidx);
            
            if (cend >= bstart)
                finalResults.add((String) candidate.get("name"));
        }
        
        for(String name : finalResults)
            System.out.println("------- Construct name: " + name + " -------");
        
        System.out.println("========== Found total: " + (finalResults.size()) + " objects satisfying your query! ==========");
        
    }
    
    public static Set<String> union(Set<String> setA, Set<String> setB) {
        
        Set<String> tmp = new HashSet<String>(setA);
        tmp.addAll(setB);
        return tmp;
    }

    public static Set<Map> unionMap(Set<Map> setA, Set<Map> setB) {
        
        Set<Map> tmp = new HashSet<Map>(setA);
        tmp.addAll(setB);
        return tmp;
    }

    public static Set<String> intersection(Set<String> setA, Set<String> setB) {
        
        Set<String> tmp = new HashSet<String>();
        for (String x : setA) {
            if (setB.contains(x))
                tmp.add(x);
        }
        return tmp;
    }
    
    public static Set<Map> intersectionMap(Set<Map> setA, Set<Map> setB) {
        
        Set<Map> tmp = new HashSet<Map>();
        for (Map x : setA) {
            if (setB.contains(x))
                tmp.add(x);
        }
        return tmp;
    }
    
}