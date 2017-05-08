/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cidarlab.clotho.model.Grant;
import org.cidarlab.clotho.model.Institution;
import org.cidarlab.clotho.model.Person;
import org.cidarlab.clotho.model.Project;
import org.cidarlab.clotho.model.Publication;
//import org.clothoapi.clotho3javaapi.Clotho;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitMetadata {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Person user) {
        
        //try {
        /*    FileWriter perJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-person.txt");
            FileWriter insJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-institution.txt");
            FileWriter graJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-grant.txt");
            FileWriter pubJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-publication.txt");
            FileWriter proJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-project.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject perJSON = new JSONObject();
            JSONArray perArr = new JSONArray();
            
            JSONObject insJSON = new JSONObject();
            JSONArray insArr = new JSONArray();
            
            JSONObject graJSON = new JSONObject();
            JSONArray graArr = new JSONArray();
            
            JSONObject pubJSON = new JSONObject();
            JSONArray pubArr = new JSONArray();
            
            JSONObject proJSON = new JSONObject();
            JSONArray proArr = new JSONArray();*/
            
            //counter for clotho
            int[] clothoCount = new int[5];
            
            //counter for clotho
            int[] clothoCount = new int[5];

            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //-----person----- [col 9]
                String username = "user" + System.currentTimeMillis();
                Person newPer = new Person(username, user);
                newPer.setSurName(row.getCell(9).getStringCellValue());
                
            //    JSONObject perObj = newPer.getJSON();
                Map perMap = newPer.getMap();
                /*String perClo = (String) clothoObject.create(perMap);
                if (!perClo.equals(null)) {
                    clothoCount[0]++;
                }*/
            //    perArr.add(perObj);
                
                //-----institution----- [col 10]
                //String ins_id = "ins" + System.currentTimeMillis();
                Institution newIns = new Institution(row.getCell(10).getStringCellValue(), "", user);
                
            //    JSONObject insObj = newIns.getJSON();
                Map insMap = newIns.getMap();
                /*String insClo = (String) clothoObject.create(insMap);
                if (!insClo.equals(null)) {
                    clothoCount[1]++;
                }*/
            //    insArr.add(insObj);
                
                //-----grant----- [col 11]
                String grant = "gra" + System.currentTimeMillis();
                Grant newGra = new Grant(grant, "", row.getCell(11).getStringCellValue(), user);
                
            //    JSONObject graObj = newGra.getJSON();
                Map graMap = newGra.getMap();
                /*String graClo = (String) clothoObject.create(graMap);
                if (!graClo.equals(null)) {
                    clothoCount[2]++;
                }*/
            //    graArr.add(graObj);
                
                //-----project----- [col 0-8]
                Project newPro = new Project("pro" + System.currentTimeMillis(), "", user,
                                    row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue(),
                                    (int) row.getCell(2).getNumericCellValue(), row.getCell(4).getStringCellValue(),
                                    row.getCell(8).getStringCellValue(), row.getCell(3).getStringCellValue(),
                                    row.getCell(7).getStringCellValue(), row.getCell(5).getStringCellValue(),
                                    row.getCell(6).getStringCellValue());
                newPro.setPI(newPer);
                newPro.setGrant(newGra);
                
                //-----publication----- [col 12-13]
                String submitted = row.getCell(12).getStringCellValue();
                if(!submitted.equals(null) && !submitted.equals("NA")) {
                    Publication newPub = new Publication(submitted, "", user);
                //    JSONObject pubObj = newPub.getJSON();
                    Map pubMap = newPub.getMap();
                    /*String pubClo = (String) clothoObject.create(pubMap);
                    if (!pubClo.equals(null)) {
                        clothoCount[3]++;
                    }*/
                //    pubArr.add(pubObj);
                    newPro.addPublication(newPub);
                }
                String published = row.getCell(13).getStringCellValue();
                if(!published.equals(null) && !published.equals("NA")) {
                    Publication newPub = new Publication(published, "", user);
                //    JSONObject pubObj = newPub.getJSON();
                    Map pubMap = newPub.getMap();
                    /*String pubClo = (String) clothoObject.create(pubMap);
                    if (!pubClo.equals(null)) {
                        clothoCount[3]++;
                    }*/
                //    pubArr.add(pubObj);
                    newPro.addPublication(newPub);
                }
                
            //   JSONObject proObj = newPro.getJSON();
                Map proMap = newPro.getMap();
                /*String proClo = (String) clothoObject.create(proMap);
                if (!proClo.equals(null)) {
                    clothoCount[4]++;
                }*/
            //    proArr.add(proObj);
                
            }
            
            System.out.println("Created " + clothoCount[0] + " Person objects" + "\n" +
                                "Created " + clothoCount[1] + " Institution objects" + "\n" +
                                "Created " + clothoCount[2] + " Grant objects" + "\n" +
                                "Created " + clothoCount[3] + " Publication objects" + "\n" +
                                "Created " + clothoCount[4] + " Project objects");
            
        /*    perJSON.put("Name", "Person");
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
            
            proJSON.put("Name", "Project");
            proJSON.put("Entries", proArr);
            
            prettyJson = gson.toJson(proJSON);
            proJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            perJSONfile.close();
            insJSONfile.close();
            graJSONfile.close();
            pubJSONfile.close();
            proJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }
}