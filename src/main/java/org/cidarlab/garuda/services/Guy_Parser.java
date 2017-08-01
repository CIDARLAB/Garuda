/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.cidarlab.garuda.legacyutil.GarudaException;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
import org.cidarlab.garuda.services.ClothoService;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mardian
 */
@Service
public class Guy_Parser {
    
    static ClothoService clotho;
    
    @Autowired
    public void setClothoService(ClothoService clothoService){
        clotho = clothoService;
    }
    
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
                        instantiate (sheet, outputUrl, username, parts, session);
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
        Map jsonmap = new HashMap();
            
        try {
            //FileWriter seqFSAfile = new FileWriter(outputFileUrl + "clotho_partsdb.fsa");
            
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
//                seqFSAfile.write(">" + seqname + "\n");
//                seqFSAfile.write(sequence + "\n");
                
                json.put("displayId", display_id);
                json.put("name", display_id);
                json.put("sequence", sequence.toLowerCase());
                //json.put("length", parLength);        //will create error if additional field is added
                json.put("role", role);
                
                
                jsonmap.put("displayId", display_id);
                jsonmap.put("name", display_id);
                jsonmap.put("sequence", sequence.toLowerCase());
                jsonmap.put("role", role);                
                
                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);
                
                //String part_id = rest.createPart(jsonString);
                String part_id =clotho.createPart_post(jsonmap, session);
                System.out.println(part_id);
                
                parts.put(display_id, part_id);
            }
