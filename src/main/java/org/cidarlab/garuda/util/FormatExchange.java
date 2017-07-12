/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import org.cidarlab.garuda.dom.Feature;

/**
 *
 * @author mardian
 */
public class FormatExchange {

    public static double[][] featureToArray(List<Feature> datalist) {

        int row = datalist.size();
        int column = datalist.get(0).getVector().getSize();

        double[][] array = new double[row][column];

        for (int i = 0; i < row; i++) {
            array[i] = datalist.get(i).getVector().getArray();
        }

        return array;
    }

    public static double[] labelTo1DArray(List<Feature> datalist) {

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

}
