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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cidarlab.garuda.main.ApplicationInit;
import org.cidarlab.clotho.model.BioDesign;
import org.cidarlab.clotho.model.Feature;
import org.cidarlab.clotho.model.Person;
import org.cidarlab.clotho.model.QC;
import org.cidarlab.clotho.model.Sequence;
//import org.clothoapi.clotho3javaapi.Clotho;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitQC {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Person user, ApplicationInit app) {
        
    /*    try {
            FileWriter seqJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-sequence.txt");
            FileWriter qcJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-qc.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject seqJSON = new JSONObject();
            JSONArray seqArr = new JSONArray();
            
            JSONObject qcJSON = new JSONObject();
            JSONArray qcArr = new JSONArray();*/
            
            //counter for clotho
            int[] clothoCount = new int[2];
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                //-----sequence----- [col 2]
                String seqname = "seq" + System.currentTimeMillis(); //sequence id is generated
                String sequence = row.getCell(2).getStringCellValue();
                Sequence newSeq = new Sequence (seqname, "", sequence, user);
                
            //    JSONObject seqObj = newSeq.getJSON();
                Map seqMap = newSeq.getMap();
                /*String seqClo = (String) clothoObject.create(seqMap);
                if (!seqClo.equals(null)) {
                    clothoCount[0]++;
                }*/
            //    seqArr.add(seqObj);
                
                //-----genData and part ID----- [col 0 & 1]
                BioDesign biod = null;
                Feature feature = null;
                
                Cell cell = row.getCell(0);
                if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
                    int bioDesignIdx = (int) cell.getNumericCellValue();
                    biod = app.getGenDataID().get(bioDesignIdx-1);
                }
                cell = row.getCell(1);
                //somehow the cell data type is not numeric
                cell.setCellType(Cell.CELL_TYPE_STRING);
                if (!cell.getStringCellValue().equals("NA")) {
                    try {
                        int partIdx = Integer.parseInt(cell.getStringCellValue());
                        feature = app.getPartsID().get(partIdx-1);
                    }
                    catch (Exception ex) {
                        System.out.println("Error in parsing number for Part ID...");
                    }
                }
                
                String qcname = "qc" + System.currentTimeMillis();
                QC newQC = new QC (qcname, "", newSeq, biod, feature, user);
                
            //    JSONObject qcObj = newQC.getJSON();
                Map qcMap = newQC.getMap();
                /*String qcClo = (String) clothoObject.create(qcMap);
                if (!qcClo.equals(null)) {
                    clothoCount[1]++;
                }*/
            //    qcArr.add(qcObj);
                
            }
            
            System.out.println("Created " + clothoCount[0] + " Sequence objects" + "\n" +
                                "Created " + clothoCount[1] + " QC objects");
            
        /*    seqJSON.put("Name", "Sequence");
            seqJSON.put("Entries", seqArr);
            
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(seqJSON);
            seqJSONfile.write (prettyJson);
            
            qcJSON.put("Name", "QC");
            qcJSON.put("Entries", qcArr);
            
            prettyJson = gson.toJson(qcJSON);
            qcJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            qcJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }
}
