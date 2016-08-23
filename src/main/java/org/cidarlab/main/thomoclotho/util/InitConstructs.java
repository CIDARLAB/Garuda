/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import static org.cidarlab.main.thomoclotho.Application.constructsID;
import static org.cidarlab.main.thomoclotho.Application.partsID;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.Annotation;
import org.clothocad.model.Feature;
import org.clothocad.model.Person;
import org.clothocad.model.Sequence;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitConstructs {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject, Person user) {
        
        try {
            FileWriter annJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-annotation.txt");
            FileWriter seqJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-sequence.txt");
            FileWriter feaJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-feature.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject annJSON = new JSONObject();
            JSONArray annArr = new JSONArray();
            
            JSONObject seqJSON = new JSONObject();
            JSONArray seqArr = new JSONArray();
            
            JSONObject feaJSON = new JSONObject();
            JSONArray feaArr = new JSONArray();
            
            //counter for clotho
            int[] clothoCount = {0, 0, 0};
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //Map conMap = new HashMap();
                //JSONObject c_obj = new JSONObject();
                
                //constructs
                
                //number of parts [col 5] and construct length [col 4]
                int numOfParts = (int) row.getCell(5).getNumericCellValue();
                int conLength = (int) row.getCell(4).getNumericCellValue();
                
                Set<Annotation> annotations = new HashSet<Annotation>();
                int mark_begin = 0;
                int mark_end = 0;
                
                for (int j=0; j<numOfParts; j++) {
                
                    String ano_id = "ann" + j + "" + System.currentTimeMillis(); //annotation id is automatically generated, somehow two annotations can be produced at the same time millis
                    
                    //check whether the part is a forward or reverse strand
                    boolean orientation = true;
                    Cell cell = row.getCell(6+j); 
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    int partVal = -1;
                    if (cellstr.indexOf('c')!=-1) {
                        orientation = false;
                        partVal = Integer.parseInt(cellstr.replaceAll("c", ""));
                    }
                    else
                        partVal = Integer.parseInt(cellstr);
                 
                    //set the beginning and the end of the annotation, instantiate and assign the annotation 
                    //int len = partsID.get(partVal-1).getSequence().getSequence().length();
                    mark_end = mark_begin + partsID.get(partVal-1).getSequence().getSequence().length();
                    Annotation anno = new Annotation(ano_id, "", mark_begin, mark_end-1, orientation, user);
                    anno.setFeature(partsID.get(partVal-1));
                    annotations.add(anno);
                    //System.out.println(ano_id +  "     " + value + "     " + len + "     " + partVal + "     " + mark_begin + "     " + mark_end);
                    mark_begin += mark_end;
                    
                    JSONObject annObj = anno.getJSON();
                    Map annMap = anno.getMap();
                    String annClo = (String) clothoObject.create(annMap);
                    if (!annClo.equals(null)) {
                        clothoCount[0]++;
                    }
                    annArr.add(annObj);
                }
                
                //additional check if the sequence starts with the barcode sequence [col 2]
                if (!row.getCell(3).getStringCellValue().startsWith(row.getCell(2).getStringCellValue()))
                    System.out.println("There is a barcode error in line " + i);
                
                //sequence [col 3]
                String seq_id = "seq" + System.currentTimeMillis(); //sequence id is automatically generated
                String sequence = row.getCell(3).getStringCellValue();
                if (sequence.length()!= conLength) { //additional checking if the construct length is not equal to the provided column
                    System.out.println("Error of construct length at row " + i + "...");
                }
                Sequence newSeq = new Sequence (seq_id, "", sequence, user);
                newSeq.setAnnotations(annotations);
                
                JSONObject seqObj = newSeq.getJSON();
                Map seqMap = newSeq.getMap();
                String seqClo = (String) clothoObject.create(seqMap);
                if (!seqClo.equals(null)) {
                    clothoCount[1]++;
                }
                seqArr.add(seqObj);
                
                //feature [role = col 1]
                String fea_id = "fea" + System.currentTimeMillis(); //sequence id is generated
                Feature.FeatureRole role = Feature.FeatureRole.TOXICITY_TEST; //default feature role
                if (row.getCell(1).getStringCellValue().equals("Toxicity Test")) {
                    role = Feature.FeatureRole.TOXICITY_TEST; //check for other types of role
                }
                //FeatureRole role = new String(row.getCell(3).getStringCellValue()).toUppercase().valueOf
                Feature newFeature = new Feature (fea_id, "", newSeq, role, user);
                constructsID.add(newFeature);
                
                //Map conMap = newFeature.getMap();
                JSONObject feaObj = newFeature.getJSON();
                Map feaMap = newFeature.getMap();
                String feaClo = (String) clothoObject.create(feaMap);
                if (!feaClo.equals(null)) {
                    clothoCount[2]++;
                }
                feaArr.add(feaObj);
                
                //System.out.println(feaObj);
                
                /*String con_id = "con" + System.currentTimeMillis();
                
                int[] conIdx = {0, 1, 2, 3};
                for (int j=0; j<conIdx.length; j++) {
                    Cell cell = row.getCell(conIdx[j]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    conMap.put(tableHeader.get(conIdx[j]), cellstr);
                    c_obj.put(tableHeader.get(conIdx[j]), cellstr);
                }*/
                
                //add to clotho
                /*String cloCon = (String) clothoObject.create(conMap);
                if (!cloCon.equals(null)) {
                    clothoCount[0]++;
                }*/
                
                //constructs part
                
                //int partNum = 0;
                //if (row.getCell(5).getCellType()==Cell.CELL_TYPE_NUMERIC) {
                //   partNum = (int) row.getCell(5).getNumericCellValue();
                //}
                /*for (int j=0; j<numOfParts; j++) {
                    
                    JSONObject cp_obj = new JSONObject();
                    
                    Map cpaMap = new HashMap();
                    
                    char orientation = 'f';
                    Cell cell = row.getCell(6+j); 
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    int partVal = -1;
                    if (cellstr.indexOf('c')!=-1) {
                        orientation = 'c';
                        partVal = Integer.parseInt(cellstr.replaceAll("c", ""));
                    }
                    else
                        partVal = Integer.parseInt(cellstr);
                    
                    cpaMap.put ("Construct ID", conMap.get(con_id));
                    cpaMap.put ("Part ID", partsID.get(partVal-1));
                    cpaMap.put ("Part Location", String.valueOf(j+1));
                    cpaMap.put ("Part Orientation", String.valueOf(orientation));
                    cp_obj.put("Construct ID", conMap.get(con_id));
                    cp_obj.put("Part ID", partsID.get(partVal-1));
                    cp_obj.put("Part Location", String.valueOf(j+1));
                    cp_obj.put("Part Orientation", String.valueOf(orientation));
                    
                    cp_arr.add(cp_obj);
                    
                    //String cloCpa = (String) clothoObject.create(cpaMap);
                    //if (!cloCpa.equals(null)) {
                    //    clothoCount[1]++;
                    // }
                }*/
            }
            
            System.out.println("Created " + clothoCount[0] + " Annotation objects" + "\n" +
                                "Created " + clothoCount[1] + " Sequence objects" + "\n" +
                                "Created " + clothoCount[2] + " Feature objects");
            
            annJSON.put("Name", "Annotation");
            annJSON.put("Entries", annArr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(annJSON);
            annJSONfile.write (prettyJson);
            
            seqJSON.put("Name", "Sequence");
            seqJSON.put("Entries", seqArr);

            prettyJson = gson.toJson(seqJSON);
            seqJSONfile.write (prettyJson);
            
            feaJSON.put("Name", "Feature");
            feaJSON.put("Entries", feaArr);

            prettyJson = gson.toJson(feaJSON);
            feaJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            annJSONfile.close();
            seqJSONfile.close();
            feaJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
