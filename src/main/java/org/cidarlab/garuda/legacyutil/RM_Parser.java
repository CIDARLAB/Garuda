/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.legacyutil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
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
public class RM_Parser {
    
    @Autowired
    static ClothoService clotho;
    
    private static Map<String, String> parts = new HashMap<>();
    private static Map<String, String> constructs_lvl1 = new HashMap<>();    
    
    public static String parse(String inputUrl, String username, HttpSession session) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                System.out.println("-----" + (i + 1) + ". " + sheetName + "-----");

                switch (sheetName) {
                    case "Glycerol":
                        populateParts(sheet, "Glycerol", username, session);
                        break;
                    case "Vectors":
                        populateParts(sheet, "Vector", username, session);
                        break;
                    case "Promoters":
                        populateParts(sheet, "Promoter", username, session);
                        break;
                    case "RBS":
                        populateParts(sheet, "RBS", username, session);
                        break;
                    case "Genes":
                        populateParts(sheet, "Gene", username, session);
                        break;
                    case "FP":
                        populateParts(sheet, "Gene", username, session);
                        break;
                    case "Terminators":
                        populateParts(sheet, "Terminator", username, session);
                        break;
                    case "Level 1":
                        populateConstructsLvl1(sheet, username);
                        break;
                    case "Level 2":
                        populateConstructsLvl2(sheet, username);
                        break;
                    default:
                        System.out.println("WARNING: Found another sheet with unregistered name!! Do nothing...");
                        break;
                }
            }
            inputFile.close();
        } catch (FileNotFoundException e) {
            return "File not found! Please enter the correct file name or url!";
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            return "Data successfully added!";
        }
    }

    public static void populateParts(XSSFSheet sheet, String role, String username, HttpSession session) {

        JSONObject json = new JSONObject();

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                
                AddForm addForm = new AddForm();
                ArrayList<Parameter> paramList = new ArrayList<>();
                
                        
                Cell cell = row.getCell(1);
                cell.setCellType(CellType.STRING);

                String display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("role", role);

                addForm.setDisplayId(display_id);
                addForm.setRole(role);
                addForm.setName(display_id);
                
                
                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //String part_id = rest.createPart(jsonString);
                String part_id = clotho.createPart_post(addForm, session);
                System.out.println(part_id);
                parts.put(display_id, part_id);
                
                addForm = new AddForm();
                paramList = new ArrayList<>();
                
                //Scar1
                cell = row.getCell(2);
                cell.setCellType(CellType.STRING);

                display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("role", "Scar");
                
                addForm.setDisplayId(display_id);
                addForm.setRole(role);
                addForm.setName(display_id);

                jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                
                //part_id = rest.createPart(jsonString);
                part_id = clotho.createPart_post(addForm, session);
                System.out.println(part_id);
                parts.put(display_id, part_id);

                addForm = new AddForm();
                paramList = new ArrayList<>();
                
                //Scar2
                cell = row.getCell(3);
                cell.setCellType(CellType.STRING);

                display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("role", "Scar");

                addForm.setDisplayId(display_id);
                addForm.setRole(role);
                addForm.setName(display_id);
                
                jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //part_id = rest.createPart(jsonString);
                part_id = clotho.createPart_post(addForm, session);
                System.out.println(part_id);
                parts.put(display_id, part_id);

            } catch (NullPointerException n) {
                continue;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void populateConstructsLvl1(XSSFSheet sheet, String username) {

        JSONObject json = new JSONObject();

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {   //last row is for H20 = empty part

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(1);
                cell.setCellType(CellType.STRING);

                String display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                int numOfParts = 5; //promoter-rbs-gene-fp-terminator, should I include scars?
                List<String> partList = new ArrayList<String>();

                /*cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String pdisplay_id = cell.getStringCellValue();

                partIds = parts.get(pdisplay_id);*/
                
                for (int j = 0; j < numOfParts; j++) {

                    cell = row.getCell(4 + j);
                    cell.setCellType(CellType.STRING);
                    String pdisplay_id = cell.getStringCellValue();

                    if (!pdisplay_id.equals("N/A")) {
                        partList.add(parts.get(pdisplay_id));
                    }

                }

                String partIds = partList.get(0);  //parts + scars
                for (int j = 1; j < partList.size(); j++) {
                    partIds = partIds + "," + partList.get(j);
                }

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("createSeqFromParts", "true");
                json.put("partIDs", partIds);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //String construct_id = rest.createDevice(jsonString);
                String construct_id = clotho.createDevice_post(jsonString);
                System.out.println(construct_id);
                constructs_lvl1.put(display_id, construct_id);

                
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        
    }

    public static void populateConstructsLvl2(XSSFSheet sheet, String username) {

        JSONObject json = new JSONObject();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            
            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(1);
                cell.setCellType(CellType.STRING);

                String display_id = cell.getStringCellValue();

                int numOfParts = 2;
                List<String> partList = new ArrayList<String>();

                for (int j = 0; j < numOfParts; j++) {

                    cell = row.getCell(4 + j);     //first cistron starts at column 1
                    cell.setCellType(CellType.STRING);
                    String pdisplay_id = cell.getStringCellValue();
                    if (!pdisplay_id.equals("N/A")) {
                        partList.add(constructs_lvl1.get(pdisplay_id));
                    }

                }
                
                String partIds = partList.get(0);  //parts + scars
                for (int j = 1; j < partList.size(); j++) {
                    partIds = partIds + "," + partList.get(j);
                }

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("createSeqFromParts", "true");
                //json.put("role", row.getCell(1).getStringCellValue());
                json.put("partIDs", partIds);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //String construct_id = rest.createDevice(jsonString);
                String construct_id = clotho.createDevice_post(jsonString);
                System.out.println(construct_id);

                
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    
}