//            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
        
        finally {
            return parts;
        }
    }
    
    public static void populateConstructs (XSSFSheet sheet, String outputFileUrl, String username, Map<String, String> parts, HttpSession session) {
        
        JSONObject json = new JSONObject();
        JSONObject partSearcher = new JSONObject();
        
        Map jsonmap = new HashMap();
        
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
                
                partsID = "[" + partsID + "]";
                
                /*String feaname = "fea" + System.currentTimeMillis(); //sequence id is generated
                Feature.FeatureRole role = Feature.FeatureRole.TOXICITY_TEST; //default feature role
                if (row.getCell(1).getStringCellValue().equals("Toxicity Test")) {
                    role = Feature.FeatureRole.TOXICITY_TEST; //check for other types of role
                }*/
                
                json.put("name", display_id);
                json.put("displayId", display_id);
                json.put("createSeqFromParts", "true");
                //json.put("role", row.getCell(1).getStringCellValue());
                json.put("partIds", partsID);
                
                jsonmap.put("name", display_id);
                jsonmap.put("displayId", display_id);
                jsonmap.put("createSeqFromParts", "true");
                //jsonmap.put("role", row.getCell(1).getStringCellValue());
                jsonmap.put("partIds", toMap(partsID));
                

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                
                System.out.println(jsonString);
                
                //rest.createDevice(jsonString);
                clotho.createDevice_post(jsonmap, session);
                
            }
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void instantiate (XSSFSheet sheet, String outputFileUrl, String username, Map<String, String> parts, HttpSession session) {
        
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
                
                //-----sequence [col 3]-----
                /*String seq_id = "seq" + System.currentTimeMillis(); //sequence id is automatically generated
                String sequence = row.getCell(3).getStringCellValue(); //construct sequence is obtained directly from spreadsheet
                if (sequence.length()!= conLength) { //additional checking if the construct length is not equal to the provided column
                    System.out.println("Error of construct length at row " + i + "...");
                }
                Sequence newSeq = new Sequence (seq_id, "", sequence, user);*/
                //newSeq.setAnnotations(annotations);
                
                //alternatively, construct is obtained from combination of its parts
                String seqname = "seq" + System.currentTimeMillis(); //sequence id is automatically generated
        //        String sequence = "";
        //        Sequence newSeq = new Sequence (seqname, "", user);
                
                //Set<Annotation> annotations = new HashSet<Annotation>();
        //        int mark_begin = 0;
        //        int mark_end = 0;
        
                String partsID = "";
                
                for (int j=0; j<numOfParts; j++) {
                
                //    String anoname = "ann" + j + "" + System.currentTimeMillis(); //annotation id is automatically generated, somehow two annotations can be produced at the same time millis
                    
                    //check whether the part is a forward (true) or reverse strand (false)
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
                    
                    ///// querying ObjectID by part's name still does not work ///
                    //partSearcher.put("objectName", "p" + partVal);
                    //String jsonString = partSearcher.toJSONString().replaceAll("\"", "'");
                    //System.out.println("***Found part " + cc + ": " + rest.getPart(jsonString));
                    //cc++;
                    //////
                    
                    if (j==0)
                        partsID = "\"" + parts.get("p" + partVal) + "\"";
                    else
                        partsID = partsID + "," + "\"" + parts.get("p" + partVal) + "\"";
                    
                    //set the beginning and the end of the annotation, instantiate and assign the annotation
        //            String seqtemp = app.getPartsID().get(partVal-1).getSequence().getSequence();
        //            mark_end = mark_begin + seqtemp.length() - 1;
        //            Annotation anno = new Annotation(anoname, "", mark_begin, mark_end, orientation, app.getPartsID().get(partVal-1), user);
                    //anno.setFeature();
                    //annotations.add(anno);
        //            newSeq.addAnnotation(anno);
                    
                    //alternative way to obtain construct sequence
        //            sequence += seqtemp;
                //    mark_begin = mark_end + 1;
                    
                //    JSONObject annObj = anno.getJSON();
        //            Map annMap = anno.getMap();
                //    annObj.put("lengthOfAnno", seqtemp.length());
        //            String annClo = (String) clothoObject.create(annMap);
        //            if (!annClo.equals(null)) {
        //                clothoCount[0]++;
        //            }
                //    annArr.add(annObj);
                }
                
                //alternative way
                /*if (sequence.length()!= conLength) { //additional checking if the construct length is not equal to the provided column
                    System.out.println("Error of construct length at row " + i + "...");
                }*/
                //System.out.println(sequence.length());
        //        newSeq.setSequence(sequence);
                
            //    JSONObject seqObj = newSeq.getJSON();
        //        Map seqMap = newSeq.getMap();
            //    seqObj.put("counter", i);
        //        String seqClo = (String) clothoObject.create(seqMap);
        //        if (!seqClo.equals(null)) {
        //            clothoCount[1]++;
        //        }
            //    seqArr.add(seqObj);
                
                //write to FASTA file for BLAST local database
        //        seqFSAfile.write(">" + seqname + "\n");
        //        seqFSAfile.write(sequence + "\n");
                
                //feature [role = col 1]
                /*String feaname = "fea" + System.currentTimeMillis(); //sequence id is generated
                Feature.FeatureRole role = Feature.FeatureRole.TOXICITY_TEST; //default feature role
                if (row.getCell(1).getStringCellValue().equals("Toxicity Test")) {
                    role = Feature.FeatureRole.TOXICITY_TEST; //check for other types of role
                }*/
                
        //        Feature newFeature = new Feature (feaname, "", newSeq, role, user);
        //        app.getConstructsID().add(newFeature);
                
            //    JSONObject feaObj = newFeature.getJSON();
        //        Map feaMap = newFeature.getMap();
        //        String feaClo = (String) clothoObject.create(feaMap);
        //        if (!feaClo.equals(null)) {
        //            clothoCount[2]++;
        //        }
            //    feaArr.add(feaObj);
            
            
            partsID = "[" + partsID + "]";            
            
            AddForm form = new AddForm();
            
            form.setName(display_id);
            form.setDisplayId(display_id);
            form.setCreateSeqFromParts("true");
            form.setPartIds(partsID);
            form.setParameters("[]");
            form.setSequence(null);
            form.setRole(null);
            
            //System.out.println(partsID);
            
//                json.put("name", display_id);
//                json.put("displayId", display_id);
//                json.put("createSeqFromParts", "true");
//                //json.put("role", row.getCell(1).getStringCellValue());
//                json.put("partIds", jarray);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                
                System.out.println(form.toDeviceJsonString());
                String devId = clotho.createDevice_post(form.toDeviceMap(), session);
                System.out.println(devId);
                
                
            }
            
        //    System.out.println("Created " + clothoCount[0] + " Annotation objects" + "\n" +
        //                        "Created " + clothoCount[1] + " Sequence objects" + "\n" +
        //                        "Created " + clothoCount[2] + " Feature objects");
            
        /*    annJSON.put("Name", "Annotation");
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
            feaJSONfile.close();*/
            
            seqFSAfile.close();
        }
        
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static public Map toMap(String input) throws ParseException{
        
        JSONParser parser = new JSONParser();
        JSONObject map = (JSONObject) parser.parse(input);
        
        return map;    
    }
}
