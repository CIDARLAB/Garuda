/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mardian
 */
public class SpreadsheetParser {
    
    private double[][] data;
    
    private int cnum;
    private int pnum;
    private int size;
    private double threshold;
    private int toxic;
    
    private int participant;
    
    List<Integer> tlist;    //list of randomly picked random genes
    
    private int[] tracker;
    
    public SpreadsheetParser (String inputUrl, int cnum, int pnum, int size, int toxic, double healthy_mean, double healthy_sd, double toxic_mean, double toxic_sd) {
        
        this.cnum = cnum;
        this.pnum = pnum;
        this.size = size;
        this.threshold = threshold;
        this.toxic = toxic;
        
        this.data = new double[cnum][size+2];
        
        this.participant = 0;
        //this.tracker = new int[cnum];
        
        init(inputUrl, healthy_mean, healthy_sd, toxic_mean, toxic_sd);
    }
    
    private void init (String inputUrl, double healthy_mean, double healthy_sd, double toxic_mean, double toxic_sd) {
        
        generateToxic();
        
        int[] partCount = new int[pnum];
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            //construct sheet
            XSSFSheet sheet = workbook.getSheetAt(2);
            
            Random rand = new Random();
      
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);
                
                int size = (int) row.getCell(5).getNumericCellValue();
                
                //randomized growth rate
                //data[i-1][1] = 1 - (Math.random() * threshold);
                data[i-1][1] = rand.nextGaussian() * healthy_sd + healthy_mean;     //find out how gaussian random works
                
                for (int j=0; j<size; j++) {
                    
                    int part = (int) row.getCell(j+6).getNumericCellValue();
                    data[i-1][j+2] = part;
                    
                    partCount[part-1]++;
                    
                    if (tlist.contains (part)) {
                        data[i-1][1] = rand.nextGaussian() * toxic_sd + toxic_mean;
                        //data[i-1][1] = Math.random() * threshold;
                        //tracker[i-1]++;
                    }/**/
                }
                //System.out.println(data[i-1][1]);
            }
            
            /*sheet = workbook.getSheetAt(3);
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);
                
                //performance
                datacopy[i-1][0] = row.getCell(7).getNumericCellValue();
                //growth rate
                //data[i-1][1] = row.getCell(5).getNumericCellValue();

            }*/
            
            for (int i=0; i<partCount.length; i++) {
                if (partCount[i]!=0) {
                    participant++;
                }
            }
            inputFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void generateToxic () {
        
        //generate list of numbers from 1 to number of available parts
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<pnum; i++) {
            list.add(i+1);
        }
        tlist = new ArrayList<>();
        
        System.out.println("List of toxic genes: ");
        for (int i=0; i<toxic; i++) {
            //randomly pick numbers from the list as toxic genes
            tlist.add (list.remove((int)(Math.random() * list.size())));
            System.out.println("--- " + tlist.get(i));
        }
        System.out.println("Total " + toxic + " toxic genes, and " + (pnum - tlist.size()) + " non-toxic genes");
        
    }
    
    //get data array as it is
    public double[][] getData() {
        
        double[][] datacopy = new double[cnum][size+2];
        for(int i=0; i<datacopy.length; i++) {
            for(int j=0; j<datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j];
            }
        }
        return datacopy;
    }
    
    //get data array, parts only
    public double[][] getPart() {
        
        double[][] datacopy = new double[cnum][size];
        for(int i=0; i<datacopy.length; i++) {
            for(int j=0; j<datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j+2];
            }
        }
        return datacopy;
    }
    
    //get growth rate data as 2d array
    public double[][] getGrowth() {
        
        double[][] datacopy = new double[cnum][1];
        for(int i=0; i<datacopy.length; i++) {
            datacopy[i][0] = this.data[i][1];
            System.out.println("** " + datacopy[i][0]);
        }
        return datacopy;
    }
    
    //get matrix of parts-count
    public double[][] getCount() {
        
        double[][] datacopy = new double[cnum][pnum];
        for(int i=0; i<this.data.length; i++) {
            for(int j=2; j<this.data[0].length; j++) {
                datacopy[i][((int)this.data[i][j]-1)] = 1;  //should this be a counter instead of hardcoding with 1?
            }
        }
        return datacopy;
    }
    
    //get transpose matrix of parts (value will be growth rate)
    public double[][] transpose() {
        
        double[][] datacopy = new double[pnum][cnum];
        for (int i=0; i<this.data.length; i++) {
            for (int j=2; j<this.data[0].length; j++) {
                datacopy[(int)this.data[i][j]-1][i] = this.data[i][1];
            }
        }
        return datacopy;
    }
    
    //clean the transpose matrix with only existed parts
    public double[][] transposeClean() {
        
        double[][] dataRaw = this.transpose();
        double[][] cleanData = new double[participant][cnum];
        
        int x = 0;
        for (int i=0; i<dataRaw.length; i++) {
            int counter = 0;
            for (int j=0; j<dataRaw[0].length; j++) {
                if(dataRaw[i][j]!=0.0) {
                    counter++;
                }  
            }
            if(counter!=0) {
                //cleanData[x][1] = i+1;  //extra column to store id+1
                for (int j=0; j<dataRaw[0].length; j++) {
                    cleanData[x][j] = dataRaw[i][j];
                }
                x++;
            }
        }
        return cleanData;
    }
    
    public int getParticipant () {
        return this.participant;
    }
    
    public List<Integer> getToxicList() {
        return this.tlist;
    }
    
    /*public int[] getTracker() {
        return this.tracker;
    }*/
}
