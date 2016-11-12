/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mardian
 */
public class SpreadsheetParser {
    
    private double[][] perfgroData;
    
    private int parts;
    private int pnum;
    private int cnum;
    private double threshold;
    private int numtoxic;
    
    private int nonemptypart;
    
    List<Integer> newlist;
    
    public SpreadsheetParser (String inputUrl, int parts, int pnum, int cnum, double threshold, int numtoxic) {
        this.parts = parts;
        this.pnum = pnum;
        this.cnum = cnum;
        this.threshold = threshold;
        //this.threshold = Math.random();
        this.numtoxic = numtoxic;
        //this.numtoxic = (int)(Math.random() * (pnum+1));
        
        this.perfgroData = new double[cnum][parts+2];
        
        this.nonemptypart = 0;
        
        init(inputUrl);
    }
    
    private void init (String inputUrl) {
        
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<pnum; i++) {
            list.add(i+1);
        }
        
        newlist = new ArrayList<>();
        
        ///
        //newlist.add(8);
        //newlist.add(5);
        //newlist.add(7);
        ///
        
    //    System.out.println("List of toxic genes: ");
        //for (int i=0; i<newlist.size(); i++) {
        for (int i=0; i<numtoxic; i++) {
        
            newlist.add (list.remove((int)(Math.random() * list.size())));
            System.out.println(newlist.get(i));
        }
        System.out.println("Total " + numtoxic + " toxic genes, and " + (pnum-newlist.size()) + " non-toxic genes with threshold " + threshold);
        
        int[] partcounter = new int[pnum];
        
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            XSSFSheet sheet = workbook.getSheetAt(2);
      
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);
                
                int size = (int) row.getCell(5).getNumericCellValue();
                
                perfgroData[i-1][1] = 1 - (Math.random() * threshold);
                
                for (int j=0; j<size; j++) {
                    
                    int part = (int) row.getCell(j+6).getNumericCellValue();
                    perfgroData[i-1][j+2] = part;
                    
                    partcounter[part-1]++;
                    
                    if (newlist.contains (part)) {
                        perfgroData[i-1][1] = Math.random() * threshold;
                    }

                }

            }
            
            /*sheet = workbook.getSheetAt(3);
            
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);
                
                //performance
                perfgroData[i-1][0] = row.getCell(7).getNumericCellValue();
                //growth rate
                //perfgroData[i-1][1] = row.getCell(5).getNumericCellValue();

            }*/
            
            for (int i=0; i<partcounter.length; i++) {
                if (partcounter[i]!=0) {
                    nonemptypart++;
                }
            }
            
            inputFile.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public double[][] getData() {
        
        double[][] data = new double[perfgroData.length][perfgroData[0].length];
        for(int i=0; i<data.length; i++) {
            for(int j=0; j<data[i].length; j++) {
                data[i][j] = perfgroData[i][j];
            }
        }
        return data;
    }
        
    public double[][] getPartData() {
        
        double[][] data = new double[cnum][parts];
        for(int i=0; i<data.length; i++) {
            for(int j=0; j<data[i].length; j++) {
                data[i][j] = perfgroData[i][j+2];
            }
        }
        return data;
    }
     
    public double[][] getGrowth() {
        
        double[][] data = new double[perfgroData.length][1];
        for(int i=0; i<data.length; i++) {
            data[i][0] = perfgroData[i][1];
        }
        return data;
    }
    
    public double[][] getPartCount() {
        
        double[][] data = new double[cnum][pnum+2];
        for(int i=0; i<perfgroData.length; i++) {
            for(int j=0; j<perfgroData[0].length; j++) {
                if (j==0 || j==1) {
                    data[i][j] = perfgroData[i][j];
                }
                else {
                    data[i][((int)perfgroData[i][j]-1)+2] = 1;
                }
            }
        }
        
        /*for(int i=0; i<data.length; i++) {
            for(int j=0; j<data[i].length; j++) {
                System.out.print (data[i][j] + "   ");
            }
            System.out.println();
        }*/
        
        return data;
    }
     
    public double[][] transposeRaw() {
        
        double[][] data = new double[pnum][perfgroData.length]; 
        
        for (int i=0; i<perfgroData.length; i++) {
        
            for (int j=2; j<perfgroData[0].length; j++) {
                data[(int)perfgroData[i][j]-1][i] = perfgroData[i][1];
            }
        }
        
        return data;
    }
    
    public double[][] transposeData() {
        
        double[][] data = new double[pnum][perfgroData.length]; 
        
        for (int i=0; i<perfgroData.length; i++) {
        
            for (int j=2; j<perfgroData[0].length; j++) {
                data[(int)perfgroData[i][j]-1][i] = perfgroData[i][1];
            }
        }
        
        double[][] cleanData = new double[nonemptypart][cnum];
        
        int x = 0;
        for (int i=0; i<data.length; i++) {
            int counter = 0;
            for (int j=0; j<data[0].length; j++) {
                if(data[i][j]!=0.0) {
                    counter++;
                }  
            }
            if(counter!=0) {
                for (int j=0; j<data[0].length; j++) {
                    cleanData[x][j] = data[i][j];
                }
                x++;
            }
        }
        
        return cleanData;
    }
    
    public int getNonEmpty () {
        return nonemptypart;
    }
    
    public List<Integer> getList() {
        return this.newlist;
    }
}
