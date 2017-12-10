/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.machinelearning;

import java.util.ArrayList;
import java.util.List;
import org.cidarlab.garuda.model.Data;
import org.cidarlab.garuda.model.Feature;

/**
 *
 * @author mardian
 */
//use this class to classify data when a threshold is known
public class ExpertSystem {

    private double[][] rawData;
    private double threshold;

    private List<Feature> clusterData;

    public ExpertSystem(double[][] rawData, double threshold) {

        this.rawData = rawData;
        this.threshold = threshold;
        this.clusterData = new ArrayList<Feature>();

        init();
    }

    public void init() {

        for (int i = 0; i < rawData.length; i++) {
            int cluster = 0;
            for (int j = 0; j < rawData[0].length; j++) {
                if (rawData[i][j] > threshold) {
                    cluster = 1;
                }
                clusterData.add(new Feature(i, rawData[i], cluster));
            }
        }
    }

    public List<Feature> getClusterData() {
        return this.clusterData;
    }

}
