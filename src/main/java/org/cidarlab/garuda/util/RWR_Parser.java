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
    private static Map<String, String> constructs_lvl1 = new HashMap<String, String>();
    
    private static RESTRequest rest = new RESTRequest();
    
    //variables for recommendation engine part
    //private static List<Integer> healthy = new ArrayList<Integer>();

    //private static int num_of_parts;

    public static String parse(String inputUrl, String username) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {

                //Get first/desired sheet from the workbook
                XSSFSheet sheet = workbook.getSheetAt(i);
                String sheetName = sheet.getSheetName();
                System.out.println("-----" + (i + 1) + ". " + sheetName + "-----");

                switch (sheetName) {
                    case "Rules":
                        //scarRules(sheet);
                        populateParts(sheet, username);
                        break;
                    case "Parts":
                        vectorParts(sheet, username);
                        populateConstructsLvl1(sheet, username);
                        break;
                    case "Enumerated Constructs":
                        populateConstructsLvl2(sheet, username);
                        break;
                    case "Final Strains":
                        //generateFitness(sheet);
                        break;
                    case "Assemblies":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
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

    ////uncommented this method if scars are considered parts
    /*public static void scarRules(XSSFSheet sheet) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                Cell cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String promoter_id = cell.getStringCellValue();

                cell = row.getCell(10);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String scar_id = cell.getStringCellValue();

                scar_rules.put(promoter_id, scar_id);

            } catch (NullPointerException n) {
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public static void populateParts(XSSFSheet sheet, String username) {

        JSONObject json = new JSONObject();

        List<String> roles = new ArrayList<String>();
        List<Integer> roles_idx = new ArrayList<Integer>();

        Row firstRow = sheet.getRow(0);

        for (int i = 1; i < firstRow.getLastCellNum() + 1; i++) {   //skip the first column

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

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            for (int j = 0; j < roles.size(); j++) {

                String role = roles.get(j);
                int idx = roles_idx.get(j);

                try {

                    Cell cell = row.getCell(idx);
                    cell.setCellType(Cell.CELL_TYPE_STRING);

                    String display_id = cell.getStringCellValue();

                    if (unique.contains(display_id) || display_id.equals("end")) {
                        continue;
                    }
                    unique.add(display_id);

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
    }

    public static void vectorParts(XSSFSheet sheet, String username) {

        JSONObject json = new JSONObject();

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum(); i++) {   //last row is for H2O = empty part

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(1);
                cell.setCellType(Cell.CELL_TYPE_STRING);

                String display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                json.put("username", username);
                json.put("objectName", display_id);
                json.put("role", "Vector");

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

    public static void populateConstructsLvl1(XSSFSheet sheet, String username) {

        JSONObject json = new JSONObject();

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum(); i++) {   //last row is for H20 = empty part

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);

                String display_id = cell.getStringCellValue();

                if (unique.contains(display_id)) {
                    continue;
                }
                unique.add(display_id);

                int numOfParts = 5; //promoter-enzyme-ribozyme-rbs-terminator, should I include scars?
                List<String> partList = new ArrayList<String>();

                /*cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String pdisplay_id = cell.getStringCellValue();

                partIds = parts.get(pdisplay_id);*/
                
                for (int j = 0; j < numOfParts; j++) {

                    cell = row.getCell(4 + j);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String pdisplay_id = cell.getStringCellValue();

                    if (!pdisplay_id.equals("H2O")) {
                        //System.out.println("****" + parts.get(pdisplay_id) + "   " + pdisplay_id);
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
                //json.put("role", row.getCell(1).getStringCellValue());    //no role
                json.put("partIDs", partIds);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                String construct_id = rest.createDevice(jsonString);
                System.out.println(construct_id);
                constructs_lvl1.put(display_id, construct_id);
                
                
                JSONObject query_json = new JSONObject();
                query_json.put("objectName", display_id);
                String query_jsonString = query_json.toJSONString().replaceAll("\"", "'");
                System.out.println(query_jsonString);
                
                System.out.println("*****Part: " + rest.getPart(query_jsonString));
                System.out.println("*****Device: " + rest.getDevice(query_jsonString));
                
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

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_STRING);

                String display_id = cell.getStringCellValue();

                int numOfParts = 6; //cistron 1-6
                List<String> partList = new ArrayList<String>();

                /*cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String pdisplay_id = cell.getStringCellValue();

                partIds = parts.get(pdisplay_id);*/
                
                for (int j = 0; j < numOfParts; j++) {

                    cell = row.getCell(1 + j);     //first cistron starts at column 1
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String pdisplay_id = cell.getStringCellValue();
                    if (!pdisplay_id.equals("H2O")) {
                        partList.add(constructs_lvl1.get(pdisplay_id));
                        
                        System.out.println("***From hack: " + constructs_lvl1.get(pdisplay_id) + "   " + pdisplay_id);
                        
                        JSONObject query_json = new JSONObject();
                        query_json.put("objectName", pdisplay_id);
                        String query_jsonString = query_json.toJSONString().replaceAll("\"", "'");

                        System.out.println("***From search: " + rest.getPart(query_jsonString));
                        System.out.println("***From device: " + rest.getDevice(query_jsonString));
                        
                        System.out.println("***From search ID: " + rest.getPartID(query_jsonString) + "    " + pdisplay_id);
                        
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

                String construct_id = rest.createDevice(jsonString);
                System.out.println(construct_id);

                
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }
    
    /*public static void generateFitness(XSSFSheet sheet) {
        
        generateList(sheet);
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            
            Row row = sheet.getRow(i);
            
            try {
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                healthy.add((int) cell.getNumericCellValue());

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
    
    private static void generateList(XSSFSheet sheet) {
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            
            Row row = sheet.getRow(i);
            
            try {
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                healthy.add((int) cell.getNumericCellValue());

            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }*/
}
