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
import static org.cidarlab.main.thomoclotho.Application.partsID;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.Feature;
import org.clothocad.model.Feature.FeatureRole;
import org.clothocad.model.Person;
import org.clothocad.model.Sequence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitParts {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject, Person user) {
        
        try {
            FileWriter seqJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-sequence.txt");
            FileWriter feaJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-feature.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject seqJSON = new JSONObject();
            JSONArray seqArr = new JSONArray();
            
            JSONObject feaJSON = new JSONObject();
            JSONArray feaArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = new int[2];
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //parts
                
                //parts length [col 1]
                int parLength = (int) row.getCell(1).getNumericCellValue();
                
                //sequence [col 2]
                String seqname = "seq" + System.currentTimeMillis(); //sequence id is generated
                String sequence = row.getCell(2).getStringCellValue();
                if (sequence.length()!= parLength) { //additional checking if the part length is not equal to the provided column
                    System.out.println("Error of part length at row " + i + "...");
                }
                Sequence newSeq = new Sequence (seqname, "", sequence, user);
                
                JSONObject seqObj = newSeq.getJSON();
                Map seqMap = newSeq.getMap();
                String seqClo = (String) clothoObject.create(seqMap);
                if (!seqClo.equals(null)) {
                    clothoCount[0]++;
                }
                seqArr.add(seqObj);
                
                //feature [role = col 3]
                String feaname = "fea" + System.currentTimeMillis(); //sequence id is generated
                FeatureRole role = FeatureRole.CDS; //default feature role
                if (row.getCell(3).getStringCellValue().equals("protein_coding")) {
                    role = FeatureRole.CDS; //check for other types of role
                }
                Feature newFeature = new Feature (feaname, "", newSeq, role, user);
                //add IDs to the static list, because they will be used in different methods
                partsID.add(newFeature);
                
                JSONObject feaObj = newFeature.getJSON();
                Map feaMap = newFeature.getMap();
                String feaClo = (String) clothoObject.create(feaMap);
                if (!feaClo.equals(null)) {
                    clothoCount[1]++;
                }
                feaArr.add(feaObj);
                
            }
            
            System.out.println("Created " + clothoCount[0] + " Sequence objects" + "\n" +
                                "Created " + clothoCount[1] + " Feature objects");
            
            seqJSON.put("Name", "Sequence");
            seqJSON.put("Entries", seqArr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(seqJSON);
            seqJSONfile.write (prettyJson);
            
            feaJSON.put("Name", "Feature");
            feaJSON.put("Entries", feaArr);

            prettyJson = gson.toJson(feaJSON);
            feaJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            seqJSONfile.close();
            feaJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
