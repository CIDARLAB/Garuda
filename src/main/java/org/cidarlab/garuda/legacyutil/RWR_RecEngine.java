/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.legacyutil;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.model.Feature;
import org.cidarlab.garuda.machinelearning.Backpropagation;
import org.cidarlab.garuda.machinelearning.MultipleRegression;
import org.cidarlab.garuda.machinelearning.NaiveBayes;

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

    public static String nnbackprop(String inputUrl, String username) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateLabel(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix(sheet);

            //////////
            /*for (int k = 0; k < list_of_parts.size(); k++) {
                System.out.println ((k+1) + "   " + list_of_parts.get(k));
            }*/
            Backpropagation backprop = new Backpropagation(data, label, cluster);   //hidden neurons = 3
            
            double[][] wih = backprop.getWih();
            for (int i = 0; i < wih.length; i++) {
                System.out.print("+++ Part " + i + ":");
                for (int j = 0; j < wih[0].length; j++) {
                    System.out.print("\t" + wih[i][j]);
                }
                System.out.println();
            }
            
            List<Feature> output = backprop.getClusterData();
            List<Integer> trainIdx = backprop.getTrainList();

            //System.out.println("Backprop:");
            int row = backprop.getClusterData().size();
            int error_count = 0;
            for (int i = 0; i < row; i++) {
                String train = "testing";
                if (trainIdx.contains(i)) {
                    train = "training";
                }
                if (output.get(i).getCluster() != label[i][0]) {
                    error_count++;
                }
                //        System.out.println(output.get(i).getCluster() + "\t" + label[i][0] + "\t" + train);
            }
            System.out.println("*** Accuracy = " + ((double) (row - error_count) / row * 100) + "%");

            /*NaiveBayes bpbayes = new NaiveBayes (backprop.getClusterData(), data, cluster, num_of_parts);
            
            List<Integer> toxicList = bpbayes.getToxicList();
            System.out.println ("Number of toxic parts: " + toxicList.size());
            for (int i = 0; i< toxicList.size(); i++) {
                System.out.println ("(possible toxic) " + list_of_parts.get(toxicList.get(i)-1));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "Recommendation generated!";
        }
    }

    public static String naivebayes(String inputUrl, String username) {
        
        List<Feature> features = new ArrayList<Feature>();

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateLabel(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix(sheet);

            //////////
            for (int i = 0; i < num_of_constructs; i++) {
                features.add(new Feature(i, data[i], (int)label[i][0]));
            }
            
            NaiveBayes bpbayes = new NaiveBayes (features, cluster, num_of_parts);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "Recommendation generated!";
        }
    }

    public static List<String> mRegression(String inputUrl, String username) {
        
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

    //recommend2 and generateMatrix_expert are for expert system
    public static String expert(String inputUrl, String username) {

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

    private static void generateMatrix_expert(XSSFSheet sheet) {

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

    }

    ////for current usage
    public static List<String> mRegression_simplified(String inputUrl, String username) {
        
        String message = "Something is not right...";
        List<String> output = new ArrayList<String>();
        
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
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return output;
            //return "Recommendation generated!";
        }
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
