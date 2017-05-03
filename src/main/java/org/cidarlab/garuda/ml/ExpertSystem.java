/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.ml;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.garuda.dom.Data;

/**
 *
 * @author mardian
 */
public class ExpertSystem {
    
    private double[][] rawData;
    private double threshold;
    
    private List<Data> clusterData;
    
    public ExpertSystem (double[][] rawData, double threshold) {
        
        this.rawData = rawData;
        this.threshold = threshold;
        this.clusterData = new ArrayList<Data>();
        
        init ();
    }
    
    public void init() {
        
        for (int i=0; i<rawData.length; i++) {
            int cluster = 0;
            for (int j=0; j<rawData[0].length; j++) {
                if (rawData[i][j]>threshold) {
                    cluster = 1;
                }
                clusterData.add(new Data(rawData[i], i, cluster));
            }
        }
    }
    
    public List<Data> getClusterData () {
        return this.clusterData;
    }
    
}
