/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.clotho.model.BioDesign;
import org.cidarlab.clotho.model.Feature;
import org.cidarlab.clotho.model.Person;
import org.cidarlab.garuda.rest.RESTRequest;
import org.cidarlab.garuda.util.InitConstructs;
import org.cidarlab.garuda.util.InitParts;
import org.cidarlab.garuda.util.RWR_Parser;
//import org.clothoapi.clotho3javaapi.Clotho;

/**
 *
 * @author mardian
 */
public class ApplicationInit {
    
    @Setter
    @Getter
    private RESTRequest rest;
    
    @Setter
    @Getter
    private List<Feature> partsID;
    
    @Setter
    @Getter
    private List<Feature> constructsID;
    
    @Setter
    @Getter
    private List<BioDesign> genDataID;
    
    /*@Setter
    @Getter
    private Clotho clothoObject;*/
    
    @Setter
    @Getter
    private Person user;
    
    @Setter
    @Getter
    private String message;
    
    @Setter
    @Getter
    private String username;
    
    @Setter
    @Getter
    private String password;
    
    @Setter
    @Getter
    private String email;
    
    @Setter
    @Getter
    private String filename;
    
    @Setter
    @Getter
    private Map<String, String> parts;
    
    public ApplicationInit() {}
    
    public ApplicationInit (String message) {
        
        rest = new RESTRequest();
        this.message = message;
    }
    
