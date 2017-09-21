/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.garuda.Utilities;

/**
 *
 * @author mardian
 */
public class MulticollinearityCheck {

    private double[][] features;
    private double[] label;
    
    private double[][] features_reduced;
    private double[] label_reduced;
    
    private String[][] fileIndex;

    private List<Integer> col_correlated;
    private List<Integer> row_correlated;

    public MulticollinearityCheck(int num_of_constructs, int num_of_parts, double[][] features, double[] label) {

        this.features = features;
        this.label = label;
        this.fileIndex = indexing("rob-small.xlsx", num_of_constructs, 6);
        
        boolean takeMax = true;
        boolean takeAve = false;
        
        performCheck(takeMax);
        
        //fileIndexCheck();
    }

    public MulticollinearityCheck(int num_of_constructs, int num_of_parts, String features_name, String label_name) {

        this.features = Utilities.readFromCSV(features_name, num_of_constructs, num_of_parts);
        this.label = Utilities.readFromCSV(label_name, num_of_constructs);
        this.fileIndex = indexing("rob-small.xlsx", num_of_constructs, 6);
        
        boolean takeMax = false;
        boolean takeAve = false;
        
        performCheck(takeMax);
        
        //fileIndexCheck();
    }

    private void performCheck(boolean takeMax) {

        this.col_correlated = new ArrayList<Integer>();
        this.row_correlated = new ArrayList<Integer>();

        //columnCheck
        double[] firstColumn = new double[features.length];
        double[] secondColumn = new double[features.length];

        for (int j = 0; j < features[0].length; j++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int k = j + 1; k < features[0].length; k++) {
                for (int i = 0; i < features.length; i++) {
                    firstColumn[i] = features[i][j];
                    secondColumn[i] = features[i][k];
                }
                double coorelation = pc.correlation(firstColumn, secondColumn);
                if (coorelation > 0.9 || coorelation < -0.9) {
                    System.out.println("Column between: " + (j + 1) + ", and " + (k + 1) + ": " + coorelation);
                    if (!col_correlated.contains(k)) {
                        col_correlated.add(k);
                    }
                }
            }
        }

        //rowCheck
        double[] firstRow = new double[features[0].length];
        double[] secondRow = new double[features[0].length];

        for (int i = 0; i < features.length; i++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int k = i + 1; k < features.length; k++) {
                for (int j = 0; j < features[0].length; j++) {
                    firstRow[j] = features[i][j];
                    secondRow[j] = features[k][j];
                }
                double coorelation = pc.correlation(firstRow, secondRow);
                if (coorelation > 0.9 || coorelation < -0.9) {
                    
                    String indexI = "";
                    for (int t = 0; t < fileIndex[0].length; t++) {
                        indexI = indexI + "\t" + fileIndex[i][t];
                    }
                    
                    String indexK = "";
                    for (int t = 0; t < fileIndex[0].length; t++) {
                        indexK = indexK + "\t" + fileIndex[k][t];
                    }
                    
                    if (takeMax) {
                        double max = (label[i] > label[k]) ? label[i] : label[k];
                        label[i] = max;
                        label[k] = max;
                    }
                    
                    System.out.println((i+2) + "\t" + (k+2) + "\t" + label[i] + "\t" + label[k] + "\t" + (Math.abs(label[i]-label[k])) + indexI + indexK);
                    
                    if (!row_correlated.contains(k)) {
                        row_correlated.add(k);
                    }
                }
            }
        }
    }

    private void fileIndexCheck() {

        this.col_correlated = new ArrayList<Integer>();
        this.row_correlated = new ArrayList<Integer>();

        //rowCheck
        String[] firstRow = new String[fileIndex[0].length];
        String[] secondRow = new String[fileIndex[0].length];

        for (int i = 0; i < fileIndex.length; i++) {
            //PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int k = i + 1; k < fileIndex.length; k++) {
                int counter = 0;
                for (int j = 1; j < fileIndex[0].length; j++) {
                    if (fileIndex[i][j].equals(fileIndex[k][j])) {
                        //System.out.println("row " + i + ", column " + j + " equals row " + k + ", column " + j);
                        counter++;
                    }
                }
                if (counter >= 5) {
                    
                    String indexI = "";
                    for (int t = 0; t < fileIndex[0].length; t++) {
                        indexI = indexI + "\t" + fileIndex[i][t];
                    }
                    
                    String indexK = "";
                    for (int t = 0; t < fileIndex[0].length; t++) {
                        indexK = indexK + "\t" + fileIndex[k][t];
                    }
                    
                    System.out.println((i+2) + "\t" + (k+2) + indexI + indexK);
                    
                }
            }
        }
    }

    
    public void removeRows(String featuresOutput, String labelOutput) {
        
        this.features_reduced = new double[features.length - row_correlated.size()][features[0].length];
        this.label_reduced = new double[label.length - row_correlated.size()];

        //System.out.println(features.length + "    " + row_correlated.size() + "   " + label_name.length);
        int lead = 0;
        for (int i = 0; i < features.length; i++) {
            if (row_correlated.contains(i)) {
                continue;
            }
            for (int j = 0; j < features[0].length; j++) {
                features_reduced[lead][j] = features[i][j];
            }
            label_reduced[lead] = label[i];
            lead++;
        }
        Utilities.writeToCSV(features_reduced, featuresOutput);
        Utilities.writeToCSV(label_reduced, labelOutput);
        
    }

    public static String[][] indexing(String input, int rowsize, int colsize) {

        String[][] fileIndex = new String[rowsize][colsize];

        try {
            FileInputStream inputFile = new FileInputStream("resources/" + input);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            XSSFSheet sheet = workbook.getSheet("Sheet1");
            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

                Row row = sheet.getRow(i);

                for (int j = 0; j < 6; j++) {

                    Cell cell = row.getCell(j + 3);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();

                    fileIndex[i - 1][j] = part;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileIndex;
    }

    public double[][] getFeaturesRed() {
        
        double[][] output = new double[features_reduced.length][features_reduced[0].length];
        
        for (int i = 0; i < output.length; i++) {
            for (int j = 0; j < output[0].length; j++) {
                output[i][j] = features_reduced[i][j];
            }
        }
        
        return output;
    }
    
    
    public double[] getLabelRed() {
        
        double[] output = new double[label_reduced.length];
        
        for (int i = 0; i < output.length; i++) {
            output[i] = label_reduced[i];
        }
        
        return output;
    }
}
