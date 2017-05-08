/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.rest.RESTRequest;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class RWR_Parser {
    
    private static Map<String, String> parts = new HashMap<String, String>();
    private static Map<String, String> enzymes = new HashMap<String, String>();
    
    private static RESTRequest rest = new RESTRequest();
    
    public static String parse (String inputUrl, String outputUrl, String username) {
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
             
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
                
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                System.out.println("-----" + (i+1) + ". " + sheetName + "-----");
                
                switch (sheetName) {
                    case "Assemblies":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                        break;
                    case "Rules":
                        //parts = populateParts (sheet, username);
                        break;
                    case "Final Strains":
                        //enzymes = populateEnzymes (sheet, username);
                        break;
                    case "Enumerated Constructs":
                        //InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    default:
                        System.out.println("WARNING: Found another sheet with unregistered name!! Do nothing...");
                        break;
                }
            }
            inputFile.close();
        } 
        catch (FileNotFoundException e) {
            return "File not found! Please enter the correct file name or url!";
        }
        catch (Exception ex) {
            ex.printStackTrace ();
        }
        finally {
            return "Data successfully added!";
        }
    }
    
    public static Map<String, String> populateParts (XSSFSheet sheet, String username) {
        
        JSONObject json = new JSONObject();
        List<String> roles = new ArrayList<String>();
        List<Integer> roles_idx = new ArrayList<Integer>();
        
        Row firstRow = sheet.getRow(0);
        for (int i = 0; i < firstRow.getLastCellNum() + 1; i++) {
            
            try {
                Cell cell = firstRow.getCell(i);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String role = cell.getStringCellValue();
                roles.add(role);
                roles_idx.add(i);
            } catch (NullPointerException n) {
                continue;
            };
        }
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            for (int j = 0; j < roles.size(); j++) {
                
                String role = roles.get(j);
                int idx = roles_idx.get(j);
                
                try {
                    
                    Cell cell = row.getCell(idx);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    
                    String display_id = cell.getStringCellValue();

                    json.put("username", username);
                    json.put("objectName", display_id);
                    json.put("role", role);

                    String jsonString = json.toJSONString().replaceAll("\"", "'");
                    System.out.println(jsonString);

                    String part_id = rest.createPart(jsonString);
                    System.out.println(part_id);
                    parts.put(display_id, part_id);
                    
                } catch (NullPointerException n) {
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return parts;
    }
    
    public static Map<String, String> populateEnzymes (XSSFSheet sheet, String username) {
        
        JSONObject json = new JSONObject();
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            for (int j = 2; j < 8; j++) {   //fix column
                String role = "Enzyme";     //fix role
                try {
                    
                    Cell cell = row.getCell(j);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    
                    String display_id = cell.getStringCellValue();

                    json.put("username", username);
                    json.put("objectName", display_id);
                    json.put("role", role);

                    String jsonString = json.toJSONString().replaceAll("\"", "'");
                    System.out.println(jsonString);

                    String part_id = rest.createPart(jsonString);
                    System.out.println(part_id);
                    enzymes.put(display_id, part_id);
                    
                } catch (NullPointerException n) {
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return enzymes;
    }
    
    public static void populateConstructs (XSSFSheet sheet, String outputFileUrl, String username, Map<String, String> parts) {
        
        JSONObject json = new JSONObject();
        JSONObject partSearcher = new JSONObject();
        
        try {
            FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_constructsdb.fsa");
            
            //int cc = 0;
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String display_id = "d" + cell.getStringCellValue();
                
                //number of parts [col 5] and construct length [col 4]
                int numOfParts = (int) row.getCell(5).getNumericCellValue();
                int conLength = (int) row.getCell(4).getNumericCellValue();
                
                //additional check if the sequence starts with the barcode sequence [col 2]
                if (!row.getCell(3).getStringCellValue().startsWith(row.getCell(2).getStringCellValue())) {
                    throw new GarudaException ("There is a barcode error in line " + i);
                }
                
                String seqname = "seq" + System.currentTimeMillis(); //sequence id is automatically generated
                
                String partsID = "";
                
                for (int j=0; j<numOfParts; j++) {
                
                    boolean orientation = true;
                    cell = row.getCell(6+j); 
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String cellstr = cell.getStringCellValue();
                    int partVal = -1;
                    if (cellstr.indexOf('c')!=-1) {
                        orientation = false;
                        partVal = Integer.parseInt(cellstr.replaceAll("c", ""));
                    }
                    else
                        partVal = Integer.parseInt(cellstr);
                    
                    if (j==0)
                        partsID = parts.get("p" + partVal);
                    else
                        partsID = partsID + "," + parts.get("p" + partVal);
                    
                }
                
                /*String feaname = "fea" + System.currentTimeMillis(); //sequence id is generated
                Feature.FeatureRole role = Feature.FeatureRole.TOXICITY_TEST; //default feature role
                if (row.getCell(1).getStringCellValue().equals("Toxicity Test")) {
                    role = Feature.FeatureRole.TOXICITY_TEST; //check for other types of role
                }*/
                
                json.put("username", "mardian");
                json.put("objectName", display_id);
                json.put("createSeqFromParts", "true");
                json.put("role", row.getCell(1).getStringCellValue());
                json.put("partIDs", partsID);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                
                System.out.println(jsonString);
                
                rest.createDevice(jsonString);
                
                
            }
            
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