    public void register () {
        
        try {
            rest.createUser(username, email, password);
            this.message = "User is succesfully created. Please login to continue!";
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    public void login () {
        
        //System.out.println("************** It goes here with " + username + "    " + password);
        try {
            long startTime = System.currentTimeMillis();
            for (int i=0; i<100; i++) {
                System.out.println ("***" + i + " " + rest.createSequence());
            }
            System.out.println ("*** Finished at: " + (System.currentTimeMillis()-startTime)/1000 + " seconds.");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        
        if (username.equals("user123") && password.equals("pass"))
            this.message = "login successful";
        
        else
            this.message = "login failed";
        
        //to be added new login/logout function --session login
        
    /*    ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        clothoObject = new Clotho(conn);
        
        /////////create user/////////
        user = new Person (username);
        clothoObject.createUser(username, password);
        //clothoObject.logout();
        
        /////////login/////////
        Object loginRet = clothoObject.login(username, password);
        
        if (loginRet == null) {
            this.message = "Login failed!";
            return;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            this.message = "Login failed!";
            return;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            this.message = "Login failed!";
            return;
        }
        
        //clothoObject.logout();
        conn.closeConnection();*/
        
        return;
    }
    
    public void createPart (String username) {
        
        //try {
        switch (username) {
            case "robwarden":
                this.message = RWR_Parser.parse("resources/" + this.filename, "resources/output-", username);
                break;
            default:
                //this.message = RestSheetParser("resources/" + this.filename, "resources/output-", username);
                System.out.println("ERROR: username not found!");
                break;
        }
        //}
        //catch (Exception e) {e.printStackTrace();}
    }
    
    public void createConstruct () {
        
        try {
            
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    public void init (String username, String password, String jsonOutput) {
        
    /*    ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        clothoObject = new Clotho(conn);
        
        /////////login/////////
        user = new Person (username);
        Object loginRet = clothoObject.login(username, password);
        
        if (loginRet == null) {
            conn.closeConnection();
            this.message = "Login failed!";
            return;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            this.message = "Login failed!";
            return;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            this.message = "Login failed!";
            return;
        }*/
        
        //query if there has already been a person instance with the same username
        /////////fix me/////////
        
        String message = RestSheetParser("resources/" + this.filename, jsonOutput, this.username);
        
        //clothoObject.logout();
        //conn.closeConnection();
        
        this.message = message;
        
    }
    
    
    public String RestSheetParser (String inputUrl, String outputUrl, String username) {
        
        partsID = new ArrayList<Feature> ();
        constructsID = new ArrayList<Feature> ();
        genDataID = new ArrayList<BioDesign> ();
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
             
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
                
                System.out.println("------" + i + "-----");
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                
                switch (sheetName) {
                    case "Metadata":
                    case "metadata":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                    case "parts":
                        this.parts = InitParts.instantiate (sheet, outputUrl, username);
                        break;
                    case "Constructs":
                    case "constructs":
                        InitConstructs.instantiate (sheet, outputUrl, username, this.parts);
                        break;
                    case "Generated Data":
                    case "generated data":
                        //InitGeneratedData.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "RNASeq":
                    case "rnaseq":
                        //InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "QC":
                    case "qc":
                        //InitQC.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    default:
                        break;
                }
            }
            inputFile.close();
        } 
        catch (FileNotFoundException e) {
            return "File not found! Please enter the correct file name or url!";
        }
        catch (Exception ex) {
            ex.printStackTrace ();
        }
        finally {
            return "Data successfully added!";
        }
    }
    
    public String XLSReader (String inputUrl, String outputUrl) {
        
        partsID = new ArrayList<Feature> ();
        constructsID = new ArrayList<Feature> ();
        genDataID = new ArrayList<BioDesign> ();
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            
            //initiation of static lists that hold shared variables
            /*partsID = new ArrayList<Feature> ();
            constructsID = new ArrayList<Feature> ();
            genDataID = new ArrayList<BioDesign> ();*/
                
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
                
                System.out.println("------" + i + "-----");
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                
                switch (sheetName) {
                    case "Metadata":
                    case "metadata":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                    case "parts":
                        //InitParts_backup.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "Constructs":
                    case "constructs":
                        //InitConstructs_backup.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "Generated Data":
                    case "generated data":
                        //InitGeneratedData.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "RNASeq":
                    case "rnaseq":
                        //InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "QC":
                    case "qc":
                        //InitQC.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    default:
                        break;
                }
            }
            inputFile.close();
        } 
        catch (FileNotFoundException e) {
            return "File not found! Please enter the correct file name or url!";
        }
        catch (Exception ex) {
            ex.printStackTrace ();
        }
        finally {
            return "Data successfully added!";
        }
    }
    
    public void contains (String sequence) {
        
        /*int numOfAnno = 5;
        Set<String> results = new HashSet<String>();
        
        //String sequence = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        
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
        
        /*for (int j=0; j<queryResults.size(); j++) {
            Map queryResult = (Map) queryResults.get(j);
            String objectId = (String) queryResult.get("id");
            System.out.println (objectId);
            queryResult.put("testField", "For test purpose only");
            String setResult = (String) clothoObject.set(queryResult);
        }
        
        JSONArray queryResults2 = (JSONArray) clothoObject.query (queryMap);
        System.out.println (queryResults2);*/
        
    }
    
    public void containsAnd (String sequenceA, String sequenceB) {
        
        /*int numOfAnno = 5;
        
        Set<String> resultsA = new HashSet<String>();
        Set<String> resultsB = new HashSet<String>();
        
        //String sequenceA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        
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
        
        //String sequenceB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
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
        */
    }
    
    public void containsSpatial(String sequenceA, String sequenceB, boolean aPrecede) {
        
        /*int numOfAnno = 5;
        
        Set<String> nameA = new HashSet<String>();
        Set<String> nameB = new HashSet<String>();
        Set<Map> resultsA = new HashSet<Map>();
        Set<Map> resultsB = new HashSet<Map>();
        
        //String sequenceA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
        
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
        
        //String sequenceB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
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
        */
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
    
    /*public static <T> Set<T> difference(Set<T> setA, Set<T> setB) {
        Set<T> tmp = new TreeSet<T>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    public static <T> Set<T> symDifference(Set<T> setA, Set<T> setB) {
        Set<T> tmpA;
        Set<T> tmpB;

        tmpA = union(setA, setB);
        tmpB = intersection(setA, setB);
        return difference(tmpA, tmpB);
    }

    public static <T> boolean isSubset(Set<T> setA, Set<T> setB) {
        return setB.containsAll(setA);
    }

    public static <T> boolean isSuperset(Set<T> setA, Set<T> setB) {
        return setA.containsAll(setB);
    }*/
    
}