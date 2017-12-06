package org.cidarlab.garuda.services;

import com.google.gson.Gson;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.Null;
import java.io.FileInputStream;
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


    public void importData(String filename, HttpSession session) throws IOException, ParseException, NullPointerException {

        this.session = session;
        this.partIds = new HashMap<>();

        FileInputStream inputFile = new FileInputStream(filename);
        XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

        XSSFSheet part_sheet = workbook.getSheet("Miniprep");
        XSSFSheet tgold_sheet = workbook.getSheet("Transformation Gold");
        XSSFSheet moclo_sheet = workbook.getSheet("MoClo Lv1");


        importParts(part_sheet);
        importDevices(tgold_sheet, moclo_sheet);


    }

    private static void importParts(XSSFSheet sheet) throws IOException, ParseException, NullPointerException {
        int num_parts = sheet.getLastRowNum();

        for (int i = 0; i < num_parts; i++){
            Row row = sheet.getRow(i);

            String params = generateAquariumPartParameters(row);
            String partName = row.getCell(3).toString();

            AddForm addForm = new AddForm();
            addForm.setName(partName);
            addForm.setParameters(params);

            Map json = addForm.toPartMap();

            String partId = clotho.createPart_post(json, session);
            System.out.println("P:"+partId);
            partIds.put(partName, partId);
        }

    }

    private static String generateAquariumPartParameters(Row row){
        String date = generateParameterString("Assembly Date", Double.parseDouble(null), row.getCell(0).toString(), null);
        String miniPlan = generateParameterString("Miniprep Plan", Double.parseDouble(null), row.getCell(1).toString(), null);
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

    private static void importDevices(XSSFSheet tgold_sheet, XSSFSheet moclo_sheet) throws ParseException, NullPointerException {
        int num_constructs = tgold_sheet.getLastRowNum();
        int num_constructs_check = moclo_sheet.getLastRowNum();

        if (num_constructs != num_constructs_check){
            System.out.println("Transformation and Assembly Sheet do not align");
            return;
        }


        for (int i = 0; i < num_constructs; i++){
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

            String params = generateAquariumDeviceParameters(tgold_row, moclo_row);
            String subParts = generateAquariumDeviceSubparts(moclo_row);

            AddForm addForm = new AddForm();
            addForm.setName(deviceName);
            addForm.setParameters(params);
            addForm.setPartIds(subParts);

            Map json = addForm.toDeviceMap();

            String deviceId = clotho.createDevice_post(json, session);
            System.out.println("D:"+deviceId);
        }
    }


    private static String generateAquariumDeviceParameters(Row t_row, Row mo_row){

        String transDate = generateParameterString("Transformation Date", Double.parseDouble(null), t_row.getCell(0).toString(), null);
        String transPlan = generateParameterString("Transformation Plan", Double.parseDouble(null), t_row.getCell(1).toString(), null);
        String transAmount = generateParameterString("Transformation Amount", t_row.getCell(3).getNumericCellValue(), null, "uL");
        String transConcentration = generateParameterString("Transformation Concentration", t_row.getCell(4).getNumericCellValue(), null, "ug/L");
        String transPlateNumber = generateParameterString("Transformation Plate Number", t_row.getCell(5).getNumericCellValue(), null, null);
        String transCheckPlatePlanNumber = generateParameterString("Transformation Check Plate Plan Number", Double.parseDouble(null), t_row.getCell(6).toString(), null);
        String transNumberTotalColonies = generateParameterString("Transformation Number Total Colonies", t_row.getCell(7).getNumericCellValue(), null, null);
        String transReactionTotal = generateParameterString("Transformation Reaction Total", t_row.getCell(8).getNumericCellValue(), null, "uL");
        String transAmountPlated = generateParameterString("Transformation Amount Plated", t_row.getCell(9).getNumericCellValue(), null, "uL");
        String transTransformantsPerMicrogramDNA = generateParameterString("Transformation Tranformants Per Microgram DNA", t_row.getCell(10).getNumericCellValue(), null, "T/ug");
        String transTransformantsPerNanogramDNA = generateParameterString("Transformation Transformants Per Nanogram DNA", t_row.getCell(11).getNumericCellValue(), null, "T/ng");
        String transTotalTimeTrans = generateParameterString("Transformation Total Time Transforming", t_row.getCell(12).getNumericCellValue(), null, "min");
        String transTotalTimeCheck = generateParameterString("Transformation Total Time Checking", t_row.getCell(13).getNumericCellValue(), null, "min");

        String assmDate = generateParameterString("Assembly Date", Double.parseDouble(null), mo_row.getCell(0).toString(), null);
        String assmPlan = generateParameterString("Assembly Plan", Double.parseDouble(null), mo_row.getCell(1).toString(), null);
        String assmEfficiency = generateParameterString("Assembly Efficiency", t_row.getCell(10).getNumericCellValue(), null, "%");
        String assmNumberWhiteColonies = generateParameterString("Assembly Number White Colonies", t_row.getCell(11).getNumericCellValue(), null, null);
        String assmNumberBlueColonies = generateParameterString("Assembly Number Blue Colonies", t_row.getCell(12).getNumericCellValue(), null, null);
        String assmTotalTimeMoclo1 = generateParameterString("Assembly Total Time MoClo Lv1", t_row.getCell(14).getNumericCellValue(), null, "min");




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

    private static String generateAquariumDeviceSubparts(Row moclo_row) {
        String subpartString = "[";

        for (int i = 3; i < 7; i++){
            subpartString += "\"";
            subpartString += moclo_row.toString();
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

}
