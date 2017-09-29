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
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.dom.Feature;
import org.cidarlab.main.dom.Part;
import org.cidarlab.main.dom.Vector;
import org.cidarlab.main.ml.KMeansClustering;
import org.cidarlab.main.ml.MulticollinearityCheck;
import org.cidarlab.main.ml.MultipleRegression;
import org.cidarlab.main.ml.NaiveBayes;

/**
 *
 * @author mardian
 */
public class DataAnalysis {
    
    private int constructs;
    private int constructs_reduced;
    private int parts;
    
    private String[] partnames;
    
    private double[][] features;
    private double[][] features_reduced;    //removing the same rows
    private double[][] features_sampled;    //for training data
    
    private double[] label;
    private double[] label_reduced;
    private double[] label_sampled;
    
    private double[] parts_ave;
    private double[] parts_std;
    
    private boolean print;
    
    public DataAnalysis(int constructs, int parts, boolean print) {
        
        this.constructs = constructs;
        this.parts = parts;
        
        this.partnames = null;
        this.features = null;
        this.features_reduced = null;
        this.features_sampled = null;
        this.label = null;
        this.label_reduced = null;
        this.label_sampled = null;
        
        this.parts_ave = null;
        this.parts_std = null;
        
        this.print = print;
    }

    public void parseFeatures(String input, String output) {
        
        try {
            
            FileInputStream inputFile = new FileInputStream("resources/" + input);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            
            generateMatrix(workbook.getSheet("Sheet1"));
            if (print) {
                Utilities.writeToCSV(features, output);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readLabel(String input) {
        
        try {
            this.label = Utilities.readFromCSV(input, constructs);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     
    public void multicollinearity(String featuresOutput, String labelOutput) {
    
        if (features==null && label==null) {
            
            System.out.println("Either features or label is NULL! Terminating...");
            return;
        }
        
        MulticollinearityCheck mc = new MulticollinearityCheck(constructs, parts, features, label);
        mc.removeRows(featuresOutput, labelOutput, print);

        this.features_reduced = mc.getFeaturesRed();
        this.constructs_reduced = this.features_reduced.length;
        this.label_reduced = mc.getLabelRed();
    }
    
    public void reducedRawPart() {

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
    }
    
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

    public List<Integer> generateTestData(int numOfTest) {
        
        List<Integer> test = new ArrayList<>();    //list of randomly picked random genes
        List<Integer> training = new ArrayList<>();
        
        //generate training of numbers from 1 to number of reduced rows
        for (int i = 0; i < constructs_reduced; i++) {
            training.add(i);
        }

        //randomly pick numbers from the training as toxic genes
        for (int i = 0; i < numOfTest; i++) {
            test.add(training.remove((int) (Math.random() * training.size())));
            /*System.out.print(test.get(i) + ": ");
            for (int j = 0; j < features_reduced[0].length; j++) {
                if (features_reduced[test.get(i)][j]==1) {
                    System.out.print(partnames[j] + ", ");
                }
            }
            System.out.println();*/
        }

        return test;
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
                    /*partnames[partIdx] = list_of_parts.get(partIdx);*/
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return fileIndex;
    }

    ////for small data
    public void average_growth(String outputFile) {
        
        if (features_reduced==null || label_reduced==null) {
            
            System.out.println("Either features_reduced or label_reduced is NULL! Terminating...");
            return;
        }
        
        this.parts_ave = new double[parts];
        this.parts_std = new double[parts];
        double[] min = new double[parts];
        double[] max = new double[parts];

        for (int j = 0; j < parts; j++) {
        //    System.out.println("-----index: " + (j+1) + ":");
            double total = 0.0;
            int count = 0;
            boolean first = true;
            for (int i = 0; i < features_reduced.length; i++) {
                if (features_reduced[i][j]==1.0) {
            //        System.out.println(label_reduced[i]);
                    if (first) {
                        min[j] = label_reduced[i];
                        max[j] = label_reduced[i];
                        first = false;
                    }
                    else {
                        if (label_reduced[i] < min[j]) {
                            min[j] = label_reduced[i];
                        }
                        if (label_reduced[i] > max[j]) {
                            max[j] = label_reduced[i];
                        }
                    }
                    total += label_reduced[i];
                    count++;
                }
            }
            parts_ave[j] = total / count;
            
            double ss = 0.0;
            for (int i = 0; i < features_reduced.length; i++) {
                if (features_reduced[i][j]==1.0) {
                //    System.out.println(label_reduced[i]);
                    ss = ss + Math.pow((label_reduced[i] - parts_ave[j]), 2);
                }
            }
            parts_std[j] = Math.sqrt(ss/(count-1));
            
            //System.out.println("++Average: " + parts_ave[j]);
            //System.out.println("++STDev: " + parts_std[j]);
            //System.out.println("++Min: " + min[j]);
            //System.out.println("++Max: " + max[j]);
            
        }
        
        //write into CSV file
        String[] container = new String[7];
        //first line
        String line = "Index,";
        for (int  j = 0; j < parts; j++) {
            line += (j + 1) + ",";
        }
        container[0] = line.substring(0, line.length()-1);
        //second line
        line = "Partname,";
        for (int  j = 0; j < parts; j++) {
            line += partnames[j] + ",";
        }
        container[1] = line.substring(0, line.length()-1);
        //third line
        container[2] = "";
        //fourth line
        line = "AVERAGE,";
        for (int  j = 0; j < parts; j++) {
            line += parts_ave[j] + ",";
        }
        container[3] = line.substring(0, line.length()-1);
        //fifth line
        line = "STDEV,";
        for (int  j = 0; j < parts; j++) {
            line += parts_std[j] + ",";
        }
        container[4] = line.substring(0, line.length()-1);
        //sixth line
        line = "MIN,";
        for (int  j = 0; j < parts; j++) {
            line += min[j] + ",";
        }
        container[5] = line.substring(0, line.length()-1);
        //seventh line
        line = "MAX,";
        for (int  j = 0; j < parts; j++) {
            line += max[j] + ",";
        }
        container[6] = line.substring(0, line.length()-1);
        
        if (print) {
            Utilities.writeToCSV(container, outputFile);
        }
        
    }

    ////for current usage
    public void mRegression_python() {
    
        MultipleRegression mReg = new MultipleRegression();
        //output = mReg.pyRegression("garuda_reg.py");
        
        double[][] data = Utilities.readFromCSV("features-simplified-del199.csv", 323, 49);
        double[] label = Utilities.nDTo1DArray(Utilities.readFromCSV("label-simplified-del199.csv", 323, 1), 0);
        mReg.jvRegression(data, label);
        
        System.out.println("***Task done!!!");
    }

    private void generateMatrix(XSSFSheet sheet) {

        this.features = new double[constructs][parts];
        List<String> list_of_parts = new ArrayList<String>();
        this.partnames = new String[parts];
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

            Row row = sheet.getRow(i);

            try {

                for (int j = 0; j < 5; j++) {

                    Cell cell = row.getCell(j + 4);
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    String part = cell.getStringCellValue();
                    
                    if (part.equals("")) {
                        continue;
                    }
                    
                    if (!list_of_parts.contains(part)) {
                        list_of_parts.add(cell.getStringCellValue());
                    }
                    int partIdx = list_of_parts.indexOf(part);

                    this.features[i-1][partIdx] = 1.0;
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        list_of_parts.toArray(this.partnames);
    }

    /*public void featureSort() {
        List<Part> list_of_parts = new ArrayList<Part>();
        
        list_of_parts.add(new Part(0, "part1", new double[]{0.1, 0.2, 0.3}, 0.7, 0));
        list_of_parts.add(new Feature(1, "part2", new double[]{0.2, 0.1, 0.3}, 0.5, 0));
        list_of_parts.add(new Feature(2, "part3", new double[]{0.2, 0.3, 0.1}, 0.2, 0));
        list_of_parts.add(new Feature(3, "part4", new double[]{0.3, 0.1, 0.2}, 0.9, 0));
        
        Collections.sort(features, Feature.Comparators.labelComparator);
        
        for (int i = 0; i < features.size(); i++) {
            System.out.println (features.get(i).getName());
        }
    }*/

    public void runKMeansNaiveBayes(boolean sampled, int numOfSample) {
        
        List<Integer> testData = generateTestData(numOfSample);
        
        List<Part> results = new ArrayList<Part>();
        double[][] label = null;
        
        //clustering part
        if (sampled) {
            subsetMatrix(testData);            //only once
            label = Utilities._1DTonDArray(label_sampled);
        }
        else {
            label = Utilities._1DTonDArray(label_reduced);
        }
        KMeansClustering kmeans = new KMeansClustering(label, 2);
        int[] cluster = kmeans.getListCluster();
        
        //comparing centroids for labeling
        List<Vector> center = kmeans.getCentroids();
        boolean flip = false;
        if (center.get(0).getDimension(0) > center.get(1).getDimension(0)) {
            flip = true;
        }
         
        //naive bayes part
        List<Feature> features = null;
        if (sampled) {
            features = Utilities.arrayToFeature(features_sampled, cluster);
        }
        else {
            features = Utilities.arrayToFeature(features_reduced, cluster);
        }
        NaiveBayes nb = new NaiveBayes(features, 2, parts);

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
            results.add(new Part(partnames[idx], parts_ave[idx], parts_std[idx], probability));
        }

        Collections.sort(results, Part.Comparators.labelComparator);
        Map<String, Double> part_prob = new HashMap<String, Double>();
        
        String partstring = "[";
        String growth = "[";
        String stdev = "[";
        String prob_pos = "[";
        String prob_neg = "[";
        
        for (int i = results.size()-1; i >= 0; i--) {
            
            partstring = partstring + "\'" + results.get(i).getName() + "\', ";
            growth = growth + results.get(i).getAve_growth() + ", ";
            stdev = stdev + results.get(i).getStd_dev() + ", ";
            double prob = results.get(i).getProbability();
            if (prob >= 0) {
                prob_pos = prob_pos + prob + ", ";
                prob_neg = prob_neg + "0.0, ";
            }
            else {
                prob_pos = prob_pos + "0.0, ";
                prob_neg = prob_neg + prob + ", ";
            }
            
            part_prob.put(results.get(i).getName(), results.get(i).getProbability());
            
            System.out.println(results.get(i).getName() + "\t" + results.get(i).getAve_growth() + "\t" + results.get(i).getProbability());
        }
        
        partstring = partstring.substring(0, partstring.length()-2) + "]";
        growth = growth.substring(0, growth.length()-2) + "]";
        stdev = stdev.substring(0, stdev.length()-2) + "]";
        prob_pos = prob_pos.substring(0, prob_pos.length()-2) + "]";
        prob_neg = prob_neg.substring(0, prob_neg.length()-2) + "]";
        
        System.out.println(partstring);
        System.out.println(prob_pos);
        System.out.println(prob_neg);
        
        System.out.println(growth);
        System.out.println(stdev);
        
        //print test data
        List<Double> fitnesses = new ArrayList<Double>();
        for (int i = 0; i < testData.size(); i++) {
            System.out.print((testData.get(i) + 1) + ": ");
            double fitness = 0.0;
            for (int j = 0; j < features_reduced[0].length; j++) {
                if (features_reduced[testData.get(i)][j]==1) {
                    fitness += part_prob.get(partnames[j]);
                    System.out.print(partnames[j] + ", ");
                }
            }
            fitnesses.add(fitness);
            System.out.println("= " + fitness);
        }
    }
     
    public void runKMeansExpert(String data, String label) {
        
        int row = 354;
        int column = 15;
        
        String[] partnames = new String[] {
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
            if (cluster[i]==1) {
                total++;
                for (int j = 0; j < data_arr[0].length; j++) {
                    if (data_arr[i][j]==1.0 && !goodparts.contains(j)) {
                        goodparts.add(j);
                    }
                }
            }
            System.out.println(cluster[i] + "    " + label_2d[i][0]);
        }
        
        System.out.println("Total good constructs: " + total + ", and bad constructs: " + (cluster.length-total));
        
        System.out.println("*****List of good parts:");
        for (int k = 0; k < goodparts.size(); k++) {
            System.out.println(partnames[goodparts.get(k)]);
        }
        
    }
    
    public void runNaiveBayes(String data, String label) {
        
        int row = 354;
        int column = 15;
        //double threshold = 0.1;
        String[] partnames = new String[] {
            "DC-01", "RHa-36", "AcT-09", "RHa-51", "AcT-11", "NMT-05", "NMT-17", "DC-02",
            "AAF-20", "AAF-27", "AcT-05", "RHa-18", "DC-06", "AAF-18", "NMT-13"
        };
        double[] growth = new double[] {
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
                            //System.out.println(growth[idx] + "\t" + partnames[idx] + "\t\t" + probability);
                            probabilities[j][(int)(threshold - 1)] = probability;
                        }
                        else if (i == 1) {
                            int idx = classList.get(j).getId();
                            double probability = classList.get(j).getVector().getDimension(0);
                            //System.out.println(growth[idx] + "\t" + partnames[idx] + "\t" + probability);
                            probabilities[j][(int)(threshold - 1)] = probability;
                        }
                    }
                    //add to the toxic list
                    /*if (classList.get(j).getCluster() == 0) {
                        toxicList.add(classList.get(j).getId() + 1);
                    }*/
                }
            }
        }
        
        for (int i = 0; i < probabilities.length; i++) {
            System.out.println(partnames[i]);
            for (int j = 0; j < probabilities[0].length; j++) {
                System.out.print (probabilities[i][j] + ", ");
            }
            System.out.println();
        }
        
        /*System.out.println("Part: " + partnames.length);
        for (int i = 0; i < partnames.length; i++) {
            System.out.println((i + 1) + ". " + partnames[i]);
        }*/
    }
    
}

