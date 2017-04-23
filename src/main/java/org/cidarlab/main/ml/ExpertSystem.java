/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.main.dom.Data;

/**
 *
 * @author mardian
 */
public class ExpertSystem {
    
    private double[][] data;
    private double threshold;
    
    private List<Data> clusterData;
    
    public ExpertSystem (double[][] data, double threshold) {
        
        this.data = data;
        this.threshold = threshold;
        this.clusterData = new ArrayList<Data>();
        
        init ();
    }
    
    public void init() {
        
        for (int i=0; i<data.length; i++) {
            int cluster = 0;
            for (int j=0; j<data[0].length; j++) {
                if (data[i][j]>threshold) {
                    cluster = 1;
                }
                clusterData.add(new Data(data[i], i, cluster));
            }
        }
    }
    
    public List<Data> getClusterData () {
        return this.clusterData;
    }
    
    /*public void printClusterData() {
        int cluster0 = 0;
        int cluster1 = 0;
        for (int i=0; i<clusterData.size(); i++) {
            if (clusterData.get(i).getCluster() == 0)
                cluster0++;
            else if (clusterData.get(i).getCluster() == 1)
                cluster1++;
        }
        System.out.println("Expert System Number of 1st cluster: " + cluster0 + " and 2nd cluster: " + cluster1);
    }*/
    
}
