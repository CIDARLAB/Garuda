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
import java.util.List;

/**
 *
 * @author mardian
 */
public class FormatExchange {

    public static double[] nDTo1DArray(double[][] ndArray, int idx) {

        int row = ndArray.length;
        double[] array = new double[row];

        for (int i = 0; i < row; i++) {
            array[i] = ndArray[i][idx];
        }

        return array;
    }

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

    public static double[][] readFromCSV(String filename, int row, int column) {

        double[][] data = new double[row][column];
        
        String csvFile = "resources/" + filename;
        String line = "";
        String cvsSplitBy = ",";

        try {

            FileReader fr = new FileReader(csvFile);
            BufferedReader br = new BufferedReader(fr);
            for (int i = 0; (line = br.readLine()) != null; i++) {

                // use comma as separator
                String[] entry = line.split(cvsSplitBy);
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
}
