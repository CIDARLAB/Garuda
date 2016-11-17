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
    private int psize;
    private double threshold;
    private int toxic;
    private int participant;
    
    List<Integer> tlist;
    
    private int[] tracker;
    
    public SpreadsheetParser (String inputUrl, int cnum, int pnum, int psize, double threshold, int toxic) {
        
        this.cnum = cnum;
        this.pnum = pnum;
        this.psize = psize;
        this.threshold = threshold;
        this.toxic = toxic;
        this.participant = 0;
        
        this.data = new double[cnum][psize+2];
        
        this.tracker = new int[cnum];
        
        init(inputUrl);
    }
    
    private void init (String inputUrl) {
        
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<pnum; i++) {
            list.add(i+1);
        }
        tlist = new ArrayList<>();
        
    //    System.out.println("List of toxic genes: ");
        for (int i=0; i<toxic; i++) {
        
            tlist.add (list.remove((int)(Math.random() * list.size())));
            System.out.println(tlist.get(i));
        }
        System.out.println("Total " + toxic + " toxic genes, and " + (pnum-tlist.size()) + " non-toxic genes with threshold " + threshold);
        
        int[] partCount = new int[pnum];
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            XSSFSheet sheet = workbook.getSheetAt(2);
            
            Random rand = new Random();
      
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);
                
                int size = (int) row.getCell(5).getNumericCellValue();
                
            //    data[i-1][1] = 1 - (Math.random() * threshold);
                data[i-1][1] = rand.nextGaussian()*0.1+0.3;
                
                for (int j=0; j<size; j++) {
                    
                    int part = (int) row.getCell(j+6).getNumericCellValue();
                    data[i-1][j+2] = part;
                    
                    partCount[part-1]++;
                    
                    if (tlist.contains (part)) {
                //        data[i-1][1] = rand.nextGaussian()*0.4+threshold;
                        tracker[i-1]++;
                    }

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
    
    public double[][] getData() {
        
        double[][] datacopy = new double[cnum][psize+2];
        for(int i=0; i<datacopy.length; i++) {
            for(int j=0; j<datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j];
            }
        }
        return datacopy;
    }
        
    public double[][] getPart() {
        
        double[][] datacopy = new double[cnum][psize];
        for(int i=0; i<datacopy.length; i++) {
            for(int j=0; j<datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j+2];
            }
        }
        return datacopy;
    }
     
    public double[][] getGrowth() {
        
        double[][] datacopy = new double[cnum][1];
        for(int i=0; i<datacopy.length; i++) {
            datacopy[i][0] = this.data[i][1];
        }
        return datacopy;
    }
    
    public double[][] getCount() {
        
        double[][] datacopy = new double[cnum][pnum];
        for(int i=0; i<this.data.length; i++) {
            for(int j=2; j<this.data[0].length; j++) {
                //if (j==0 || j==1) {
                //    datacopy[i][j] = this.data[i][j];
                //}
                //else {
                    datacopy[i][((int)this.data[i][j]-1)] = 1;
                //}
            }
        }
        return datacopy;
    }
     
    public double[][] transpose() {
        
        double[][] datacopy = new double[pnum][cnum]; 
        
        for (int i=0; i<this.data.length; i++) {
        
            for (int j=2; j<this.data[0].length; j++) {
                datacopy[(int)this.data[i][j]-1][i] = this.data[i][1];
            }
        }
        return datacopy;
    }
    
    public double[][] transposeClean() {
        
        double[][] dataRaw = this.transpose();
        
        double[][] cleanData = new double[participant][cnum+1];
        
        int x = 0;
        for (int i=0; i<dataRaw.length; i++) {
            int counter = 0;
            for (int j=0; j<dataRaw[0].length; j++) {
                if(dataRaw[i][j]!=0.0) {
                    counter++;
                }  
            }
            if(counter!=0) {
                cleanData[x][1] = i+1;  //extra column to store id+1
                for (int j=0; j<dataRaw[0].length; j++) {
                    cleanData[x][j+1] = dataRaw[i][j];
                }
                x++;
            }
        }
        return cleanData;
    }
    
    public int getParticipant () {
        return this.participant;
    }
    
    public List<Integer> getList() {
        return this.tlist;
    }
    
    public int[] getTracker() {
        return this.tracker;
    }
}
