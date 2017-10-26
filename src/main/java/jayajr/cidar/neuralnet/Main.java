/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jayajr.cidar.neuralnet;

import java.util.*;
import java.io.*;
import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;


/**
 *
 * @author root
 */
public class Main {
   
    @Test
    public static void main(String[] args) throws IOException{
        
        //countFeaturesAndConstructs();
        
        XSSFWorkbook feat = new XSSFWorkbook(new FileInputStream("nnet-input.xlsx"));
        XSSFSheet sh = feat.getSheetAt(0);
        
        // Get Cell syntax
        // wb.getSheetAt(0).getRow(9).getCell(4);
        
        // Enzyme Hash
        HashMap<String, Integer> enzymes = new HashMap<>();
        HashSet<String> enz_names = new HashSet<>();
        
        // Enzymes 1-5 lie in columns 1-5
        int left = 1; int right = 5;
        
        // Label lies in column P
        int J = 9;
        
        // Index
        Integer hashIndex = -1;
        
        // Number of Features and constructs
        int featureCount = 15;
        int constructCount = 424;
        // Counted by below function

        double[][] featuresData = new double[constructCount][featureCount];
        double[][] labelData = new double[constructCount][1];
        int cluster = 3;
        
        
        int rowIndex = -2;
        for (int j = 0; j < constructCount; j++){
            Row row = sh.getRow(j);
            // Skip column headers
            if (rowIndex++ == -1) continue;
            
            // Get Feature Data
            for (int i = left; i <= right; i++){
                String enzyme = row.getCell(i).toString();
                
                
                // Skip column headers
                if (enzyme.contains("Enzyme")){
                    continue;
                }
                
                if (!enzyme.isEmpty()){
                    if (enzymes.containsKey(enzyme)){
                        Integer repeatIndex = enzymes.get(enzyme);
                        featuresData[rowIndex][repeatIndex] = 1;
                        
                    } else {
                        hashIndex = hashIndex+1;
                        enzymes.put(enzyme, hashIndex);
                        enz_names.add(enzyme);
                        
                        featuresData[rowIndex][hashIndex] = 1;
                        
                    }
                }
            }
            
            // Get Label Data
            /**/
            String label = row.getCell(J).toString();
            switch(label.toLowerCase().trim()){
                case "bad":
                    labelData[rowIndex][0] = -1;
                    break;
                    
                case "ok":
                    labelData[rowIndex][0] = 0;
                    break;
                    
                case "good":
                    labelData[rowIndex][0] = 1;
                    break;
            }
            /**/
        }
        
        // Fill in zeroes of feature matrix;
        for (int i = 0; i < constructCount; i++){
            for (int j = 0; j < featureCount; j++){
                if (featuresData[i][j] != 1){
                    featuresData[i][j] = 0;
                }
                
                //System.out.print((int)featuresData[i][j] + " ");
            }
            //System.out.print("\n");
        }
        /**/
        
        // Print out the label matrix;
        /**
            for (int j = 0; j < constructCount; j++){
                System.out.println((int)labelData[j][0]);
            }        
        /**/
            
            
        // See which feature has which index
        /**
        Iterator it = enz_names.iterator();
        while(it.hasNext()){
            featureCount++;
             
            Object thing = it.next();
            int num = enzymes.get((String)thing);
            
            System.out.println(thing + ": " + num);
        }
        /**/
        
        // NNet Constructor
        NNet nnet = new NNet(featuresData, labelData, cluster, 420);
        
        System.out.println();
        
        
        List<Integer> output = nnet.getTestList();
        
        
        for (int i = 0; i < output.size(); i++){
            System.out.println(output.get(i) + " real:" + labelData[output.get(i)][0]);
        }
        
        //System.out.println((double)correct/(double)constructCount);
        
        
    }
    
    public static void countFeaturesAndConstructs() throws IOException{

        XSSFWorkbook wb = new XSSFWorkbook(new FileInputStream("nnet-input.xlsx"));
        XSSFSheet sh = wb.getSheetAt(0);
        
        // Get Cell syntax
        // wb.getSheetAt(0).getRow(9).getCell(4);
        
        // Enzyme Hash
        HashMap<String, Integer> enzymes = new HashMap<>();
        HashSet<String> enz_names = new HashSet<>();
        
        // Enzymes 1-5 lie in columns E-I
        int E = 4; int I = 8;
        
        // Label lies in column P
        int P = 12;
        
        int featureCount = 0;
        int constructCount = -1;    // Skip the column headers
        
        for (Row row: sh){
            for (int i = E; i <= I; i++){
                String enzyme = row.getCell(i).toString();
                
                // Skip the column headers
                if (enzyme.contains("Enzyme")){
                    continue;
                }
                
                if (!enzyme.isEmpty()){
                    if (enzymes.containsKey(enzyme)){
                        Integer plusone = enzymes.get(enzyme)+1;
                        enzymes.put(enzyme, plusone);
                        
                    } else {
                        Integer one = 1;
                        enzymes.put(enzyme, one);
                        enz_names.add(enzyme);
                        
                    }
                }
                
            }
            constructCount++;
        }
        

        Iterator it = enz_names.iterator();
        
        while(it.hasNext()){
            featureCount++;
            
             
            Object thing = it.next();
            int num = enzymes.get((String)thing);
            
            System.out.println(thing + ": " + num);
        }
        
        System.out.println();
        System.out.println("featureCount: " + featureCount);
        System.out.println("constructCount: " + constructCount);

    }
    
}
