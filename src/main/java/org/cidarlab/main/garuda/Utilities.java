/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.cidarlab.main.dom.Feature;

/**
 *
 * @author mardian
 */
public class Utilities {

    //without label and class
    public static List<Feature> arrayToFeature(double[][] datamatrix) {

        int row = datamatrix.length;
        int column = datamatrix[0].length;
        
        List<Feature> features = new ArrayList<Feature>();
        
        for (int i = 0; i < row; i++) {
            double[] vector = new double[datamatrix[0].length];
            for (int j = 0; j < column; j++) {
                vector[j] = datamatrix[i][j];
            }
            features.add(new Feature(i, vector));
        }

        return features;
    }

    //with clusters
    public static List<Feature> arrayToFeature(double[][] datamatrix, int[] clusters) {

        int row = datamatrix.length;
        int column = datamatrix[0].length;
        
        List<Feature> features = new ArrayList<Feature>();
        
        for (int i = 0; i < row; i++) {
            double[] vector = new double[datamatrix[0].length];
            for (int j = 0; j < column; j++) {
                vector[j] = datamatrix[i][j];
            }
            features.add(new Feature(i, vector, clusters[i]));
        }

        return features;
    }

    //with label and threshold for class
    public static List<Feature> arrayToFeature(double[][] datamatrix, double[] label, double threshold) {

        int row = datamatrix.length;
        int column = datamatrix[0].length;
        
        List<Feature> features = new ArrayList<Feature>();
        
        for (int i = 0; i < row; i++) {
            double[] vector = new double[datamatrix[0].length];
            for (int j = 0; j < column; j++) {
                vector[j] = datamatrix[i][j];
            }
            int cluster = 0;
            if (label[i] > threshold)
                cluster = 1;
            features.add(new Feature(i, vector, cluster));
        }

        return features;
    }

    public static double[][] featureToArray(List<Feature> datalist) {

        int row = datalist.size();
        int column = datalist.get(0).getVector().getSize();

        double[][] array = new double[row][column];

        for (int i = 0; i < row; i++) {
            array[i] = datalist.get(i).getVector().getArray();
        }

        return array;
    }

    public static double[] labelToArray(List<Feature> datalist) {

        int row = datalist.size();
        
        double[] array = new double[row];

        for (int i = 0; i < row; i++) {
            array[i] = datalist.get(i).getCluster();
        }

        return array;
    }

    public static double[] nDTo1DArray(double[][] ndArray, int idx) {

        int row = ndArray.length;
        
        double[] array = new double[row];

        for (int i = 0; i < row; i++) {
            array[i] = ndArray[i][idx];
        }

        return array;
    }

    //String array
    public static void writeToCSV(String[][] ndArray, String filename) {

        try {

            FileWriter fw = new FileWriter("resources/" + filename);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (int i = 0; i < ndArray.length; i++) {
                String line = "";
                for (int j = 0; j< ndArray[0].length; j++) {
                    line += ndArray[i][j] + ",";
                }
                line = line.substring(0, line.length() - 1);
                bw.write(line + "\n");
            }
            
            bw.close();
            fw.close();
            
            System.out.println("***File " + filename + " is written!!!");

        } catch (IOException e) {

            e.printStackTrace();

        } 

    }

    //double array
    public static void writeToCSV(double[][] ndArray, String filename) {

        try {

            FileWriter fw = new FileWriter("resources/" + filename);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (int i = 0; i < ndArray.length; i++) {
                String line = "";
                for (int j = 0; j< ndArray[0].length; j++) {
                    line += ndArray[i][j] + ",";
                }
                line = line.substring(0, line.length() - 1);
                bw.write(line + "\n");
            }
            
            bw.close();
            fw.close();
            
            System.out.println("***File " + filename + " is written!!!");

        } catch (IOException e) {

            e.printStackTrace();

        } 

    }

    //String array
    public static void writeToCSV(String[] _1dArray, String filename) {

        try {

            FileWriter fw = new FileWriter("resources/" + filename);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (int i = 0; i < _1dArray.length; i++) {
                bw.write(_1dArray[i] + "\n");
            }
            
            bw.close();
            fw.close();
            
            System.out.println("***File " + filename + " is written!!!");

        } catch (IOException e) {

            e.printStackTrace();

        } 

    }

    //double array
    public static void writeToCSV(double[] _1dArray, String filename) {

        try {

            FileWriter fw = new FileWriter("resources/" + filename);
            BufferedWriter bw = new BufferedWriter(fw);
            
            for (int i = 0; i < _1dArray.length; i++) {
                bw.write(_1dArray[i] + "\n");
            }
            
            bw.close();
            fw.close();
            
            System.out.println("***File " + filename + " is written!!!");

        } catch (IOException e) {

            e.printStackTrace();

        } 

    }

    public static double[][] readFromCSV(String filename, int row, int column) {

        double[][] data = new double[row][column];
        
        String csvFile = "resources/" + filename;
        String line = "";
        String splitBy = ",";

        try {

            FileReader fr = new FileReader(csvFile);
            BufferedReader br = new BufferedReader(fr);
            
            for (int i = 0; (line = br.readLine()) != null; i++) {

                String[] entry = line.split(splitBy);
                for (int j = 0; j < entry.length; j++) {
                    data[i][j] = Double.parseDouble(entry[j]);
                }

            }
            
            fr.close();
            br.close();

        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
        
            return data;
        }
    }
    
    public static double[] readFromCSV(String filename, int row) {

        double[] data = new double[row];
        
        String csvFile = "resources/" + filename;
        String line = "";

        try {

            FileReader fr = new FileReader(csvFile);
            BufferedReader br = new BufferedReader(fr);
            
            for (int i = 0; (line = br.readLine()) != null; i++) {

                data[i] = Double.parseDouble(line);
            }
            
            fr.close();
            br.close();

        } catch (Exception e) {
            
            e.printStackTrace();
        } finally {
        
            return data;
        }
    }
}
