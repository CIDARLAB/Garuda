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
public class CategoricalRecEngine {

    private String username;
    private String inputUrl;
    private String labelSheet;    //the name of the sheet where labelData data is taken from
    private String featuresSheet;    //the name of the sheet where feature data is taken from

    private int labelIdx;
    private int[] featuresIdx;

    private int num_of_parts;
    private int num_of_constructs;
    private int size_of_constructs;

    private String null_flag;   //String that is used to represent null part

    private double[][] featuresData = new double[num_of_constructs][num_of_parts];
    private double[][] labelData = new double[num_of_constructs][1];

    private List<Integer> healthy = new ArrayList<Integer>();
    private List<String> list_of_parts = new ArrayList<String>();

    private List<String> cumToxic;
    private List<Integer> cumCount;
    private int cumTotal;

    public CategoricalRecEngine(String username, String inputUrl, String labelSheet, String featuresSheet,
            int labelIdx, int[] featuresIdx, int num_of_parts, int num_of_constructs, int size_of_constructs,
            String null_flag) {

        this.username = username;
        this.inputUrl = inputUrl;
        this.labelSheet = labelSheet;
        this.featuresSheet = featuresSheet;

        this.labelIdx = labelIdx;
        this.featuresIdx = featuresIdx;

        this.num_of_parts = num_of_parts;
        this.num_of_constructs = num_of_constructs;
        this.size_of_constructs = size_of_constructs;

        this.null_flag = null_flag;

        this.featuresData = new double[num_of_constructs][num_of_parts];
        this.labelData = new double[num_of_constructs][1];

        this.cumToxic = new ArrayList<String>();
        this.cumCount = new ArrayList<Integer>();
        this.cumTotal = 0;
    }

    public String recommend(String inputUrl, String username, String fitnessSheet, String featureSheet) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(inputUrl));

            XSSFSheet sheet = workbook.getSheet(fitnessSheet);
            generateFitness(sheet);

            sheet = workbook.getSheet(featureSheet);
            generateMatrix(sheet);

            /////backpropagation algorithm from here
            int cluster = 2;

            Backpropagation backprop = new Backpropagation(labelData, labelData, cluster);
            List<Data> output = backprop.getClusterData();
            List<Integer> trainIdx = backprop.getTrainList();

            System.out.println("Backprop:");
            int row = backprop.getClusterData().size();
            int error_count = 0;
            for (int i = 0; i < row; i++) {
                String datatype = "testing";
                if (trainIdx.contains(i)) {
                    datatype = "training";
                }
                if (output.get(i).getCluster() != labelData[i][0]) {
                    error_count++;
                }
                System.out.println(output.get(i).getCluster() + "\t" + (int) labelData[i][0] + "\t" + datatype);
            }
            System.out.println("*** Accuracy = " + ((double) (row - error_count) / row * 100) + "%");

            /*NaiveBayes bpbayes = new NaiveBayes (backprop.getClusterData(), featuresData, cluster, num_of_parts);
            
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

    //recommend_expert and generateMatrix_expert are for expert system
    public String recommend_expert() {

        try {
            FileInputStream inputFile = new FileInputStream(this.inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            int range = 5;
            for (int i = 2; i < range; i++) {

                System.out.println("---------" + (i - 1) + "-th iteration");

                XSSFSheet sheet = workbook.getSheet(this.labelSheet);
                generateFitness_expert(sheet, i);

                sheet = workbook.getSheet(this.featuresSheet);
                generateMatrix_expert(sheet);
            }
            
            System.out.println("++++++++++++cumToxic contains:++++++++++");
            for (int i = 0; i < this.cumToxic.size(); i++) {
                double count = this.cumCount.get(i);
                double percentage = count/range;
                System.out.println(this.cumToxic.get(i) + "\t" + percentage);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return "Recommendation generated!";
        }
    }

    private void generateFitness(XSSFSheet sheet) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                Cell cell = row.getCell(this.labelIdx);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                labelData[i - 1][0] = ((cell.getNumericCellValue() >= 1) ? 1.0 : 0.0);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateMatrix(XSSFSheet sheet) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                Cell cell = row.getCell(0);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                int constructIdx = (int) cell.getNumericCellValue() - 1;

                for (int j = 0; j < size_of_constructs; j++) {

                    cell = row.getCell(this.featuresIdx[0]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    if (part.equals(this.null_flag)) {
                        continue;
                    }
                    if (!list_of_parts.contains(part)) {
                        list_of_parts.add(part);
                    }
                    int partIdx = list_of_parts.indexOf(part);

                    featuresData[constructIdx][partIdx] = 1.0;

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateFitness_expert(XSSFSheet sheet, int threshold) {

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                Cell cell = row.getCell(this.labelIdx);
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                labelData[i - 1][0] = ((cell.getNumericCellValue() >= threshold) ? 1.0 : 0.0);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateMatrix_expert(XSSFSheet sheet) {

        List<String> healthyParts = new ArrayList<String>();
        List<String> toxicParts = new ArrayList<String>();
        List<String> finalToxicParts = new ArrayList<String>();

        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {
                for (int j = 0; j < size_of_constructs; j++) {
                    Cell cell = row.getCell(this.featuresIdx[0]);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    if (part.equals(this.null_flag)) {
                        continue;
                    }

                    if (labelData[i - 1][0] == 1.0) {
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
            String part = toxicParts.get(j);
            if (!healthyParts.contains(part)) {
                finalToxicParts.add(part);
                
                //do cummulative
                if (!this.cumToxic.contains(part)) {
                    this.cumToxic.add(part);
                }
                int idx = this.cumToxic.indexOf(part);
                if (idx >= this.cumCount.size()) {
                    this.cumCount.add(1);
                } else {
                    this.cumCount.set(idx, this.cumCount.get(idx) + 1);
                }
                this.cumTotal++;
            }
        }

        System.out.println("Number of toxic parts: " + finalToxicParts.size());
        for (int j = 0; j < finalToxicParts.size(); j++) {
            System.out.println("(possible toxic): " + finalToxicParts.get(j));
        }

    }

}
