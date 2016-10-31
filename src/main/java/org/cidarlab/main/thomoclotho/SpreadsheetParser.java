/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.io.FileInputStream;
import java.util.Random;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author mardian
 */
public class SpreadsheetParser {
    
    public static double[][] perfgroData;
    
    public static void initiate (String inputUrl) {
          
        try
        {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            //Create Workbook instance holding reference to .xlsx file
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            XSSFSheet sheet = workbook.getSheetAt(2);
      
            int pnum = 20;
            int cnum = 10000;
            perfgroData = new double[cnum][6];
          
            for (int i=1; i<sheet.getLastRowNum()+1; i++) {

                Row row = sheet.getRow(i);

                int takenpart1, takenpart2, takenpart3, takenpart4;

                takenpart1 = (int) row.getCell(6).getNumericCellValue();
                perfgroData[i-1][2] = takenpart1;

                takenpart2 = (int) row.getCell(7).getNumericCellValue();
                perfgroData[i-1][3] = takenpart2;

                takenpart3 = (int) row.getCell(8).getNumericCellValue();
                perfgroData[i-1][4] = takenpart3;

                takenpart4 = (int) row.getCell(9).getNumericCellValue();
                perfgroData[i-1][5] = takenpart4;

                perfgroData[i-1][0] = Math.random() * 100;

                double aStart = 0.0;
                double aEnd = 1.0;
                Random aRandom = new Random();
                double Result =  aStart + (aEnd - aStart) * aRandom.nextDouble();

                perfgroData[i-1][1] = Result;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
