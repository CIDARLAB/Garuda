/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import static org.cidarlab.main.thomoclotho.Application.genDataID;
import static org.cidarlab.main.thomoclotho.Application.partsID;
import org.clothoapi.clotho3javaapi.Clotho;
import org.clothocad.model.BioDesign;
import org.clothocad.model.Expression;
import org.clothocad.model.Feature;
import org.clothocad.model.Person;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitRNASeq {
       
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject, Person user) {
       
        try {
            FileWriter expJSONfile = new FileWriter(outputFileUrl + sheet.getSheetName () + "-expression.txt");
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject expJSON = new JSONObject();
            JSONArray expArr = new JSONArray();
            
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
                        part = partsID.get(par_idx-1);
                    }
                    catch (Exception ex) {
                        System.out.println("Error of parsing part ID in RNASeq...");
                    }
                }
                
                for (int j=10; j<numOfExp+10; j++) {
               
                    BioDesign biod = genDataID.get(j-10);
                    
                    Cell exp = row.getCell(j);
                    exp.setCellType(Cell.CELL_TYPE_STRING);
                    String expstr = exp.getStringCellValue();
                    
                    if (!expstr.equals("NA")) {
                        try {
                            //reads
                            double read_val = Double.parseDouble(expstr);
                            
                            //---expresion---
                            String exp_id = "exp" + System.currentTimeMillis();
                            Expression newExp = new Expression(exp_id, "", read_val, sheet.getRow(0).getCell(j).getStringCellValue(), biod, part, user);
                            
                            JSONObject expObj = newExp.getJSON();
                            Map expMap = newExp.getMap();
                            String expClo = (String) clothoObject.create(expMap);
                            if (!expClo.equals(null)) {
                                clothoCount++;
                            }
                            expArr.add(expObj);
                
                            /*
                            rnaMap.put("Reads", read_val);
                            r_obj.put("Reads", read_val);
                            
                            //software
                            Row row_soft = sheet.getRow(0);
                            cell = row_soft.getCell(j);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cellstr = cell.getStringCellValue();
                            rnaMap.put("Software", cellstr);
                            r_obj.put("Software", cellstr);
                            
                            r_arr.add(r_obj);
                            
                            String cloRna = (String) clothoObject.create(rnaMap);
                            if (!cloRna.equals(null)) {
                                clothoCount++;
                            }*/
                        }
                        catch (Exception ex) {
                            System.out.println("Error in parsing number in RNASeq...");
                        }
                    }
                    /*Map rnaMap = new HashMap();
                    
                    JSONObject r_obj = new JSONObject();
                    
                    
                    
                    //generated data ID
                    rnaMap.put("Generated Data ID", genDataID.get(j-10));
                    r_obj.put("Generated Data ID", genDataID.get(j-10));
                    
                    
                    
                        rnaMap.put("Part ID", "None");
                        r_obj.put("Part ID", "None");
                    }
                    else {
                        try {
                            int part_id = Integer.parseInt(cellstr);
                            rnaMap.put("Part ID", partsID.get(part_id-1));
                            r_obj.put("Part ID", partsID.get(part_id-1));
                            
                            //add transcript ID
                        }
                        catch (Exception ex) {
                            System.out.println("Error of parsing part ID in RNASeq...");
                        }
                    }
                    
                    cell = row.getCell(j);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellstr = cell.getStringCellValue();
                    
                    if (!cellstr.equals("NA")) {
                        try {
                            //reads
                            int read_val = Integer.parseInt(cellstr);
                            rnaMap.put("Reads", read_val);
                            r_obj.put("Reads", read_val);
                            
                            //software
                            Row row_soft = sheet.getRow(0);
                            cell = row_soft.getCell(j);
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            cellstr = cell.getStringCellValue();
                            rnaMap.put("Software", cellstr);
                            r_obj.put("Software", cellstr);
                            
                            r_arr.add(r_obj);
                            
                            String cloRna = (String) clothoObject.create(rnaMap);
                            if (!cloRna.equals(null)) {
                                clothoCount++;
                            }
                        }
                        catch (Exception ex) {
                            System.out.println("Error in parsing number in RNASeq...");
                        }
                    }*/
                }
            }
            
            System.out.println("Created " + clothoCount + " Expression objects");
            
            expJSON.put("Name", "Expression");
            expJSON.put("Entries", expArr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(expJSON);
            expJSONfile.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            expJSONfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
