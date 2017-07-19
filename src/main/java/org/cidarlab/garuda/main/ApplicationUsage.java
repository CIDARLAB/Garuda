/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.cidarlab.garuda.main.Part.Orientation;
import org.cidarlab.garuda.main.Part.PartType;
import org.cidarlab.garuda.rest.RESTSynbiohubRequest;
//import org.clothoapi.clotho3javaapi.Clotho;
//import org.clothoapi.clotho3javaapi.ClothoConnection;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class ApplicationUsage {
    
    //@Setter
    //@Getter
    //private Clotho clothoObject;
    
    @Setter
    @Getter
    private RESTSynbiohubRequest rest;
    
    @Setter
    @Getter
    private String organization;
    
    @Setter
    @Getter
    private String message;
    
    @Setter
    @Getter
    private String fieldA, fieldB;
    
    @Setter
    @Getter
    private String seqA;// = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
    
    @Setter
    @Getter
    private String seqB;// = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
    
    @Setter
    @Getter
    private List<SearchResult> results;
    
    public ApplicationUsage () {
        
        rest = new RESTSynbiohubRequest();
        this.message = message;
    }
    
    public void init (String username, String password) {
        
        /*ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
        clothoObject = new Clotho(conn);
        
        /////////login/////////
        Object loginRet = clothoObject.login(username, password);
        
        if (loginRet == null) {
            conn.closeConnection();
            this.message = "login failed!";
            return;
        }
        if (loginRet.toString().equals("null")) {
            conn.closeConnection();
            this.message = "login failed!";
            return;
        }
        if (loginRet.toString().startsWith("Authentication attempt failed for username")) {
            conn.closeConnection();
            this.message = "login failed!";
            return;
        }
        
    //    String seqA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
    //    String seqB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
        String message = "Please input query!";
        if (!((seqA==null || seqB==null) || (seqA=="" || seqB==""))) {
            message = containsAnd (seqA, seqB);
        }
        
        conn.closeConnection ();*/
        
        this.message = message;
        
        //to be deleted
        Part a = new Part("pid001", PartType.PROMOTER, Orientation.FORWARD, new PartProperty ("sid001", "AAACCCGGGTTT"));
        Part b = new Part("pid002", PartType.RBS, Orientation.FORWARD, new PartProperty ("sid002", "CCCGGGTTTAAA"));
        Part c = new Part("pid003", PartType.TERMINATOR, Orientation.FORWARD, new PartProperty ("sid003", "TTTAAACCCGGG"));
        
        List<Part> listofpart = new ArrayList<Part>();
        listofpart.add(a);
        listofpart.add(b);
        listofpart.add(c);
        GeneticConstruct device = new GeneticConstruct("device_name", listofpart);
        System.out.println(GenBankExporter.writeGenBank(device, "random_project"));
        
    }
    
    /*public String[] getAnnotations() {
        
        String[] anno = null;
        if (results!=null) {
            anno = new String[results.size()];
            for (int i=0; i<anno.length; i++) {
                anno[i] = results.get(i).getAnnostring();
            }
        }
        return anno;
    }*/

    public String[] getAnnotations() {
        
        String[] anno = new String[results.size()];
        for (int i=0; i<anno.length; i++) {
            anno[i] = results.get(i).getAnnostring();
        }
        return anno;
    }
    
    /*public JSONArray getJSONArray(){
        
        JSONArray jsonarr = new JSONArray();
        
        JSONObject obj1 = new JSONObject();
        obj1.put("first", "rizki");
        obj1.put("last", "mardian");
        obj1.put("weight", 64);
        
        JSONObject obj2 = new JSONObject();
        obj2.put("first", "iki");
        obj2.put("last", "mardi");
        obj2.put("weight", 60);
        
        jsonarr.add(obj1);
        jsonarr.add(obj2);
        
        return jsonarr;
    }*/
    
    public JSONArray getJSONArray (){
        
        if (results!=null) {
        
            JSONArray arr = new JSONArray();
            
            for (int i=0; i<results.size(); i++) {

                JSONObject obj = new JSONObject();
                obj.put("id", results.get(i).getId());
                obj.put("author", results.get(i).getAuthor());
                obj.put("length", results.get(i).getLength());
                obj.put("sequence", results.get(i).getSequence());
                
                JSONArray inner_arr = new JSONArray();
                
                for (int j=0; j<results.get(i).getAnnotations().size(); j++) {
                    
                    JSONObject inner_obj = new JSONObject();
                    inner_obj.put("start", results.get(i).getAnnotations().get(j).getStart());
                    inner_obj.put("end", results.get(i).getAnnotations().get(j).getEnd());
                    inner_obj.put("label", results.get(i).getAnnotations().get(j).getLabel());
                    inner_obj.put("color", results.get(i).getAnnotations().get(j).getColor());
                    
                    inner_arr.add(inner_obj);
                    
                }
                
                obj.put("annotations", inner_arr);

                arr.add(obj);
            }
            
            return arr;
        }
        else
            return null;
    }
    
    public void contains (String sequence) {
        
        int numOfAnno = 5;
        Set<String> results = new HashSet<String>();
        
        Map queryMap1 = new HashMap();
        queryMap1.put("sequence", sequence);
        //JSONArray queryResults1 = (JSONArray) clothoObject.query (queryMap1);
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMap2 = new HashMap();
            queryMap2.put("sequence" + i, sequence);
            //JSONArray queryResults2 = (JSONArray) clothoObject.query (queryMap2);
            
            //for (int j=0; j<queryResults2.size(); j++) {
            //    Map queryResult = (Map) queryResults2.get(j);
            //    results.add((String) queryResult.get("name"));
            }
        }
        /*
        for (String name : results)
            System.out.println("------- Construct name: " + name + " -------");
        
        System.out.println("========== Found total: " + (queryResults1.size() + results.size()) + " objects satisfying your query! ==========");
        */
    
   /* public String containsAnd (String sequenceA, String sequenceB) {
        
        String output = "";
        int numOfAnno = 5;
        //Set<String> resultsA = new HashSet<String>();
        //Set<String> resultsB = new HashSet<String>();
        
        Set<Map> resultMapA = new HashSet<Map>();
        Set<Map> resultMapB = new HashSet<Map>();
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapA = new HashMap();
            queryMapA.put("sequence" + i, sequenceA);
            JSONArray queryResultsA = (JSONArray) clothoObject.query (queryMapA);
            
            //System.out.println("*** " + queryResultsA);
            
            for (int j=0; j<queryResultsA.size(); j++) {
                Map queryResultA = (Map) queryResultsA.get(j);
                //resultsA.add((String) queryResultA.get("name"));
                resultMapA.add(queryResultA);
            }
        }
        
        for (int i=0; i<numOfAnno; i++)
        {
            Map queryMapB = new HashMap();
            queryMapB.put("sequence" + i, sequenceB);
            JSONArray queryResultsB = (JSONArray) clothoObject.query (queryMapB);
            for (int j=0; j<queryResultsB.size(); j++) {
                Map queryResultB = (Map) queryResultsB.get(j);
                //resultsB.add((String) queryResultB.get("name"));
                resultMapB.add(queryResultB);
            }
        }
        
        //Set<String> finalResults = intersection(resultsA, resultsB);
        Set<Map> finalMap = intersectionMap(resultMapA, resultMapB);
        
        results = new ArrayList<SearchResult>();
        
        int idx = 0;
        for (Map map : finalMap) { 
            /*output += map.get("name") + "\n"
                    + map.get("description") + "\n"
                    + "created by: " + map.get("author") + "\n";
            
            List<Integer> annotations = new ArrayList<Integer>();
            List<String> labels = new ArrayList<String>();
            List<String> colors = new ArrayList<String>();
            colors.add("#F7464A");
            colors.add("#46BFBD");
            colors.add("#FDB45C");
            colors.add("#949FB1");
            colors.add("#3498db");
            
            int annolength = (int) map.get("annotation");
            annotations.add(0); //to add the beginning of the annotations
            for (int i=0; i<annolength; i++) {
                annotations.add((int) map.get("end" + i) + 1);
                labels.add((String) map.get("name" + i));
            }
            
            //Arrays.sort(annotations);
            Collections.sort(annotations);
            
            /*for (int i=0; i<annotations.length; i++) {
                output += annotations[i] + "\n";
            }
        //    System.out.println("****** " + annotations.size());
        //    System.out.println(map);
        
            results.add(new SearchResult ((String) map.get("name"), "entry" + idx, (int) map.get("length"), (String) map.get("sequence"), annotations, labels, colors, (String) map.get("author")));
            idx++;
        }
        
        output += "Found total " + finalMap.size() + " objects satisfying your query!\n";
        
        return output;
        
    }
    
    public void containsSpatial(String sequenceA, String sequenceB, boolean aPrecede) {
        
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
    }*/
    
    public void fetchSynbioHub () {
        
        
        try {
            this.message = rest.getLibSBOLJ();
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    public void postSynbioHub (String username, String password) {
        
        
        try {
            this.message = rest.postLibSBOLJ(username, password);
        }
        catch (Exception e) {e.printStackTrace();}
    }
    
    public String printSomething () {
        
        return "Completed!";
    }
    
}
