/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.Grant;
import org.clothocad.model.Institution;
import org.clothocad.model.Person;
import org.clothocad.model.Publication;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitMetadata {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject, Person user) {
        
        try {
            FileWriter perJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-person.txt");
            FileWriter insJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-institution.txt");
            FileWriter graJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-grant.txt");
            FileWriter pubJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-publication.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject perJSON = new JSONObject();
            JSONArray perArr = new JSONArray();
            
            JSONObject insJSON = new JSONObject();
            JSONArray insArr = new JSONArray();
            
            JSONObject graJSON = new JSONObject();
            JSONArray graArr = new JSONArray();
            
            JSONObject pubJSON = new JSONObject();
            JSONArray pubArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = new int[4];
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //-----person----- [col 9]
                String username = "user" + System.currentTimeMillis();
                Person newPer = new Person(username, user);
                newPer.setSurName(row.getCell(9).getStringCellValue());
                
                JSONObject perObj = newPer.getJSON();
                Map perMap = newPer.getMap();
                String perClo = (String) clothoObject.create(perMap);
                if (!perClo.equals(null)) {
                    clothoCount[0]++;
                }
                perArr.add(perObj);
                
                //-----institution----- [col 10]
                //String ins_id = "ins" + System.currentTimeMillis();
                Institution newIns = new Institution(row.getCell(10).getStringCellValue(), "", user);
                
                JSONObject insObj = newIns.getJSON();
                Map insMap = newIns.getMap();
                String insClo = (String) clothoObject.create(insMap);
                if (!insClo.equals(null)) {
                    clothoCount[1]++;
                }
                insArr.add(insObj);
                
                //-----grant----- [col 11]
                //String ins_id = "ins" + System.currentTimeMillis();
                Grant newGra = new Grant("", "", row.getCell(11).getStringCellValue(), user);
                
                JSONObject graObj = newGra.getJSON();
                Map graMap = newGra.getMap();
                String graClo = (String) clothoObject.create(graMap);
                if (!graClo.equals(null)) {
                    clothoCount[2]++;
                }
                graArr.add(graObj);
                
                //-----publication----- [col 12-13]
                String submitted = row.getCell(12).getStringCellValue();
                if(!submitted.equals(null) && !submitted.equals("NA")) {
                    Publication newPub = new Publication(submitted, "", user);
                    JSONObject pubObj = newPub.getJSON();
                    Map pubMap = newPub.getMap();
                    String pubClo = (String) clothoObject.create(pubMap);
                    if (!pubClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    pubArr.add(pubObj);
                }
                String published = row.getCell(13).getStringCellValue();
                if(!published.equals(null) && !published.equals("NA")) {
                    Publication newPub = new Publication(published, "", user);
                    JSONObject pubObj = newPub.getJSON();
                    Map pubMap = newPub.getMap();
                    String pubClo = (String) clothoObject.create(pubMap);
                    if (!pubClo.equals(null)) {
                        clothoCount[3]++;
                    }
                    pubArr.add(pubObj);
                }
                
                /*Map libMap = new HashMap();
                Map pubMap = new HashMap();
                Map tesMap = new HashMap();
                
                JSONObject l_obj = new JSONObject();
                JSONObject p_obj = new JSONObject();
                JSONObject t_obj = new JSONObject();
                
                //extract, add to JSON, add to map
                //publications
                
                int[] pubIdx = {9, 10, 11, 12, 13};
                String pub_id = "pub" + System.currentTimeMillis();
                pubMap.put("Publication ID", pub_id);
                p_obj.put("Publication ID", pub_id);
                
                for (int j=0; j<pubIdx.length; j++) {
                    Cell cell = row.getCell(pubIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    pubMap.put(tableHeader.get(pubIdx[j]), cellstr);
                    p_obj.put(tableHeader.get(pubIdx[j]), cellstr);
                }
                p_arr.add(p_obj);
                
                //test system
                
                int[] tesIdx = {3, 5, 6, 7, 8};
                String tes_id = "tes" + System.currentTimeMillis();
                tesMap.put("Test System ID", tes_id);
                t_obj.put("Test System ID", tes_id);
                
                for (int j=0; j<tesIdx.length; j++) {
                    Cell cell = row.getCell(tesIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    tesMap.put(tableHeader.get(tesIdx[j]), cellstr);
                    t_obj.put(tableHeader.get(tesIdx[j]), cellstr);
                }
                t_arr.add(t_obj);
                
                //library
                
                int[] libIdx = {0, 1, 2, 4, 5};
                for (int j=0; j<libIdx.length; j++) {
                    Cell cell = row.getCell(libIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    libMap.put(tableHeader.get(libIdx[j]), cellstr);
                    l_obj.put(tableHeader.get(libIdx[j]), cellstr);
                }
                libMap.put ("Publication ID", pub_id);
                l_obj.put("Publication ID", pub_id);
                libMap.put ("Test System ID", tes_id);
                l_obj.put("Test System ID", tes_id);
                
                l_arr.add(l_obj);
                
                //add to clotho
                String cloPub = (String) clothoObject.create(pubMap);
                if (!cloPub.equals(null)) {
                    clothoCount[0]++;
                }
                String cloTes = (String) clothoObject.create(tesMap);
                if (!cloTes.equals(null)) {
                    clothoCount[1]++;
                }
                String cloLib = (String) (clothoObject.create(libMap));
                if (!cloLib.equals(null)) {
                    clothoCount[2]++;
                }*/
            }
            
            System.out.println("Created " + clothoCount[0] + " Person objects" + "\n" +
                                "Created " + clothoCount[1] + " Institution objects" + "\n" +
                                "Created " + clothoCount[2] + " Grant objects" + "\n" +
                                "Created " + clothoCount[3] + " Publication objects");
            
            perJSON.put("Name", "Person");
            perJSON.put("Entries", perArr);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(perJSON);
            perJSONfile.write (prettyJson);
            
            insJSON.put("Name", "Institution");
            insJSON.put("Entries", insArr);
            
            prettyJson = gson.toJson(insJSON);
            insJSONfile.write (prettyJson);
            
            graJSON.put("Name", "Grant");
            graJSON.put("Entries", graArr);
            
            prettyJson = gson.toJson(graJSON);
            graJSONfile.write (prettyJson);
            
            pubJSON.put("Name", "Publication");
            pubJSON.put("Entries", pubArr);
            
            prettyJson = gson.toJson(pubJSON);
            pubJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            perJSONfile.close();
            insJSONfile.close();
            graJSONfile.close();
            pubJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
