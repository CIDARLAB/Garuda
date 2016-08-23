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
            
            /*List<String> tableHeader = new ArrayList<String>();

            //read table header
            Row firstRow = sheet.getRow(0);
            for (int i=0; i<firstRow.getLastCellNum(); i++) {
                Cell cell = firstRow.getCell(i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tableHeader.add(cell.getStringCellValue());
            }*/
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            
            JSONObject seqJSON = new JSONObject();
            JSONArray seqArr = new JSONArray();
            
            JSONObject feaJSON = new JSONObject();
            JSONArray feaArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = {0, 0};
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //Map parMap = new HashMap();
                //Map traMap = new HashMap();
                
                //JSONObject p_obj = new JSONObject();
                //JSONObject t_obj = new JSONObject();
                
                //transcript
                
                /*int[] traIdx = {7, 8, 9, 10, 11, 12, 13, 14, 15};
                String tra_id = "tra" + System.currentTimeMillis();
                traMap.put ("Transcript ID", tra_id);
                t_obj.put("Transcript ID", tra_id);
                transcriptID.add(tra_id);
                
                int counter = 0;
                for (int j=0; j<traIdx.length; j++) {
                    Cell cell = row.getCell(traIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    if (!cellstr.equals("NA"))
                        counter++;
                    traMap.put(tableHeader.get(traIdx[j]), cellstr);
                    t_obj.put(tableHeader.get(traIdx[j]), cellstr);
                }
                if (counter>0)
                    t_arr.add(t_obj);*/
                
                //parts
                
                //parts length [col 1]
                int parLength = (int) row.getCell(1).getNumericCellValue();
                
                //sequence [col 2]
                String seq_id = "seq" + System.currentTimeMillis(); //sequence id is generated
                String sequence = row.getCell(2).getStringCellValue();
                if (sequence.length()!= parLength) { //additional checking if the part length is not equal to the provided column
                    System.out.println("Error of part length at row " + i + "...");
                }
                Sequence newSeq = new Sequence (seq_id, "", sequence, user);
                
                JSONObject seqObj = newSeq.getJSON();
                Map seqMap = newSeq.getMap();
                String seqClo = (String) clothoObject.create(seqMap);
                if (!seqClo.equals(null)) {
                    clothoCount[0]++;
                }
                seqArr.add(seqObj);
                
                //feature [role = col 3]
                String fea_id = "fea" + System.currentTimeMillis(); //sequence id is generated
                FeatureRole role = FeatureRole.CDS; //default feature role
                if (row.getCell(3).getStringCellValue().equals("protein_coding")) {
                    role = FeatureRole.CDS; //check for other types of role
                }
                //FeatureRole role = new String(row.getCell(3).getStringCellValue()).toUppercase().valueOf
                Feature newFeature = new Feature (fea_id, "", newSeq, role, user);
                //add IDs to the static list, because they will be used in different methods
                partsID.add(newFeature);
                
                JSONObject feaObj = newFeature.getJSON();
                Map feaMap = newFeature.getMap();
                String feaClo = (String) clothoObject.create(feaMap);
                if (!feaClo.equals(null)) {
                    clothoCount[1]++;
                }
                feaArr.add(feaObj);
                
                //System.out.println(newFeature);
                
                /*
                //length is not important?
                int[] parIdx = {1, 4, 5, 6};
                //String par_id = "par" + System.currentTimeMillis();
                parMap.put("Parts ID", fea_id);
                p_obj.put("Parts ID", fea_id);
                
                for (int j=0; j<parIdx.length; j++) {
                    Cell cell = row.getCell(parIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    parMap.put(tableHeader.get(parIdx[j]), cellstr);
                    p_obj.put(tableHeader.get(parIdx[j]), cellstr);
                }
                if (counter>0) {
                    parMap.put("Transcript ID", tra_id);
                    p_obj.put("Transcript ID", tra_id);
                }
                else if (counter==0) {
                    parMap.put("Transcript ID", "none");
                    p_obj.put("Transcript ID", "none");
                }
                p_arr.add(p_obj);
                
                //add to clotho
                String cloTra = (String) clothoObject.create(traMap);
                if (!cloTra.equals(null)) {
                    clothoCount[0]++;
                }
                String cloPar = (String) clothoObject.create(parMap);
                if (!cloPar.equals(null)) {
                    clothoCount[1]++;
                }*/
            }
            
            System.out.println("Created " + clothoCount[0] + " Sequence objects" + "\n" +
                                "Created " + clothoCount[1] + " Feature objects");
            
            /*System.out.println("Created " + clothoCount[1] + " objects at table Parts" + "\n" +
                                "Created " + clothoCount[0] + " objects at table Transcript");*/
            
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
