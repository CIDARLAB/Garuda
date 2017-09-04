/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.cidarlab.main.garuda.FormatExchange;

/**
 *
 * @author mardian
 */
public class MulticollinearityCheck {
    
    private int num_of_constructs;
    private int num_of_parts;
    private String filename;
    private double[][] matrix;
    
    public MulticollinearityCheck (int num_of_constructs, int num_of_parts, String filename) {
    
        this.num_of_constructs = num_of_constructs;
        this.num_of_parts = num_of_parts;
        this.filename = filename;
        this.matrix = FormatExchange.readFromCSV(filename, num_of_constructs, num_of_parts);
    
        performCheck();
    }
    
    private void performCheck() {
        
        double[] firstColumn = new double[matrix.length];
        double[] secondColumn = new double[matrix.length];
        
        //columnCheck
        for (int j = 0; j < matrix[0].length; j++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int k = j+1; k < matrix[0].length; k++) {
                for (int i = 0; i < matrix.length; i++) {
                    firstColumn[i] = matrix[i][j];
                    secondColumn[i] = matrix[i][k];
                }
                double coorelation = pc.correlation(firstColumn, secondColumn);
                if (coorelation > 0.4 || coorelation < -0.4)
                    System.out.println("Column between: " + j + ", and " + k + ": " + coorelation);
            }
        }
        
        double[] firstColumn1 = new double[matrix[0].length];
        double[] secondColumn1 = new double[matrix[0].length];
        
        //rowCheck
        for (int i = 0; i < matrix.length; i++) {
            PearsonsCorrelation pc = new PearsonsCorrelation();
            for (int k = i+1; k < matrix.length; k++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    firstColumn1[j] = matrix[i][j];
                    secondColumn1[j] = matrix[k][j];/**/
                }
                double coorelation = pc.correlation(firstColumn1, secondColumn1);/**/
                if (coorelation > 0.8 || coorelation < -0.8)
                    System.out.println("Row between: " + i + ", and " + k + ": " + coorelation);
            }
        }
    }
}
