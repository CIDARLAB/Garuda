/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.legacyutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
import org.cidarlab.garuda.services.ClothoService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mardian
 */
public class Guy_Parser {
    
    @Autowired
    static ClothoService clotho;
    
    @Setter
    @Getter
    private static Map<String, String> parts;
    
    public static String parse (String inputUrl, String outputUrl, String username, HttpSession session) {
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
             
            for (int i=0; i<workbook.getNumberOfSheets(); i++) {
                
                System.out.println("------" + i + "-----");
                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName ();
                
                switch (sheetName) {
                    case "Metadata":
                    case "metadata":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Parts":
                    case "parts":
                        parts = populateParts (sheet, outputUrl, session);
                        break;
                    case "Constructs":
                    case "constructs":
                        InitConstructs.instantiate (sheet, outputUrl, username, parts);
                        break;
                    case "Generated Data":
                    case "generated data":
                        //InitGeneratedData.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "RNASeq":
                    case "rnaseq":
                        //InitRNASeq.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    case "QC":
                    case "qc":
                        //InitQC.instantiate (sheet, outputUrl, clothoObject, user, this);
                        break;
                    default:
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
    
    public static Map<String, String> populateParts (XSSFSheet sheet, String outputFileUrl, HttpSession session) {
        
        Map<String, String> parts = new HashMap<String, String>();
        
        JSONObject json = new JSONObject();
            
        try {
            FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_partsdb.fsa");
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                AddForm addForm = new AddForm();
                ArrayList<Parameter> paramList = new ArrayList<>();
                
                Row row = sheet.getRow(i);
                
                Cell cell = row.getCell(0);
                cell.setCellType(CellType.STRING);
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
                
                
                addForm.setDisplayId(display_id);
                addForm.setName(display_id);
                addForm.setParameters(paramList);
                addForm.setRole(role);
                addForm.setSequence(sequence.toLowerCase());
                
                
                
                
                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);
                
                //String part_id = rest.createPart(jsonString);
                String part_id =clotho.createPart_post(addForm, session);
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
    
    public static void populateConstructs (XSSFSheet sheet, String outputFileUrl, String username, Map<String, String> parts) {
        
        JSONObject json = new JSONObject();
        JSONObject partSearcher = new JSONObject();
        
        try {
            FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_constructsdb.fsa");
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {
                
                Row row = sheet.getRow(i);
                
                Cell cell = row.getCell(0);
                cell.setCellType(CellType.STRING);
                String display_id = "d" + cell.getStringCellValue();
                
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
                    cell.setCellType(CellType.STRING);
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
                
                //rest.createDevice(jsonString);
                clotho.createDevice_post(jsonString);
                
            }
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
