/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.dom.Data;
import org.cidarlab.garuda.ml.Backpropagation;

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

    private static double[][] data = new double[num_of_constructs][num_of_parts];
    private static double[][] label = new double[num_of_constructs][1];

    public static String nnbackprop(String inputUrl, String username) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateFitness(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix(sheet);
            
            //////////
            
            for (int k = 0; k < list_of_parts.size(); k++) {
                System.out.println (k + "   " + list_of_parts.get(k));
            }

            Backpropagation backprop = new Backpropagation(data, label, cluster);   //hidden neurons = 3
            List<Data> output = backprop.getClusterData();
            List<Integer> trainIdx = backprop.getTrainList();
            
            System.out.println("Backprop:");
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
            
            List<Integer> toxicList = bpbayes.getList();
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

    //recommend2 and generateMatrix_expert are for expert system
    
    public static String expert(String inputUrl, String username) {

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Final Strains");
            generateFitness(sheet);

            sheet = workbook.getSheet("Enumerated Constructs");
            generateMatrix_expert(sheet);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "Recommendation generated!";
        }
    }

    private static void generateFitness(XSSFSheet sheet) {

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

                    if (label[i-1][0] == 1.0) {
                        if (!healthyParts.contains(part)) {
                            healthyParts.add(part);
                        }
                    } else {
                        if (!toxicParts.contains(part)) {
                            toxicParts.add(part);
                        }
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

}
