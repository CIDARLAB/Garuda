/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.util;

import java.util.List;
import org.cidarlab.garuda.dom.Feature;

/**
 *
 * @author mardian
 */
public class FormatExchange {
    
    public static double[][] featureToArray (List<Feature> datalist) {
        
        int row = datalist.size();
        int column = datalist.get(0).getVector().getSize();
        
        double[][] array = new double[row][column];
        
        for (int i = 0; i < row; i++) {
            array[i] = datalist.get(i).getVector().getArray();
        }
        
        return array;
    }
    
    public static double[] labelTo1DArray (List<Feature> datalist) {
        
        int row = datalist.size();
        double[] array = new double[row];
        
        for (int i = 0; i < row; i++) {
            array[i] = datalist.get(i).getCluster();
        }
        
        return array;
    }
    
    public static double[] nDTo1DArray (double[][] ndArray, int idx) {
        
        int row = ndArray.length;
        double[] array = new double[row];
        
        for (int i = 0; i < row; i++) {
            array[i] = ndArray[i][idx];
        }
        
        return array;
    }
    
}
