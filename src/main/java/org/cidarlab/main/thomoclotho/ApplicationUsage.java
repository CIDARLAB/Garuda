/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import net.sf.json.JSONArray;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothoapi.clotho3javaapi.ClothoConnection;

/**
 *
 * @author mardian
 */
public class ApplicationUsage {
    
    @Setter
    @Getter
    private Clotho clothoObject;
    
    @Setter
    @Getter
    private String message;
    
    @Setter
    @Getter
    private String seqA = "ATGAGTGAAAAGAAGAAAGTTCTAATGCTACATGGTTTTGTCCAATCCGATAAGATATTTTCTGCGAAGACTGGAGGATTACGAAAGAATTTGAAGAAGTTAGGTTACGATTTATACTATCCTTGCGCCCCACATTCAATTGATAAAAAAGCGTTATTCCAATCAGAGTCAGAAAAGGGTAGAGATGCTGCGAAGGAATTCAACACCTCAGCGACTAGTGATGAAGTATACGGGTGGTTCTTTAGAAATCCCGAATCTTTCAATTCCTTTCAAATAGATCAAAAGGTGTTTAACTATTTACGTAACTACGTGCTAGAAAATGGACCATTTGATGGTGTCATTGGATTCAGCCAAGGTGCAGGTCTTGGGGGCTACTTAGTCACTGACTTTAACAGAATATTAAATCTTACTGATGAACAACAGCCCGCTTTAAAATTTTTTATTTCATTTAGTGGATTCAAATTAGAAGATCAATCCTACCAGAAAGAATATCATAGGATTATCCAGGTGCCCTCTCTACATGTAAGGGGGGAGTTAGATGAAGTTGTAGCAGAATCTAGAATCATGGCATTGTACGAGTCATGGCCCGATAACAAAAGGACATTGTTGGTTCATCCTGGAGCCCATTTTGTCCCAAACTCGAAACCATTCGTATCCCAAGTTTGCAATTGGATCCAAGGAATTACTAGCAAAGAGGGTCAAGAGCATAATGCCCAACCTGAAGTAGATCGGAAACAATTTGACAAACCTCAATTGGAAGATGATTTGTTAGATATGATCGATTCCTTGGGTAAATTGTAA";
    
    @Setter
    @Getter
    private String seqB = "ATGTTGAAGTCAGCCGTTTATTCAATTTTAGCCGCTTCTTTGGTTAATGCAGGTACCATACCCCTCGGAAAGTTATCTGACATTGACAAAATCGGAACTCAAACGGAAATTTTCCCATTTTTGGGTGGTTCTGGGCCATACTACTCTTTCCCTGGTGATTATGGTATTTCTCGTGATTTGCCGGAAAGTTGTGAAATGAAGCAAGTGCAAATGGTTGGTAGACACGGTGAAAGATACCCCACTGTCAGCAAAGCCAAAAGTATCATGACAACGTGGTACAAATTGAGTAACTATACCGGTCAATTCAGCGGAGCATTGTCTTTCTTGAACGATGACTACGAATTTTTCATTCGTGACACCAAAAACCTAGAAATGGAAACCACACTTGCCAATTCGGTCAATGTTTTGAACCCATATACCGGTGAGATGAATGCTAAGAGACACGCTCGTGATTTCTTGGCGCAATATGGCTACATGGTCGAAAACCAAACCAGTTTTGCCGTTTTTACGTCTAACTCGAACAGATGTCATGATACTGCCCAGTATTTCATTGACGGTTTGGGTGATAAATTCAACATATCCTTGCAAACCATCAGTGAAGCCGAGTCTGCTGGTGCCAATACTCTGAGTGCCCACCATTCGTGTCCTGCTTGGGACGATGATGTCAACGATGACATTTTGAAAAAATATGATACCAAATATTTGAGTGGTATTGCCAAGAGATTAAACAAGGAAAACAAGGGTTTGAATCTGACTTCAAGTGATGCAAACACTTTTTTTGCATGGTGTGCATATGAAATAAACGCTAGAGGTTACAGTGACATCTGTAACATCTTCACCAAAGATGAATTGGTCCGTTTCTCCTACGGCCAAGACTTGGAAACTTATTATCAAACGGGACCAGGCTATGACGTCGTCAGATCCGTCGGTGCCAACTTGTTCAACGCTTCAGTGAAACTACTAAAGGAAAGTGAGGTCCAGGACCAAAAGGTTTGGTTGAGTTTCACCCACGATACCGATATTCTGAACTATTTGACCACTATCGGCATAATCGATGACCAAAATAACTTGACCGCCGAACATGTTCCATTCATGGAAAACACTTTCCACAGATCCTGGTACGTTCCACAAGGTGCTCGTGTTTACACTGAAAAGTTCCAGTGTTCCAATGACACCTATGTTAGATACGTCATCAACGATGCTGTCGTTCCAATTGAAACCTGTTCTACTGGTCCAGGGTTCTCTTGTGAAATAAATGACTTCTACGGCTATGCTGAAAAGAGAGTAGCCGGTACTGACTTCCTAAAGGTCTGTAACGTCAGCAGCGTCAGTAACTCTACTGAATTGACCTTTTTCTGGGACTGGAATACCAAGCACTACAACGACACTTTATTAAAACAGTAA";
        
    
    public ApplicationUsage () {
    }
    
    public void init (String username, String password) {
        
        ClothoConnection conn = new ClothoConnection("wss://localhost:8443/websocket");
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
        
        String message = containsAnd (seqA, seqB);
        
        conn.closeConnection ();
        
        this.message = message;
        
    }
    
    public void contains (String sequence) {
        
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
    
    public String containsAnd (String sequenceA, String sequenceB) {
        
        String output = "";
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
            output += "------- Construct name: " + name + " -------\n";
        
        output += "========== Found total: " + (finalResults.size()) + " objects satisfying your query! ==========\n";
        
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
    }
    
}
