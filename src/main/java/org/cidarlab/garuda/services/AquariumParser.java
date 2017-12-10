<<<<<<< HEAD
package org.cidarlab.garuda.services;

import com.google.gson.Gson;
=======
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
>>>>>>> garudadb_rm
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
<<<<<<< HEAD
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AquariumParser {

    static HttpSession session;
    static Map<String, String> partIds;
    static ClothoService clotho;

    @Autowired
    public void setClothoService(ClothoService clothoService){
        clotho = clothoService;
    }


    public static void importData(String filename, HttpSession session)  {

        try {

            Map<String, String> partIds = new HashMap<>();

            FileInputStream inputFile = new FileInputStream(filename);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet part_sheet = workbook.getSheet("Miniprep");
            XSSFSheet tgold_sheet = workbook.getSheet("Transformation Gold");
            XSSFSheet moclo_sheet = workbook.getSheet("MoClo Lv1");


            importParts(part_sheet, partIds, session);
            importDevices(tgold_sheet, moclo_sheet, partIds, session);


        }  catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void importParts(XSSFSheet sheet, Map<String, String> partIds, HttpSession session) throws IOException, ParseException, NullPointerException {
        int num_parts = sheet.getLastRowNum();

        // BUG IN CLOTHO: Role should be optional
        String defaultRole = "GENE";

        // getLastRowNum returning 1000? Hard code for now.
        num_parts = 15;



        for (int i = 1; i < num_parts; i++){

            Row row = sheet.getRow(i);

            String partName = row.getCell(3).toString();
            System.out.println(i+":"+partName);
            String params = generateAquariumPartParameters(row);

            AddForm addForm = new AddForm();
            addForm.setName(partName);
            addForm.setParameters(params);
            addForm.setRole(defaultRole);

            Map json = addForm.toPartMap();

            String partId = clotho.createPart_post(json, session);
            System.out.println("P:"+partId);
            partIds.put(partName, partId);
=======
import org.cidarlab.garuda.services.ClothoService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mardian
 */
@Service
public class AquariumParser {

    static ClothoService clotho;

    @Autowired
    public void setClothoService(ClothoService clothoService) {
        clotho = clothoService;
    }

    private static Map<String, String> parts = new HashMap<String, String>();
    private static Map<String, String> constructs_lvl1 = new HashMap<String, String>();

    //variables for recommendation engine part
    //private static List<Integer> healthy = new ArrayList<Integer>();
    //private static int num_of_parts;
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
                    case "Transformation Gold":
                        //scarRules(sheet);
                        //populateParts(sheet, username, session);
                        break;
                    case "Transformation Bronze":
                        //vectorParts(sheet, username, session);
                        //populateConstructsLvl1(sheet, username, session);
                        break;
                    case "Miniprep":
                        //populateConstructsLvl2(sheet, username, session);
                        break;
                    case "Gibson":
                        //generateFitness(sheet);
                        break;
                    case "MoGib":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "Sequencing":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "PCR":
                        //InitMetadata.instantiate (sheet, outputUrl, clothoObject, user);
                        break;
                    case "MoClo Lv1":
                        populateConstructs(sheet, username, session);
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
    public static void populateConstructs(XSSFSheet sheet, String username, HttpSession session) {

        JSONObject json = new JSONObject();
        Map jsonmap = new HashMap();

        List<String> roles = new ArrayList<String>();
        roles.add("Promoter");
        roles.add("RBS");
        roles.add("Gene");
        roles.add("Terminator");
        roles.add("Vector");

        List<String> uniqueParts = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            Cell cell = row.getCell(3);
            cell.setCellType(CellType.STRING);
            String cdisplay_id = cell.getStringCellValue();

            List<String> partList = new ArrayList<String>();

            //populate parts
            for (int j = 3; j < 7; j++) {

                String role = roles.get(j - 3);

                try {

                    cell = row.getCell(j);
                    cell.setCellType(CellType.STRING);

                    String pdisplay_id = cell.getStringCellValue();

                    if (uniqueParts.contains(pdisplay_id)) {
                        continue;
                    }
                    uniqueParts.add(pdisplay_id);

                    json.put("name", pdisplay_id);
                    json.put("displayId", pdisplay_id);
                    json.put("role", role.toUpperCase());

                    System.out.println(pdisplay_id + "\t" + json.get("role"));

                    String jsonString = json.toJSONString().replaceAll("\"", "'");
                    System.out.println(jsonString);

                    String part_id = clotho.createPart_post(json, session);
                    System.out.println(part_id);
                    parts.put(pdisplay_id, part_id);

                    partList.add(parts.get(pdisplay_id));

                } catch (NullPointerException n) {
                    continue;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            List<String> partIds = new ArrayList();  //parts + scars
            for (int j = 0; j < partList.size(); j++) {
                String subPartId = partList.get(j);

                if (subPartId != null) {
                    partIds.add(subPartId);
                } else {
                    continue;
                }
            }

            json.put("name", cdisplay_id);
            json.put("displayId", cdisplay_id);
            json.put("createSeqFromParts", "true");
            //json.put("role", row.getCell(1).getStringCellValue());    //no role
            json.put("partIds", partIds);

            String jsonString = json.toJSONString().replaceAll("\"", "'");
            System.out.println(jsonString);

            //String construct_id = rest.createDevice(jsonString);
            String construct_id = clotho.createDevice_post(json, session);
            System.out.println(construct_id);
            constructs_lvl1.put(cdisplay_id, construct_id);
>>>>>>> garudadb_rm
        }

    }

<<<<<<< HEAD
    private static String generateAquariumPartParameters(Row row){
        String date = generateParameterString("Assembly Date", 0.0, row.getCell(0).toString(), null);
        String miniPlan = generateParameterString("Miniprep Plan", 0.0, row.getCell(1).toString(), null);
        String lengthBp = generateParameterString("length bp", row.getCell(5).getNumericCellValue(),null, null);
        String conc = generateParameterString("Concentration", row.getCell(6).getNumericCellValue(),null, null);
        String ratio_280 = generateParameterString("A260/A280", row.getCell(7).getNumericCellValue(),null, null);
        String ratio_230 = generateParameterString("A260/A230", row.getCell(8).getNumericCellValue(),null, null);

        return "[" + date + ","
                + miniPlan + ","
                + lengthBp + ","
                + conc + ","
                + ratio_280 + ","
                + ratio_230 + "]";
    }

    private static void importDevices(XSSFSheet tgold_sheet, XSSFSheet moclo_sheet, Map<String, String> partIds, HttpSession session) throws ParseException, NullPointerException {
        int num_constructs = tgold_sheet.getLastRowNum();
        int num_constructs_check = moclo_sheet.getLastRowNum();


        // BUG IN CLOTHO: CreateSeqFromParts should be optional
        String defaultVal = "false";

        // getLastRowNum returning 1000? Hard code for now
        num_constructs = 49;
        num_constructs_check = 49;




        if (num_constructs != num_constructs_check){
            System.out.println("Transformation and Assembly Sheet do not align");
            return;
        }


        for (int i = 1; i < num_constructs; i++){
            Row tgold_row = tgold_sheet.getRow(i);
            Row moclo_row = moclo_sheet.getRow(i);

            String deviceName = moclo_row.getCell(2).toString();
            String deviceNameCheck = tgold_row.getCell(2).toString();

            if (!deviceName.equals(deviceNameCheck)){
                System.out.println("Transformation and Assembly Sheet do not align");
                System.out.println("Terminated at index: " + i);
                System.out.println(i + " of " + num_constructs + "constructed");
                return;
            }

            System.out.println(i+":"+deviceName);

            String params = generateAquariumDeviceParameters(tgold_row, moclo_row);
            String subParts = generateAquariumDeviceSubparts(moclo_row, partIds);

            AddForm addForm = new AddForm();
            addForm.setName(deviceName);
            addForm.setParameters(params);
            addForm.setPartIds(subParts);
            addForm.setCreateSeqFromParts(defaultVal);

            Map json = addForm.toDeviceMap();

            System.out.println("  :"+addForm.toDeviceJsonString());

            String deviceId = clotho.createDevice_post(json, session);
            System.out.println("D:"+deviceId);
        }
    }


    private static String generateAquariumDeviceParameters(Row t_row, Row mo_row){

        String transDate = generateParameterString("Transformation Date", 0.0, t_row.getCell(0).toString(), null);
        String transPlan = generateParameterString("Transformation Plan", 0.0, t_row.getCell(1).toString(), null);
        String transAmount = generateParameterString("Transformation Amount", t_row.getCell(3).getNumericCellValue(), null, "uL");
        String transConcentration = generateParameterString("Transformation Concentration", t_row.getCell(4).getNumericCellValue(), null, "ug/L");
        String transPlateNumber = generateParameterString("Transformation Plate Number", t_row.getCell(5).getNumericCellValue(), null, null);
        String transCheckPlatePlanNumber = generateParameterString("Transformation Check Plate Plan Number", 0.0, t_row.getCell(6).toString(), null);
        String transNumberTotalColonies = generateParameterString("Transformation Number Total Colonies", t_row.getCell(7).getNumericCellValue(), null, null);
        String transReactionTotal = generateParameterString("Transformation Reaction Total", t_row.getCell(8).getNumericCellValue(), null, "uL");
        String transAmountPlated = generateParameterString("Transformation Amount Plated", t_row.getCell(9).getNumericCellValue(), null, "uL");
        String transTransformantsPerMicrogramDNA = generateParameterString("Transformation Tranformants Per Microgram DNA", t_row.getCell(10).getNumericCellValue(), null, "T/ug");
        String transTransformantsPerNanogramDNA = generateParameterString("Transformation Transformants Per Nanogram DNA", t_row.getCell(11).getNumericCellValue(), null, "T/ng");
        String transTotalTimeTrans = generateParameterString("Transformation Total Time Transforming", t_row.getCell(12).getNumericCellValue(), null, "min");
        String transTotalTimeCheck = generateParameterString("Transformation Total Time Checking", t_row.getCell(13).getNumericCellValue(), null, "min");

        String assmDate = generateParameterString("Assembly Date", 0.0, mo_row.getCell(0).toString(), null);
        String assmPlan = generateParameterString("Assembly Plan", 0.0, mo_row.getCell(1).toString(), null);
        String assmEfficiency = generateParameterString("Assembly Efficiency", t_row.getCell(10).getNumericCellValue(), null, "%");
        String assmNumberWhiteColonies = generateParameterString("Assembly Number White Colonies", mo_row.getCell(11).getNumericCellValue(), null, null);
        String assmNumberBlueColonies = generateParameterString("Assembly Number Blue Colonies", mo_row.getCell(12).getNumericCellValue(), null, null);
        String assmTotalTimeMoclo1 = generateParameterString("Assembly Total Time MoClo Lv1", mo_row.getCell(14).getNumericCellValue(), null, "min");




        return "[" + transDate + ","
                + transPlan + ","
                + transAmount + ","
                + transConcentration + ","
                + transPlateNumber+ ","
                + transCheckPlatePlanNumber+ ","
                + transNumberTotalColonies+ ","
                + transReactionTotal+ ","
                + transAmountPlated+ ","
                + transTransformantsPerMicrogramDNA + ","
                + transTransformantsPerNanogramDNA + ","
                + transTotalTimeTrans + ","
                + transTotalTimeCheck + ","

                + assmDate + ","
                + assmPlan + ","
                + assmEfficiency + ","
                + assmNumberWhiteColonies + ","
                + assmNumberBlueColonies + ","
                + assmTotalTimeMoclo1 + "]";
    }

    private static String generateAquariumDeviceSubparts(Row moclo_row, Map<String, String> partIds) {
        String subpartString = "[";

        for (int i = 3; i < 7; i++){
            subpartString += "\"";
            subpartString += partIds.get(moclo_row.getCell(i).toString());
            subpartString += "\"";

            if (i != 6){
                subpartString += ",";
            }
        }

        subpartString += "]";

        return subpartString;
    }


    private static String generateParameterString(String name, double value, String variable, String units){
        Parameter param = new Parameter();
        Gson gson = new Gson();

        if (name.isEmpty()){
            return null;
        }

        param.setName(name);
        param.setValue(value);
        param.setVariable(variable);
        param.setUnits(units);

        return gson.toJson(param);

    }

    public static void test(){
        System.out.println("test!");
    }

=======
    public static void populateConstructsLvl1(XSSFSheet sheet, String username, HttpSession session) {

        JSONObject json = new JSONObject();

        List<String> unique = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum(); i++) {   //last row is for H20 = empty part

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(CellType.STRING);

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
                    cell.setCellType(CellType.STRING);
                    String pdisplay_id = cell.getStringCellValue();

                    if (!pdisplay_id.equals("H2O")) {
                        //System.out.println("****" + parts.get(pdisplay_id) + "   " + pdisplay_id);
                        partList.add(parts.get(pdisplay_id));

                    }

                }

                List<String> partIds = new ArrayList();  //parts + scars
                for (int j = 0; j < partList.size(); j++) {
                    String subPartId = partList.get(j);

                    if (subPartId != null) {
                        partIds.add(subPartId);
                    } else {
                        continue;
                    }
                }

                json.put("name", display_id);
                json.put("displayId", display_id);
                json.put("createSeqFromParts", "true");
                //json.put("role", row.getCell(1).getStringCellValue());    //no role
                json.put("partIds", partIds);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //String construct_id = rest.createDevice(jsonString);
                String construct_id = clotho.createDevice_post(json, session);
                System.out.println(construct_id);
                constructs_lvl1.put(display_id, construct_id);

//                
//                JSONObject query_json = new JSONObject();
//                query_json.put("objectName", pdisplay_id);
//                String query_jsonString = query_json.toJSONString().replaceAll("\"", "'");
//                System.out.print("This is from query: ");
//                //rest.getDeviceID(query_jsonString);
//                clotho.getDeviceId_get(query_jsonString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }

    public static void populateConstructsLvl2(XSSFSheet sheet, String username, HttpSession session) {

        JSONObject json = new JSONObject();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(CellType.STRING);

                String display_id = cell.getStringCellValue();

                int numOfParts = 6; //cistron 1-6
                List<String> partList = new ArrayList<String>();

                /*cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String pdisplay_id = cell.getStringCellValue();

                partIds = parts.get(pdisplay_id);*/
                for (int j = 0; j < numOfParts; j++) {

                    cell = row.getCell(1 + j);     //first cistron starts at column 1
                    cell.setCellType(CellType.STRING);
                    String pdisplay_id = cell.getStringCellValue();
                    if (!pdisplay_id.equals("H2O")) {
                        partList.add(constructs_lvl1.get(pdisplay_id));

                        System.out.println("***From hack: " + constructs_lvl1.get(pdisplay_id) + "   " + pdisplay_id);

                        //JSONObject query_json = new JSONObject();		
                        //query_json.put("pdisplay_id", pdisplay_id);		
                        //String query_jsonString = query_json.toJSONString().replaceAll("\"", "'");		
                        //System.out.println("***From search: " + rest.getPart(query_jsonString));		
                        //System.out.println("***From device: " + rest.getDevice(query_jsonString));		
                        //System.out.println("***From search ID: " + clotho.getPartById_get(session, query_jsonString) + "    " + pdisplay_id);		
                    }

                }

                List<String> partIds = new ArrayList();  //parts + scars
                for (int j = 0; j < partList.size(); j++) {
                    String subPartId = partList.get(j);

                    if (subPartId != null) {
                        partIds.add(subPartId);
                    } else {
                        continue;
                    }
                }

                json.put("name", display_id);
                json.put("displayId", display_id);
                json.put("createSeqFromParts", "true");
                //json.put("role", row.getCell(1).getStringCellValue());
                json.put("partIds", partIds);

                String jsonString = json.toJSONString().replaceAll("\"", "'");
                System.out.println(jsonString);

                //String construct_id = rest.createDevice(jsonString);-
                String construct_id = clotho.createDevice_post(json, session);
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
>>>>>>> garudadb_rm
}
