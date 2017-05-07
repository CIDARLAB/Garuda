/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cidarlab.garuda.rest.RESTClothoRequest;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitParts {
    
    public static Map<String, String> instantiate (XSSFSheet sheet, String outputFileUrl, String username) {
        
        Map<String, String> parts = new HashMap<String, String>();
        
        RESTClothoRequest rest = new RESTClothoRequest();
        
        JSONObject json = new JSONObject();
            
        try {
            FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_partsdb.fsa");
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String display_id = "p" + cell.getStringCellValue();
                
                //parts length [col 1]
                int parLength = (int) row.getCell(1).getNumericCellValue();
                //sequence [col 2]
                String seqname = "seq" + System.currentTimeMillis(); //sequence id is generated
                String sequence = row.getCell(2).getStringCellValue();
                if (sequence.length()!= parLength) { //additional checking if the part length is not equal to the provided column
                    throw new GarudaException("Error of part length at row " + i + "...");
                }
                //role [col 3]
                String role = "CDS"; //default feature role
                if (row.getCell(3).getStringCellValue().equals("protein_coding")) {
                    role = "CDS"; //check for other types of role
                }
                
                //write to FASTA file for BLAST local database
                seqFSAfile.write(">" + seqname + "\n");
                seqFSAfile.write(sequence + "\n");
                
                json.put("username", "mardian");
                json.put("objectName", display_id);
                json.put("sequence", sequence.toLowerCase());
                json.put("length", parLength);        //will create error if additional field is added
                json.put("role", role);
                
                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);
                
                String part_id = rest.createPart(jsonString);
                System.out.println(part_id);
                
                parts.put(display_id, part_id);
            }
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        finally {
            return parts;
        }
    }
}