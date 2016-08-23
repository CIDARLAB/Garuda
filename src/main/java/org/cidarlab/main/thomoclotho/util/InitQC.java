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
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitQC {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, Clotho clothoObject) {
        
        try {
            FileWriter qcJSON = new FileWriter(outputFileUrl + sheet.getSheetName () + "-qc.txt");
            
            List<String> tableHeader = new ArrayList<String>();

            //read table header
            Row firstRow = sheet.getRow(0);
            for (int i=0; i<firstRow.getLastCellNum(); i++) {
                Cell cell = firstRow.getCell(i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                tableHeader.add(cell.getStringCellValue());
            }
            
            //one JSON object container for each table, one JSON array for all table entries, one JSON object for each entry
            JSONObject q_json = new JSONObject();
            JSONArray q_arr = new JSONArray();
            
            //counter for clotho
            int clothoCount = 0;
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                Row row = sheet.getRow(i);
                
                Map qcMap = new HashMap();
                
                JSONObject q_obj = new JSONObject();
                
                Cell cell = row.getCell(0);
                if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
                    qcMap.put("Generated ID", genDataID.get((int)cell.getNumericCellValue()-1));
                    q_obj.put("Generated ID", genDataID.get((int)cell.getNumericCellValue()-1));
                }
                else if ((cell.getCellType()==Cell.CELL_TYPE_STRING) && cell.getStringCellValue().equals("NA")) {
                    qcMap.put("Generated ID", "None");
                    q_obj.put("Generated ID", "None");
                }
                
                cell = row.getCell(1);
                //somehow the cell data type is not numeric
                cell.setCellType(Cell.CELL_TYPE_STRING);
                if (cell.getStringCellValue().equals("NA")) {
                    qcMap.put("Part ID", "None");
                    q_obj.put("Part ID", "None");
                }
                else {
                    try {
                        int part_id = Integer.parseInt(cell.getStringCellValue());
                        qcMap.put("Part ID", partsID.get(part_id-1));
                        q_obj.put("Part ID", partsID.get(part_id-1));
                    }
                    catch (Exception ex) {
                        System.out.println("Error in parsing number for Part ID...");
                    }
                }
                
                cell = row.getCell(2);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                qcMap.put("Sequence", cell.getStringCellValue());
                q_obj.put("Sequence", cell.getStringCellValue());
                
                q_arr.add(q_obj);
                
                String cloQc = (String) clothoObject.create(qcMap);
                if (!cloQc.equals(null)) {
                    clothoCount++;
                }
            }
            
            System.out.println("Created " + clothoCount + " objects at table QC");
            
            q_json.put("Name", "qc");
            q_json.put("Entries", q_arr);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String prettyJson = gson.toJson(q_json);
            qcJSON.write (prettyJson);
            
            System.out.println("Successfully wrote JSON objects entries from " + sheet.getSheetName () + " sheet.");

            qcJSON.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
