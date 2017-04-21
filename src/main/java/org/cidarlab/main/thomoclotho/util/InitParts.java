/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.util;

import java.io.FileWriter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.cidarlab.main.thomoclotho.rest.RESTRequest;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class InitParts {
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl) {
        
        RESTRequest rest = new RESTRequest();
        
        JSONObject json = new JSONObject();
            
        try {
            FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_partsdb.fsa");
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
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
                
                json.put("sequence", sequence);
                json.put("length", parLength);
                json.put("role", role);
                
                String jsonString = json.toJSONString().replaceAll("\"", "'");
                
                System.out.println (jsonString);
                
                rest.createPart(jsonString);
            }
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        //finally {
            //return jsonString;
        //}
    }
}