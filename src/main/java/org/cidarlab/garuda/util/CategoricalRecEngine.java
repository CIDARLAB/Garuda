/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.garuda.dom.Feature;
import org.cidarlab.garuda.ml.Backpropagation;
import org.cidarlab.garuda.ml.MultipleRegression;

/**
 *
 * @author mardian
 */
public class CategoricalRecEngine {

    private String username;
    private String inputUrl;
    private String labelSheet;    //the name of the sheet where label data is taken from
    private String featuresSheet;    //the name of the sheet where feature data is taken from

    private int labelIdx;
    private int[] featuresIdx;

    private int num_of_parts;
    private int num_of_constructs;
    private int size_of_constructs;

    private String null_flag;   //String that is used to represent null part

    private double[][] data = new double[num_of_constructs][num_of_parts];
    private double[][] label = new double[num_of_constructs][1];

    private List<Integer> healthy = new ArrayList<Integer>();
    private List<String> list_of_parts = new ArrayList<String>();

    private List<String> cumToxic;
    private List<Integer> cumCount;
    private int cumTotal;
    
    private int cluster;
    
    @Getter
    @Setter
    private String[] partnames;

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

        this.data = new double[num_of_constructs][num_of_parts];
        this.label = new double[num_of_constructs][1];

        this.cumToxic = new ArrayList<String>();
        this.cumCount = new ArrayList<Integer>();
        this.cumTotal = 0;
        
        this.partnames = new String[num_of_parts];
        this.cluster = 2;
    }

    public List<String> mRegression() {
        
        String message = "Something is not right...";
        List<String> output = new ArrayList<String>();
        
        try {
            FileInputStream inputFile = new FileInputStream(this.inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet(this.labelSheet);
            generateLabel(sheet);
            
            sheet = workbook.getSheet(this.featuresSheet);
            generateMatrix(sheet);
            
            FormatExchange.writeToCSV(this.data, "features.csv");
            FormatExchange.writeToCSV(FormatExchange.nDTo1DArray(this.label, 0), "label.csv");
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

    public String recommend(String inputUrl, String username, String fitnessSheet, String featureSheet) {

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(inputUrl));

            XSSFSheet sheet = workbook.getSheet(fitnessSheet);
            generateLabel(sheet);

            sheet = workbook.getSheet(featureSheet);
            generateMatrix(sheet);

            /////backpropagation algorithm from here
            int cluster = 2;

            Backpropagation backprop = new Backpropagation(label, label, cluster);
            List<Feature> output = backprop.getClusterData();
            List<Integer> trainIdx = backprop.getTrainList();

            System.out.println("Backprop:");
            int row = backprop.getClusterData().size();
            int error_count = 0;
            for (int i = 0; i < row; i++) {
                String datatype = "testing";
                if (trainIdx.contains(i)) {
                    datatype = "training";
                }
                if (output.get(i).getCluster() != label[i][0]) {
                    error_count++;
                }
                System.out.println(output.get(i).getCluster() + "\t" + (int) label[i][0] + "\t" + datatype);
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

    private void generateLabel(XSSFSheet sheet) {

        for (int i = 1; i < 15; i++) {

            Row row = sheet.getRow(i);
            
            try {
                Cell cell = row.getCell(this.labelIdx);
                
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                label[i - 1][0] = ((cell.getNumericCellValue() >= 1) ? 1.0 : 0.0);
                
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void generateMatrix(XSSFSheet sheet) {

        for (int i = 1; i < 15; i++) {

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
                    
                    //System.out.println("**Apakah masalahnya ini? " + partIdx);

                    data[constructIdx][partIdx] = 1.0;
                    partnames[partIdx] = list_of_parts.get(partIdx);

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
                label[i - 1][0] = ((cell.getNumericCellValue() >= threshold) ? 1.0 : 0.0);

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

    
    public String nnbackprop(String inputUrl, String username) {

        try {
            FileInputStream inputFile = new FileInputStream(this.inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet(this.labelSheet);
            generateLabel(sheet);
            
            sheet = workbook.getSheet(this.featuresSheet);
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

}
