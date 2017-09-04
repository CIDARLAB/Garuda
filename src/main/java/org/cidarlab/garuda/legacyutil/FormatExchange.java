/*
 * To change this license contents, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.legacyutil;

import org.cidarlab.garuda.model.DNAPlotLib;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import org.cidarlab.garuda.model.Feature;

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
                for (int j = 0; j < ndArray[0].length; j++) {
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

    public static void writeDPL(String fileIdx, DNAPlotLib info) {

        try {

            //write .sh file
            FileWriter fw = new FileWriter("resources/visual/generated/execute-" + fileIdx + ".sh");
            BufferedWriter bw = new BufferedWriter(fw);
            
            String command = "python -W ignore resources/visual/plot_SBOL_designs.py";
            command += " -params resources/visual/plot_parameters.csv";
            command += " -parts resources/visual/generated/part_information-" + fileIdx + ".csv";
            command += " -designs resources/visual/generated/dna_designs-" + fileIdx + ".csv";
            command += " -regulation resources/visual/reg_information.csv";
            command += " -output src/main/resources/static/images/generated/" + fileIdx + ".png";

            bw.write("#!/bin/bash\n");
            bw.write("\n");
            bw.write(command + "\n");

            bw.close();
            fw.close();

            //write dna_design.csv file
            fw = new FileWriter("resources/visual/generated/dna_designs-" + fileIdx + ".csv");
            bw = new BufferedWriter(fw);
            
            String contents = info.getName();
            List<String> partnames = info.getPartnames();
            for(int i = 0; i < partnames.size(); i++) {
                contents = contents + "," + partnames.get(i);
            }
            
            bw.write("design_name,parts,,,\n");
            bw.write(contents);

            bw.close();
            fw.close();

            //write part_information.csv file
            fw = new FileWriter("resources/visual/generated/part_information-" + fileIdx + ".csv");
            bw = new BufferedWriter(fw);
            
            contents = "";
            List<String> roles = info.getRoles();
            String[] colors = new String[]{"0.9;0.0;0.0", "0.0;0.9;0.0", "0.0;0.0;0.9"};
            int j = 0;
            for(int i = 0; i < roles.size(); i++) {
                if (j == colors.length) j = 0;
                contents = contents + "\n" + partnames.get(i) + "," + roles.get(i) + ",,,,," + colors[j] + ",,,,,," + partnames.get(i);
                if(i%2==0) contents = contents + ",,4,15,0,";
                else contents = contents + ",,4,-15,0,";
                j++;
            }
            
            bw.write("part_name,type,x_extent,y_extent,start_pad,end_pad,color,hatch,arrowhead_height,arrowhead_length,linestyle,linewidth,label,label_style,label_size,label_y_offset,label_x_offset,label_color");
            bw.write(contents);

            bw.close();
            fw.close();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public static boolean dnaplotlib_exec(String fileIdx) {

        boolean flag = true;

        try {

            ProcessBuilder pb = new ProcessBuilder("sh", "resources/visual/generated/execute-" + fileIdx + ".sh");
            Process p = pb.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String ret = in.readLine();

            while (ret != null) {
                flag = false;
                System.out.println(ret);
                ret = in.readLine();
            }
            //System.out.println("***dnaplotlib executed!!!");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }
}
