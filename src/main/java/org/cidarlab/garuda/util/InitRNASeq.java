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
import org.cidarlab.clotho.model.Expression;
import org.cidarlab.clotho.model.Feature;
import org.cidarlab.clotho.model.Person;
//import org.clothoapi.clotho3javaapi.Clotho;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitRNASeq {
       
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Person user, ApplicationInit app) {
       
    /*    try {
            FileWriter expJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-expression.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject expJSON = new JSONObject();
            JSONArray expArr = new JSONArray();*/
            
            //get the number of RNA sequencing entries
            int numOfExp = (int) sheet.getRow(0).getCell(1).getNumericCellValue();
            
            //counter for clotho
            int clothoCount = 0;
            
            for (int i=2; i<sheet.getLastRowNum()+1; i++) {
             
                Row row = sheet.getRow(i);
                
                //part ID
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String cellstr = cell.getStringCellValue();
                Feature part = null;
                int par_idx = 0;
                
                if (!cellstr.equals("NA")) {
                    try {
                        par_idx = Integer.parseInt(cellstr);
                        part = app.getPartsID().get(par_idx-1);
                    }
                    catch (Exception ex) {
                        System.out.println("Error of parsing part ID in RNASeq...");
                    }
                }
                
                for (int j=10; j<numOfExp+10; j++) {
               
                    BioDesign biod = app.getGenDataID().get(j-10);
                    
                    Cell exp = row.getCell(j);
                    exp.setCellType(Cell.CELL_TYPE_STRING);
                    String expstr = exp.getStringCellValue();
                    
                    if (!expstr.equals("NA")) {
                        try {
                            //reads
                            double read_val = Double.parseDouble(expstr);
                            
                            //---expresion---
                            String expname = "exp" + System.currentTimeMillis();
                            Expression newExp = new Expression(expname, "", read_val, sheet.getRow(0).getCell(j).getStringCellValue(), biod, part, user);
                            
                        //    JSONObject expObj = newExp.getJSON();
                            Map expMap = newExp.getMap();
                            /*String expClo = (String) clothoObject.create(expMap);
                            if (!expClo.equals(null)) {
                                clothoCount++;
                            }*/
                        //    expArr.add(expObj);
                        }
                        catch (Exception ex) {
                            System.out.println("Error in parsing number in RNASeq...");
                        }
                    }
                }
            }
            
            System.out.println("Created " + clothoCount + " Expression objects");
            
        /*    expJSON.put("Name", "Expression");
            expJSON.put("Entries", expArr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(expJSON);
            expJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            expJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }*/
    }
}