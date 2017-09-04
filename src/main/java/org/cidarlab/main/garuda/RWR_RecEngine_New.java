/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.ml.MultipleRegression;

/**
 *
 * @author mardian
 */
public class RWR_RecEngine_New {

    private static final int cluster = 2;

    private static final int num_of_parts = 49; //total unique parts
    private static final int num_of_constructs = 702;
    private static final int size_of_constructs = 6;

    private static List<Integer> healthy = new ArrayList<Integer>();

    private static List<String> list_of_parts = new ArrayList<String>();
    //private static List<Integer> list_of_idx = new ArrayList<Integer>();

    @Getter
    @Setter
    private static double[][] data = new double[num_of_constructs][num_of_parts];
    
    @Getter
    @Setter
    private static double[][] label = new double[num_of_constructs][1];
    
    @Getter
    @Setter
    private static String[] partnames = new String[num_of_parts];

    public static List<String> mRegression(String inputUrl) {
        
        String message = "Something is not right...";
        List<String> output = new ArrayList<String>();
        
        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateLabel(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix(sheet);
            
            FormatExchange.writeToCSV(data, "features.csv");
            FormatExchange.writeToCSV(FormatExchange.nDTo1DArray(label, 0), "label.csv");
            //FormatExchange.writeToCSV(partnames, "part.csv");

            MultipleRegression mReg = new MultipleRegression();
            output = mReg.pyRegression("garuda_reg.py");
            //mReg.jvRegression(data, FormatExchange.nDTo1DArray(label, 0));
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return output;
            //return "Recommendation generated!";
        }
    }

    private static void generateLabel(XSSFSheet sheet) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                label[(int) cell.getNumericCellValue() - 1][0] = 1.0;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private static void generateMatrix(XSSFSheet sheet) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                int constructIdx = (int) cell.getNumericCellValue() - 1;
                //healthy.add((int) cell.getNumericCellValue());

                for (int j = 0; j < size_of_constructs; j++) {

                    cell = row.getCell(j + 1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    if (part.equals("H2O")) {
                        continue;
                    }
                    if (!list_of_parts.contains(part)) {
                        list_of_parts.add(cell.getStringCellValue());
                    }
                    int partIdx = list_of_parts.indexOf(part);

                    data[constructIdx][partIdx] = 1.0;
                    partnames[partIdx] = list_of_parts.get(partIdx);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    ////for current usage
    public static void mRegression_simplified(String inputUrl) {
        
        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            List<Integer> ids = generateLabel_simplified(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            double[][] matrix = generateMatrix_simplified(sheet, ids);
            
            FormatExchange.writeToCSV(matrix, "features-simplified.csv");
            //FormatExchange.writeToCSV(FormatExchange.nDTo1DArray(label, 0), "label-simplified.csv");
            //FormatExchange.writeToCSV(partnames, "part.csv");

            /*MultipleRegression mReg = new MultipleRegression();
            output = mReg.pyRegression("garuda_reg.py");*/
            //mReg.jvRegression(data, FormatExchange.nDTo1DArray(label, 0));
            System.out.println("***Task done!!!");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ////for current usage
    public static void mRegression_python() {
    
        MultipleRegression mReg = new MultipleRegression();
        /*output = mReg.pyRegression("garuda_reg.py");*/
        
        double[][] data = FormatExchange.readFromCSV("features-simplified-del199.csv", 323, 49);
        double[] label = FormatExchange.nDTo1DArray(FormatExchange.readFromCSV("label-simplified-del199.csv", 323, 1), 0);
        mReg.jvRegression(data, label);
        
        System.out.println("***Task done!!!");
    }

    private static List<Integer> generateLabel_simplified(XSSFSheet sheet) {

        List<Integer> ids = new ArrayList<Integer>();
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                int id = (int) cell.getNumericCellValue();
                
                if (!ids.contains(id)) {
                    ids.add(id);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return ids;
    }

    private static double[][] generateMatrix_simplified(XSSFSheet sheet, List<Integer> ids) {

        double[][] matrix = new double[ids.size()][num_of_parts];
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                int constructIdx = (int) cell.getNumericCellValue();
                
                if (!ids.contains(constructIdx)) continue;
                
                int matrixIdx = ids.indexOf(constructIdx);
                
                //healthy.add((int) cell.getNumericCellValue());

                for (int j = 0; j < size_of_constructs; j++) {

                    cell = row.getCell(j + 1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    if (part.equals("H2O")) {
                        continue;
                    }
                    if (!list_of_parts.contains(part)) {
                        list_of_parts.add(cell.getStringCellValue());
                    }
                    int partIdx = list_of_parts.indexOf(part);

                    matrix[matrixIdx][partIdx] = 1.0;
                    partnames[partIdx] = list_of_parts.get(partIdx);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return matrix;
    }

}

