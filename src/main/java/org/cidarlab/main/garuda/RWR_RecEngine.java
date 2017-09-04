/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.ml.MultipleRegression;

/**
 *
 * @author mardian
 */
public class RWR_RecEngine {

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


    public static void mRegression(String inputUrl) {
        
        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateLabel(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix(sheet);
            
        /*    double[] data_temp = new double[num_of_constructs];
            double[] label_temp = new double[num_of_constructs];
            double[] coorelations = new double[num_of_parts];
            Map <String, String> map_coorelations = new HashMap<String, String>();
            
            for (int i = 0; i < label_temp.length; i ++) {
                label_temp[i] = label[i][0];
            }
            
            PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int j = 0; j < data[0].length; j++) {
                for (int i = 0; i < data.length; i++) {
                    data_temp[i] = data[i][j];
                }
                coorelations[j] = pc.correlation(data_temp, label_temp);
                map_coorelations.put("" + coorelations[j], "" + j);
            }
            
            Arrays.sort(coorelations);
            
            System.out.println(coorelations.length);
            for (int i = 0; i < coorelations.length; i ++) {
                System.out.println((i+1) + "\tPart " + (map_coorelations.get("" + coorelations[i])) + ": " + coorelations[i]);
            }
            */
        
            //boolean echelon = echelonMatrix(data);
            
            MultipleRegression mReg = new MultipleRegression();
            //List<String> output = mReg.pyRegression("garuda_reg.py");
            mReg.jvRegression(data, FormatExchange.nDTo1DArray(label, 0));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //recommend2 and generateMatrix_expert are for expert system
    /*public static String expert(String inputUrl) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateLabel(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix_expert(sheet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "Recommendation generated!";
        }
    }*/
    
    private static boolean echelonMatrix(double[][] data) {
        
        boolean test = false;
        
        int lead = 0;
        int rowCount = data.length;
        int columnCount = data[0].length;
        
        for (int i = 0; i < rowCount; i++) {
            if (columnCount <= lead) {
                break;
            }
            int r = i;
            while (data[r][lead] == 0) {
                r++;
                if (rowCount == r) {
                    r = i;
                    lead++;
                    if (columnCount == lead) {
                        break;
                    }
                }
            }
            double[] temp = new double[columnCount];
            for (int j = 0; j < temp.length; j++) {
                temp[j] = data[i][j];
                data[i][j] = data[r][j];
                data[r][j] = temp[j];
            }
            if (data[i][lead]!=0) {
                for (int j = 0; j < data[i].length; j++) {
                    data[i][j] = data[i][j]/data[i][lead];
                }
            }
            for (int j = 0; j < rowCount; j++) {
                if (j != i) {
                    ;
                }
            }
        }
    /*lead := 0
    rowCount := the number of rows in M
    columnCount := the number of columns in M
    for 0 ≤ r < rowCount do
        if columnCount ≤ lead then
            stop
        end if
        i = r
        while M[i, lead] = 0 do
            i = i + 1
            if rowCount = i then
                i = r
                lead = lead + 1
                if columnCount = lead then
                    stop
                end if
            end if
        end while
        Swap rows i and r
        If M[r, lead] is not 0 divide row r by M[r, lead]
        for 0 ≤ i < rowCount do
            if i ≠ r do
                Subtract M[i, lead] multiplied by row r from row i
            end if
        end for
        lead = lead + 1
    end for
end function*/
        
        
        
        return test;
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
                        list_of_parts.add(part);
                    }
                    int partIdx = list_of_parts.indexOf(part);

                    data[constructIdx][partIdx] = 1.0;
                    //partnames[partIdx] = list_of_parts.get(partIdx);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /*private static void generateMatrix_expert(XSSFSheet sheet) {

        List<String> healthyParts = new ArrayList<String>();
        List<String> toxicParts = new ArrayList<String>();
        List<String> finalToxicParts = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                for (int j = 0; j < size_of_constructs; j++) {

                    Cell cell = row.getCell(j + 1);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    if (part.equals("H2O")) {
                        continue;
                    }

                    if (label[i - 1][0] == 1.0) {
                        if (!healthyParts.contains(part)) {
                            healthyParts.add(part);
                        }
                    } else if (!toxicParts.contains(part)) {
                        toxicParts.add(part);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        for (int j = 0; j < toxicParts.size(); j++) {
            if (!healthyParts.contains(toxicParts.get(j))) {
                finalToxicParts.add(toxicParts.get(j));
            }
        }

        System.out.println("Number of toxic parts: " + finalToxicParts.size());
        for (int j = 0; j < finalToxicParts.size(); j++) {
            System.out.println("(possible toxic): " + finalToxicParts.get(j));
        }

    }*/

}
