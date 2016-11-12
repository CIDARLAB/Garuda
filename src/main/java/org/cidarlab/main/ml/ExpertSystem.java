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
    
    private double[][] rawData;
    private List<Data> clusterData;
    private double threshold;
    private int cluster;
    
    public ExpertSystem (double[][] rawData, int cluster, double threshold) {
        this.rawData = rawData;
        this.threshold = threshold;
        this.cluster = cluster;
        clusterData = new ArrayList<Data>();
        init ();
    }
    
    public void init() {
        
        for (int i=0; i<rawData.length; i++) {
            int mCluster = 0;
            for (int j=0; j<rawData[0].length; j++) {
                if (rawData[i][j]>threshold) {
                    mCluster = 1;
                }
                clusterData.add(new Data(rawData[i], i, mCluster));
                //System.out.println((newlist.get(i)+1) + "     " + trainData[i][j]);
            }
        }
        
    }
    
    public List<Data> getData () {
        return this.clusterData;
    }
    
}
