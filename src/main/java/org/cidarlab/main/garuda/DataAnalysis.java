/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.dom.Construct;
import org.cidarlab.main.dom.Feature;
import org.cidarlab.main.dom.Part;
import org.cidarlab.main.dom.Vector;
import org.cidarlab.main.ml.Backpropagation;
import org.cidarlab.main.ml.KMeansClustering;
import org.cidarlab.main.ml.NaiveBayes;

/**
 *
 * @author mardian
 */
public class DataAnalysis {

    private int num_of_constructs;
    private int constructs_reduced;
    private int num_of_parts;

    private String[] partnames;

    @Getter
    private double[][] features;
    
    @Getter
    private double[][] features_reduced;    //removing the same rows
    private double[][] features_sampled;    //for training data

    @Getter
    private double[] label;
    private double[] label_bin;
    
    @Getter
    private double[] label_reduced;
    private double[] label_sampled;

    private double[] parts_ave;
    private double[] parts_std;

    private boolean print;

    private List<Construct> constructs;
    private List<Part> parts;

    private List<Part> results;
    private List<Integer> testData;
    private List<Vector> center;
    private double thres;

    public DataAnalysis(int constructs, int parts, boolean print) {

        this.constructs = new ArrayList<Construct>();
        this.parts = new ArrayList<Part>();

        this.num_of_constructs = constructs;
        this.num_of_parts = parts;

        this.partnames = null;
        this.features = null;
        this.features_reduced = null;
        this.features_sampled = null;
        this.label = null;
        this.label_bin = null;
        this.label_reduced = null;
        this.label_sampled = null;

        this.parts_ave = null;
        this.parts_std = null;

        this.print = print;

        this.results = null;
        this.testData = null;
        this.center = null;
        this.thres = 0.0;
    }

