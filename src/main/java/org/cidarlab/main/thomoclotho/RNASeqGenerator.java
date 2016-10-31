/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 *
 * @author mardian
 */
public class RNASeqGenerator {
    
    public static void parseTable () {
    
        try {
            BufferedReader br = new BufferedReader(new FileReader("resources/input.txt"));
            
            /*File file = new File("resources/output.txt");
            if (!file.exists()) {
               file.createNewFile();
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));*/
            
            int numOfParts = 6;
            int numOfConstructs = 10;
            int parts = 3;
            
            String[][] rnaseqtab = new String[numOfParts][numOfConstructs];
            String[][] rnaseq = new String[numOfConstructs][parts];
             
            //String everything = "This is output\n";
            
            for (int i=0; i<numOfParts; i++) {
                for (int j=0; j<numOfConstructs; j++) {
                    rnaseqtab[i][j] = "NA";
                }
            }

            //System.out.println("==============");
            
            
            for (int i=0; i<numOfConstructs; i++) {
                String line = br.readLine();
                rnaseq[i] = line.split("\t");
                for (int j=0; j<rnaseq[i].length; j++)
                {
                    //System.out.println("==============" + j);
                    int partIdx = Integer.parseInt(rnaseq[i][j]) - 1;
                    rnaseqtab[partIdx][i] = String.valueOf(Math.random() * 10);
                }
                //System.out.println();
            }
            
            
            for (int i=0; i<numOfParts; i++) {
                for (int j=0; j<numOfConstructs; j++) {
                    System.out.print(rnaseqtab[i][j] + "\t");
                }
                System.out.println();
            }

            System.out.println("==============");
            
            
            //bw.write(everything);
            
            br.close();
            //bw.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
            
    }
}