    public void parseFeatures(String input, String output) {

        try {

            FileInputStream inputFile = new FileInputStream("resources/" + input);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);

            generateMatrix(workbook.getSheet("Sheet1"));
            if (print) {
                Utilities.writeToCSV(features, output);
            }

            /*System.out.println("*****CONSTRUCTS*****");
            for (int i = 0; i < constructs.size(); i++) {
                Construct c = constructs.get(i);
                System.out.print((i + 1) + "\t" + c.getName() + "\t");
                List<String> parts = c.getParts();
                for(int j = 0; j < parts.size(); j++) {
                    System.out.print(parts.get(j) + "\t");
                }
                System.out.println(c.getPlate_pos());
            }
            System.out.println("*****PARTS*****");
            for (int i = 0; i < parts.size(); i++) {
                Part p = parts.get(i);
                System.out.println((i + 1) + "\t" + p.getName() + "\t" + p.getAssoc_idx());
            }
            System.out.println("*****PARTNAMES*****");
            for (int i = 0; i < partnames.length; i++) {
                System.out.println((i + 1) + "\t" + partnames[i]);
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateMatrix(XSSFSheet sheet) {

        this.features = new double[num_of_constructs][num_of_parts];
        List<String> allparts = new ArrayList<String>();
        this.partnames = new String[num_of_parts];

        try {

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) { //row

                Row row = sheet.getRow(i);
                List<String> cparts = new ArrayList<String>();

                Cell cell = row.getCell(3);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String cname = cell.getStringCellValue();

                for (int j = 0; j < 5; j++) { //column

                    cell = row.getCell(j + 4);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();

                    if (part.equals("")) {
                        continue;
                    }

                    cparts.add(part);                   //list of parts for the current construct
                    if (!allparts.contains(part)) {
                        allparts.add(part);             //list of parts
                        parts.add(new Part(part, allparts.size() - 1));
                    }
                    int partIdx = allparts.indexOf(part);

                    features[i - 1][partIdx] = 1.0;
                }
                
                cell = row.getCell(9);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String pos = cell.getStringCellValue();

                cell = row.getCell(11);
                cell.setCellType(Cell.CELL_TYPE_STRING);
                pos += "_" + cell.getStringCellValue();

                constructs.add(new Construct(cname, cparts, pos));

            }
            allparts.toArray(this.partnames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readLabel(String input) {

        try {
            this.label = Utilities.readFromCSV(input, num_of_constructs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void average_growth(String outputFile, double[][] features, double[] label) {

        for (int j = 0; j < num_of_parts; j++) {
            
            boolean first = true;
            double total = 0.0;
            int count = 0;
            double min = 0.0;
            double max = 0.0;
            
            for (int i = 0; i < features.length; i++) {
                if (features[i][j] == 1.0) {
                    if (first) {
                        min = label[i];
                        max = label[i];
                        first = false;
                    } else {
                        if (label[i] < min) {
                            min = label[i];
                        }
                        if (label[i] > max) {
                            max = label[i];
                        }
                    }
                    total += label[i];
                    count++;
                }
            }
            parts.get(j).setAve_growth(total / count);
            parts.get(j).setMin(min);
            parts.get(j).setMax(max);

            double ss = 0.0;
            for (int i = 0; i < features.length; i++) {
                if (features[i][j] == 1.0) {
                    ss = ss + Math.pow((label[i] - parts.get(j).getAve_growth()), 2);
                }
            }
            parts.get(j).setStd_dev(Math.sqrt(ss / (count - 1)));
        }

        if (print) {
            //write into CSV file
            String[] container = new String[7];
            //first line
            String line = "Index,";
            for (int j = 0; j < num_of_parts; j++) {
                line += (j + 1) + ",";
            }
            container[0] = line.substring(0, line.length() - 1);
            //second line
            line = "Partname,";
            for (int j = 0; j < num_of_parts; j++) {
                line += parts.get(j).getName() + ",";
            }
            container[1] = line.substring(0, line.length() - 1);
            //third line
            container[2] = "";
            //fourth line
            line = "AVERAGE,";
            for (int j = 0; j < num_of_parts; j++) {
                line += parts.get(j).getAve_growth() + ",";
            }
            container[3] = line.substring(0, line.length() - 1);
            //fifth line
            line = "STDEV,";
            for (int j = 0; j < num_of_parts; j++) {
                line += parts.get(j).getStd_dev() + ",";
            }
            container[4] = line.substring(0, line.length() - 1);
            //sixth line
            line = "MIN,";
            for (int j = 0; j < num_of_parts; j++) {
                line += parts.get(j).getMin() + ",";
            }
            container[5] = line.substring(0, line.length() - 1);
            //seventh line
            line = "MAX,";
            for (int j = 0; j < num_of_parts; j++) {
                line += parts.get(j).getMax() + ",";
            }
            container[6] = line.substring(0, line.length() - 1);

            Utilities.writeToCSV(container, outputFile);
        }

    }

    public void multicollinearity(String featuresOutput, String labelOutput) {

        if (features == null && label == null) {

            System.out.println("Either features or label is NULL! Terminating...");
            return;
        }

        //multicollinearity
        boolean takeMax = true;
        boolean takeAve = false;
        
        List<Integer> col_correlated = new ArrayList<Integer>();
        List<Integer> row_correlated = new ArrayList<Integer>();

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
                    
                    if (!row_correlated.contains(k)) {
                        row_correlated.add(k);
                    }
                    
                    Construct c = constructs.get(i);
                    String indexI = c.getName() + "\t" + c.getPlate_pos() + "\t" + c.printParts();
                    c = constructs.get(k);
                    String indexK = c.getName() + "\t" + c.getPlate_pos() + "\t" + c.printParts();
                    
                    if (takeMax) {
                        double max = (label[i] > label[k]) ? label[i] : label[k];
                        label[i] = max;
                        label[k] = max;
                    }
                    
                    if (takeAve) {
                        double ave = (label[i] + label[k]) / 2;
                        label[i] = ave;
                        label[k] = ave;
                    }
                    
                    System.out.println((i+2) + "\t" + (k+2) + "\t" + label[i] + "\t" + label[k] + "\t" + (Math.abs(label[i]-label[k])) + "\t" + indexI + "\t" + indexK);
                }
            }
        }
        
        features_reduced = Utilities.removeRows(features, row_correlated);
        label_reduced = Utilities.removeRows(label, row_correlated);
        constructs_reduced = this.features_reduced.length;

    }
   
    /*public void removeRows(List<Integer> removedRows) {
        
        this.features_reduced = new double[features.length - removedRows.size()][features[0].length];
        this.label_reduced = new double[label.length - removedRows.size()];
        
        int lead = 0;
        for (int i = 0; i < features.length; i++) {
            if (removedRows.contains(i)) {
                continue;
            }
            for (int j = 0; j < features[0].length; j++) {
                features_reduced[lead][j] = features[i][j];
            }
            label_reduced[lead] = label[i];
            lead++;
        }
    }*/

    public void runKMeansNaiveBayes(boolean sampled, int numOfTest) {

        testData = generateTestData(numOfTest);
        results = new ArrayList<Part>();
        double[][] label_2D = null;

        //clustering part
        if (sampled) {
            features_sampled = Utilities.removeRows(features_reduced, testData); //creating features_sampled and label_sampled
            label_sampled = Utilities.removeRows(label_reduced, testData);
            
            label_2D = Utilities._1DTonDArray(label_sampled);
        } else {
            
            label_2D = Utilities._1DTonDArray(label_reduced);
        }
        KMeansClustering kmeans = new KMeansClustering(label_2D, 2);
        int[] cluster = kmeans.getListCluster();

        //compare centroids for labeling and compute threshold between clusters
        center = kmeans.getCentroids();
        boolean flip = false;
        double cent0 = center.get(0).getDimension(0);
        double cent1 = center.get(1).getDimension(0);
        if (cent0 > cent1) {
            thres = ((cent0 - cent1) / 2) + cent1;
            flip = true;
        } else {
            thres = ((cent1 - cent0) / 2) + cent0;
        }
        System.out.println("==threshold: " + thres);

        //naive bayes part
        List<Feature> features_loc = null;
        if (sampled) {
            features_loc = Utilities.arrayToFeature(features_sampled, cluster);
        } else {
            features_loc = Utilities.arrayToFeature(features_reduced, cluster);
        }
        NaiveBayes nb = new NaiveBayes(features_loc, 2, num_of_parts);

        List<Feature> classList = nb.getClassList();

        for (int j = 0; j < classList.size(); j++) {
            int factor = 0;
            if (classList.get(j).getCluster() == 0) {
                factor = -1;
                if (flip) {
                    factor = 1;
                }
            } else if (classList.get(j).getCluster() == 1) {
                factor = 1;
                if (flip) {
                    factor = -1;
                }
            }
            int idx = classList.get(j).getId();
            double probability = classList.get(j).getVector().getDimension(0) * factor;
            Part newpart = parts.get(idx);
            results.add(new Part(newpart.getName(), newpart.getAve_growth(), newpart.getStd_dev(), probability));

        }

        makeplot(testData);

        System.out.println("***Task done with K-Means/Bayes!!!");
    }

    public void makeplot(List<Integer> testData) {

        System.out.println("***MAKEPLOT***");

        String partstring = "";
        String growth = "";
        String stdev = "";

        String prob_pos = "";
        String prob_neg = "";

        //sort by growth rate
        Collections.sort(results, Part.Comparators.labelComparator);
        Map<String, Double> part_prob = new HashMap<String, Double>();

        for (int i = results.size() - 1; i >= 0; i--) {

            partstring = partstring + results.get(i).getName() + ",";
            growth = growth + results.get(i).getAve_growth() + ",";
            stdev = stdev + results.get(i).getStd_dev() + ",";

            double prob = results.get(i).getProbability();
            if (prob >= 0) {
                prob_pos = prob_pos + prob + ",";
                prob_neg = prob_neg + "0.0,";
            } else {
                prob_pos = prob_pos + "0.0,";
                prob_neg = prob_neg + prob + ",";
            }

            part_prob.put(results.get(i).getName(), results.get(i).getProbability());
            System.out.println(results.get(i).getName() + "\t" + results.get(i).getAve_growth() + "\t" + results.get(i).getProbability());
        }

        //remove the last comma
        partstring = partstring.substring(0, partstring.length() - 1);
        growth = growth.substring(0, growth.length() - 1);
        stdev = stdev.substring(0, stdev.length() - 1);

        prob_pos = prob_pos.substring(0, prob_pos.length() - 1);
        prob_neg = prob_neg.substring(0, prob_neg.length() - 1);

        System.out.println("===GROWTH CURVE===");
        System.out.println("Label: " + partstring);
        System.out.println("Growth: " + growth);
        System.out.println("Error Bar: " + stdev);

        System.out.println("===RECOMMENDATION===");
        System.out.println("Label: " + partstring);
        System.out.println("Good: " + prob_pos);
        System.out.println("Bad: " + prob_neg);

        //print test data
        //List<Double> fitnesses = new ArrayList<Double>();
        String partpy = "";
        String predpy = "";
        String realpy = "";

        for (int i = 0; i < testData.size(); i++) {
            System.out.print((testData.get(i) + 1) + ": ");
            double fitness = 0.0;
            for (int j = 0; j < features_reduced[0].length; j++) {
                if (features_reduced[testData.get(i)][j] == 1) {
                    System.out.print(parts.get(j).getName() + " ");
                    fitness += part_prob.get(parts.get(j).getName());
                    //partpy = partpy + partnames[i] + "_";
                }
            }
            //partpy = partpy.substring(0, partpy.length()-1) + ",";
            partpy = partpy + (testData.get(i) + 1) + ",";
            predpy = predpy + fitness + ",";
            realpy = realpy + label_reduced[testData.get(i)] + ",";

            //fitnesses.add(fitness);
            System.out.println("= " + fitness + " versus " + label_reduced[testData.get(i)]);
        }

        /*double fitness_min = fitnesses.get(0);
        double fitness_max = fitnesses.get(0);
        for (int i = 1; i < fitnesses.size(); i++) {
            if (fitnesses.get(i) < fitness_min) {
                fitness_min = fitnesses.get(i);
            }
            if (fitnesses.get(i) > fitness_max) {
                fitness_max = fitnesses.get(i);
            }
        }
        List<Double> fitnesses_norm = new ArrayList<Double>();
        for (int i = 0; i < fitnesses.size(); i++) {
            double new_fitness = (fitnesses.get(i) - fitness_min) / (fitness_max - fitness_min);
            fitnesses_norm.add(new_fitness);
            //predpy = predpy + new_fitness + ",";
        }*/
        partpy = partpy.substring(0, partpy.length() - 1);
        predpy = predpy.substring(0, predpy.length() - 1);
        realpy = realpy.substring(0, realpy.length() - 1);

        /*for (int i = 0; i < center.size(); i++) {
            System.out.println(center.get(i));
        }*/
        //arguments for python script
        List<String> arguments = new ArrayList<String>();

        arguments.add(partpy);
        arguments.add(predpy);
        arguments.add(realpy);

        arguments.add(partstring);
        arguments.add(prob_pos);
        arguments.add(prob_neg);
        arguments.add(growth);
        arguments.add(stdev);

        arguments.add(thres + "");

        System.out.println(partpy);
        System.out.println(predpy);
        System.out.println(realpy);

        PythonRunner pr = new PythonRunner("make_barplot.py", arguments);

    }

    public List<Integer> generateTestData(int numOfTest) {

        List<Integer> test = new ArrayList<>();     //list of randomly picked random genes
        List<Integer> training = new ArrayList<>();

        //generate training of numbers from 1 to number of reduced rows
        for (int i = 0; i < constructs_reduced; i++) {
            training.add(i);
        }

        //randomly pick numbers from the training as toxic genes
        for (int i = 0; i < numOfTest; i++) {
            test.add(training.remove((int) (Math.random() * training.size())));
        }

        return test;
    }

    /*public void reducedRawPart() {

        String[][] fileIndex = new String[features_reduced.length][6];

        for (int i = 0; i < features_reduced.length; i++) {
            int lead = 0;
            for (int j = 0; j < features_reduced[0].length; j++) {
                if (features_reduced[i][j] == 1) {
                    fileIndex[i][lead] = partnames[j];
                    lead++;
                }
            }
            fileIndex[i][5] = label_reduced[i] + "";
        }

        Utilities.writeToCSV(fileIndex, "rob-reduced.csv");
    }*/

    public void subsetMatrix(List<Integer> removedRows) {

        this.features_sampled = new double[features_reduced.length - removedRows.size()][features_reduced[0].length];
        this.label_sampled = new double[label_reduced.length - removedRows.size()];

        int lead = 0;
        for (int i = 0; i < features_reduced.length; i++) {
            if (removedRows.contains(i)) {
                continue;
            }
            for (int j = 0; j < features_reduced[0].length; j++) {
                features_sampled[lead][j] = features_reduced[i][j];
            }
            label_sampled[lead] = label_reduced[i];
            lead++;
        }
    }

    /*public static String[][] indexing(String input, int rowsize, int colsize) {

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
                    //partnames[partIdx] = allparts.get(partIdx);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileIndex;
    }*/

    ////for current usage
    public void mRegression_python(boolean sampled, int numOfSample, boolean generateFile) {

        results = new ArrayList<Part>();

        if (generateFile) {
            testData = generateTestData(numOfSample);

            //clustering part
            if (sampled) {
                subsetMatrix(testData);
                Utilities.writeToCSV(features_sampled, "features_sampled_regression.csv");
                Utilities.writeToCSV(label_sampled, "label_sampled_regression.csv");
            } else {
                Utilities.writeToCSV(features_sampled, "features_unsampled_regression.csv");
                Utilities.writeToCSV(label_sampled, "label_unsampled_regression.csv");
            }

            String[] testDataString = new String[testData.size()];
            for (int i = 0; i < testDataString.length; i++) {
                testDataString[i] = testData.get(i) + "";
            }
            Utilities.writeToCSV(testDataString, "testData_python.csv");

            System.out.println("***Generating csv files for Regression!!!");
        } else {

            List<Integer> testDataFromFile = new ArrayList<Integer>();

            double[] testDataDouble = Utilities.readFromCSV("testData_python.csv", numOfSample);
            for (int i = 0; i < testDataDouble.length; i++) {
                testDataFromFile.add((int) testDataDouble[i]);
            }

            double[] coefficients = Utilities.readFromCSV("params.csv", (num_of_parts + 1)); //+1 for intercept
            double[] probabilities = Utilities.readFromCSV("pvalues.csv", (num_of_parts + 1));

            for (int i = 1; i < probabilities.length; i++) {
                int factor = 0;
                if (coefficients[i] >= 0) {
                    factor = 1;
                } else {
                    factor = -1;
                }
                results.add(new Part(partnames[i - 1], parts_ave[i - 1], parts_std[i - 1], factor * probabilities[i], coefficients[i]));
            }

            makeplotRegression(testDataFromFile, coefficients[0]);
        }

        /*MultipleRegression mReg = new MultipleRegression();
        List<String> output = mReg.pyRegression("garuda.py");*/
 /*double[][] data = Utilities.readFromCSV("features-simplified-del199.csv", 323, 49);
        double[] label = Utilities.nDTo1DArray(Utilities.readFromCSV("label-simplified-del199.csv", 323, 1), 0);
        mReg.jvRegression(data, label);*/
    }

    /*public void featureSort() {
        List<Part> allparts = new ArrayList<Part>();
        
        allparts.add(new Part(0, "part1", new double[]{0.1, 0.2, 0.3}, 0.7, 0));
        allparts.add(new Feature(1, "part2", new double[]{0.2, 0.1, 0.3}, 0.5, 0));
        allparts.add(new Feature(2, "part3", new double[]{0.2, 0.3, 0.1}, 0.2, 0));
        allparts.add(new Feature(3, "part4", new double[]{0.3, 0.1, 0.2}, 0.9, 0));
        
        Collections.sort(features_loc, Feature.Comparators.labelComparator);
        
        for (int i = 0; i < features_loc.size(); i++) {
            System.out.println (features_loc.get(i).getName());
        }
    }*/
    public int runBackprop(boolean sampled, int numOfSample) {

        //testData = generateTestData(numOfSample);
        results = new ArrayList<Part>();

        //clustering part
        /*if (sampled) {
            subsetMatrix(testData);            //creating features_sampled and label_sampled
            label_2D = Utilities._1DTonDArray(label_sampled);
            features_loc = features_sampled;
        }
        else {
            label_2D = Utilities._1DTonDArray(label_reduced);
            features_loc = features_reduced;
        }*/
        Backpropagation bp = new Backpropagation(features_reduced, Utilities._1DTonDArray(label_reduced), 3, features_reduced.length, numOfSample);

        testData = bp.getTestList();
        List<Feature> clusterData = bp.getClusterData();

        /*System.out.println("***Cluster Data***");
        for (int i = 0; i < clusterData.size(); i++) {
            System.out.println(clusterData.get(i).getCluster() +  "\t" + clusterData.get(i).getLabel());
        }*/
        int counter = 0;
        for (int i = 0; i < testData.size(); i++) {
            int idx = testData.get(i);
            //System.out.println((idx + 1) + ":\t" + ((int) label_reduced[idx]) + "\t" + clusterData.get(idx).getCluster());
            if ((int) label_reduced[idx] == clusterData.get(idx).getCluster()) {
                counter++;
            }
        }
        //System.out.println(counter);

        return counter;
    }

    public void makeplotRegression(List<Integer> testData, double intercept) {

        System.out.println("***MAKEPLOT***");

        String partstring = "";
        String growth = "";
        String stdev = "";

        String prob_pos = "";
        String prob_neg = "";

        //sort by growth rate
        Collections.sort(results, Part.Comparators.labelComparator);
        Map<String, Double> part_prob = new HashMap<String, Double>();

        for (int i = results.size() - 1; i >= 0; i--) {

            partstring = partstring + results.get(i).getName() + ",";
            growth = growth + results.get(i).getAve_growth() + ",";
            stdev = stdev + results.get(i).getStd_dev() + ",";

            double prob = results.get(i).getProbability();
            if (prob >= 0) {
                prob_pos = prob_pos + prob + ",";
                prob_neg = prob_neg + "0.0,";
            } else {
                prob_pos = prob_pos + "0.0,";
                prob_neg = prob_neg + prob + ",";
            }

            part_prob.put(results.get(i).getName(), results.get(i).getCoefficient());
            System.out.println(results.get(i).getName() + "\t" + results.get(i).getAve_growth() + "\t" + results.get(i).getProbability());
        }

        //remove the last comma
        partstring = partstring.substring(0, partstring.length() - 1);
        growth = growth.substring(0, growth.length() - 1);
        stdev = stdev.substring(0, stdev.length() - 1);

        prob_pos = prob_pos.substring(0, prob_pos.length() - 1);
        prob_neg = prob_neg.substring(0, prob_neg.length() - 1);

        System.out.println("===GROWTH CURVE===");
        System.out.println("Label: " + partstring);
        System.out.println("Growth: " + growth);
        System.out.println("Error Bar: " + stdev);

        System.out.println("===RECOMMENDATION===");
        System.out.println("Label: " + partstring);
        System.out.println("Good: " + prob_pos);
        System.out.println("Bad: " + prob_neg);

        //print test data
        //List<Double> fitnesses = new ArrayList<Double>();
        String partpy = "";
        String predpy = "";
        String realpy = "";

        for (int i = 0; i < testData.size(); i++) {
            System.out.print((testData.get(i) + 1) + ": ");
            double fitness = 0.0;
            for (int j = 0; j < features_reduced[0].length; j++) {
                if (features_reduced[testData.get(i)][j] == 1) {
                    System.out.print(partnames[j] + " ");
                    fitness += part_prob.get(partnames[j]);
                    //partpy = partpy + partnames[i] + "_";
                }
            }
            //partpy = partpy.substring(0, partpy.length()-1) + ",";
            partpy = partpy + (testData.get(i) + 1) + ",";
            predpy = predpy + (fitness + intercept) + ",";
            realpy = realpy + label_reduced[testData.get(i)] + ",";

            //fitnesses.add(fitness);
            System.out.println("= " + fitness + " versus " + label_reduced[testData.get(i)]);
        }

        /*double fitness_min = fitnesses.get(0);
        double fitness_max = fitnesses.get(0);
        for (int i = 1; i < fitnesses.size(); i++) {
            if (fitnesses.get(i) < fitness_min) {
                fitness_min = fitnesses.get(i);
            }
            if (fitnesses.get(i) > fitness_max) {
                fitness_max = fitnesses.get(i);
            }
        }
        List<Double> fitnesses_norm = new ArrayList<Double>();
        for (int i = 0; i < fitnesses.size(); i++) {
            double new_fitness = (fitnesses.get(i) - fitness_min) / (fitness_max - fitness_min);
            fitnesses_norm.add(new_fitness);
            //predpy = predpy + new_fitness + ",";
        }*/
        partpy = partpy.substring(0, partpy.length() - 1);
        predpy = predpy.substring(0, predpy.length() - 1);
        realpy = realpy.substring(0, realpy.length() - 1);

        /*for (int i = 0; i < center.size(); i++) {
            System.out.println(center.get(i));
        }*/
        //arguments for python script
        List<String> arguments = new ArrayList<String>();

        arguments.add(partpy);
        arguments.add(predpy);
        arguments.add(realpy);

        arguments.add(partstring);
        arguments.add(prob_pos);
        arguments.add(prob_neg);
        arguments.add(growth);
        arguments.add(stdev);

        arguments.add(thres + "");

        System.out.println(partpy);
        System.out.println(predpy);
        System.out.println(realpy);

        PythonRunner pr = new PythonRunner("make_barplot.py", arguments);

    }

    public void runKMeansExpert(String data, String label) {

        int row = 354;
        int column = 15;

        String[] partnames = new String[]{
            "DC-01", "RHa-36", "AcT-09", "RHa-51", "AcT-11", "NMT-05", "NMT-17", "DC-02",
            "AAF-20", "AAF-27", "AcT-05", "RHa-18", "DC-06", "AAF-18", "NMT-13"
        };
        List<Integer> goodparts = new ArrayList<Integer>();

        double[][] data_arr = Utilities.readFromCSV(data, row, column);
        double[][] label_2d = Utilities.readFromCSV(label, row, 1);

        KMeansClustering kmeans = new KMeansClustering(label_2d, 2);

        int[] cluster = kmeans.getListCluster();

        int total = 0;
        for (int i = 0; i < cluster.length; i++) {
            if (cluster[i] == 1) {
                total++;
                for (int j = 0; j < data_arr[0].length; j++) {
                    if (data_arr[i][j] == 1.0 && !goodparts.contains(j)) {
                        goodparts.add(j);
                    }
                }
            }
            System.out.println(cluster[i] + "    " + label_2d[i][0]);
        }

        System.out.println("Total good constructs: " + total + ", and bad constructs: " + (cluster.length - total));

        System.out.println("*****List of good parts:");
        for (int k = 0; k < goodparts.size(); k++) {
            System.out.println(partnames[goodparts.get(k)]);
        }

    }

    public void runNaiveBayes(String data, String label) {

        int row = 354;
        int column = 15;
        //double threshold = 0.1;
        String[] partnames = new String[]{
            "DC-01", "RHa-36", "AcT-09", "RHa-51", "AcT-11", "NMT-05", "NMT-17", "DC-02",
            "AAF-20", "AAF-27", "AcT-05", "RHa-18", "DC-06", "AAF-18", "NMT-13"
        };
        double[] growth = new double[]{
            0.609582398, 0.695577196, 0.662406204, 0.712407217, 0.689552324,
            0.700538101, 0.778724162, 0.6573707, 0.827001054, 0.662825018,
            0.712291276, 0.713080792, 0.869661337, 0.614503912, 0.829831316
        };
        double[][] probabilities = new double[partnames.length][9];

        double[][] data_arr = Utilities.readFromCSV(data, row, column);
        double[] label_arr = Utilities.nDTo1DArray(Utilities.readFromCSV(label, row, 1), 0);

        for (int threshold = 1; threshold < 10; threshold++) {

            List<Feature> features = Utilities.arrayToFeature(data_arr, label_arr, (double) threshold / 10);
            NaiveBayes nb = new NaiveBayes(features, 2, column);

            /*List<Integer> list = nb.getToxicList();

            System.out.println("List size: " + list.size());
            for (int i = 0; i < list.size(); i++) {
                System.out.println(i + ". " + list.get(i));
            }*/
            List<Feature> classList = nb.getClassList();

            //System.out.println ("===threshold: " + threshold);
            //printing
            for (int i = 0; i < nb.getClasses(); i++) {
                //System.out.println("***Toxicity " + i + " contains: ");
                for (int j = 0; j < classList.size(); j++) {
                    if (classList.get(j).getCluster() == i) {
                        if (i == 0) {
                            int idx = classList.get(j).getId();
                            double probability = classList.get(j).getVector().getDimension(0) * -1;
                            //System.out.println(growth[idx] + "\t" + partnames[idx] + "\t\t" + probabilities);
                            probabilities[j][(int) (threshold - 1)] = probability;
                        } else if (i == 1) {
                            int idx = classList.get(j).getId();
                            double probability = classList.get(j).getVector().getDimension(0);
                            //System.out.println(growth[idx] + "\t" + partnames[idx] + "\t" + probabilities);
                            probabilities[j][(int) (threshold - 1)] = probability;
                        }
                    }
                    //add to the toxic list
                    /*if (classList.get(i).getCluster() == 0) {
                        toxicList.add(classList.get(i).getId() + 1);
                    }*/
                }
            }
        }

        for (int i = 0; i < probabilities.length; i++) {
            System.out.println(partnames[i]);
            for (int j = 0; j < probabilities[0].length; j++) {
                System.out.print(probabilities[i][j] + ", ");
            }
            System.out.println();
        }

        /*System.out.println("Part: " + partnames.length);
        for (int i = 0; i < partnames.length; i++) {
            System.out.println((i + 1) + ". " + partnames[i]);
        }*/
    }

}
