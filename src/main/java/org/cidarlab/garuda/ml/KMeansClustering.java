/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.ml;

import org.cidarlab.garuda.dom.Vector;
import org.cidarlab.garuda.dom.Feature;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author mardian
 */
public class KMeansClustering {
    
    private int NUM_CLUSTERS;    // Total clusters.
    private int TOTAL_DATA;      // Total data points.
    private int DIMENSION;
    
    private double SAMPLES[][];
    
    private boolean zeroIsToxic;
            
    private List<Feature> dataSet;
    private List<Vector> centroids;
    
    public KMeansClustering (double[][] inputData, int cluster) {
        
        NUM_CLUSTERS = cluster;
        TOTAL_DATA = inputData.length;
        DIMENSION = inputData[0].length;
        SAMPLES = inputData;
        
        zeroIsToxic = false;
        
        dataSet = new ArrayList<Feature>();
        centroids = new ArrayList<Vector>();
        
        start();
    }
    
    public void start()
    {
    //    System.out.println("Centroids initialized at:");
        
        //generate random centroids
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<TOTAL_DATA; i++) {
            list.add(i);
        }
        
        int[] rand = new int[NUM_CLUSTERS];
        for (int i=0; i<NUM_CLUSTERS; i++) {
        
            rand[i] = list.remove((int)(Math.random()*list.size()));
            double[] parameters = new double [DIMENSION];
            for (int j=0; j<parameters.length; j++) {
                parameters[j] = SAMPLES[rand[i]][j];
            }
            centroids.add (new Vector (parameters));
        }
        //System.out.println();
        
        kMeanCluster();
        
        ////for 2 setCluster only////
        if (centroids.get(0).getDimension(0)<centroids.get(1).getDimension(0)) {
            this.zeroIsToxic = true;
        }
        ////
        
        // Print out clustering results.
        for (int i=0; i<NUM_CLUSTERS; i++) {
            
            System.out.println("Cluster " + i + " includes:");
            for(int j=0; j<TOTAL_DATA; j++) {
                
                if (dataSet.get(j).getCluster()==i) {
                    
                    //System.out.println((dataSet.get(j).getId()+1));
                    System.out.println((dataSet.get(j).getVector().getDimension(0) +
                            "\t" + dataSet.get(j).getVector().getDimension(1)));
                }
            } // j
            System.out.println();
        } // i /**/
        
    }
    
    private void kMeanCluster()
    {
        final double bigNumber = Math.pow(10, 10);    // some big number that's sure to be larger than our data range.
        double minimum = bigNumber;                   // The minimum value to beat. 
        double distance = 0.0;                        // The current minimum value.
        int cluster = 0;
        boolean isStillMoving = true;
        Feature newData = null;
        
        // Add in new data, one at a time, recalculating centroids with each new one. 
        for (int sampleNumber=0; dataSet.size()<TOTAL_DATA; sampleNumber++) {
            
            double[] parameters = new double[DIMENSION];
            for (int i=0; i<DIMENSION; i++) {
                parameters[i] = SAMPLES[sampleNumber][i];
            }
            
            newData = new Feature(sampleNumber, parameters);
            dataSet.add(newData);
            minimum = bigNumber;
            for (int i=0; i<NUM_CLUSTERS; i++) {
                distance = dist(newData, centroids.get(i));
                if (distance<minimum){
                    minimum = distance;
                    cluster = i;
                }
            }
            newData.setCluster(cluster);
            
            // calculate new centroids.
            for (int i=0; i<NUM_CLUSTERS; i++) {
                
                double[] total = new double[DIMENSION];
                int totalInCluster = 0;
                
                for (int j=0; j<dataSet.size(); j++) {
                    
                    if (dataSet.get(j).getCluster()==i) {
                        
                        for (int k=0; k<DIMENSION; k++) {
                            total[k] += dataSet.get(j).getVector().getDimension(k);
                        }
                        totalInCluster++;
                    }
                }
                
                if (totalInCluster>0){
                    
                    for (int k=0; k<DIMENSION; k++) {
                        
                        centroids.get(i).setDimension((total[k]/totalInCluster), k);
                    }
                }
            }
        }
        
        // Now, keep shifting centroids until equilibrium occurs.
        while (isStillMoving) {
            
            // calculate new centroids.
            for (int i=0; i<NUM_CLUSTERS; i++) {
                
                double[] total = new double[DIMENSION];
                int totalInCluster = 0;
                
                for (int j=0; j<dataSet.size(); j++) {
                    
                    if(dataSet.get(j).getCluster()==i) {
                        
                        for (int k=0; k<DIMENSION; k++) {
                            total[k] += dataSet.get(j).getVector().getDimension(k);
                        }
                        totalInCluster++;
                    }
                }
                if (totalInCluster > 0) {
                    
                    for (int k=0; k<DIMENSION; k++) {
                        centroids.get(i).setDimension((total[k]/totalInCluster), k);
                    }
                }
            }
            
            // Assign all data to the new centroids
            isStillMoving = false;
            
            for (int i=0; i<dataSet.size(); i++) {
                
                Feature tempData = dataSet.get(i);
                minimum = bigNumber;
                
                for (int j = 0; j<NUM_CLUSTERS; j++) {
                    
                    distance = dist(tempData, centroids.get(j));
                    if (distance < minimum){
                        
                        minimum = distance;
                        cluster = j;
                    }
                }
                tempData.setCluster(cluster);
                
                if (tempData.getCluster()!=cluster){
                    
                    tempData.setCluster(cluster);
                    isStillMoving = true;
                }
            }
        }
    }
    
    /**
     * // Calculate Euclidean distance.
     * @param d - Feature object.
     * @param c - Centroid object.
     * @return - double value.
     */
    private static double dist (Feature d, Vector c) {
        
        double total = 0.0;
        for (int i=0; i<c.getSize(); i++) {
            total += Math.pow((d.getVector().getDimension(i) - c.getDimension(i)), 2);
        }
        return Math.sqrt(total);
    }
    
    public List<Feature> getClusterData () {
        
        //passed by reference
        return this.dataSet;
    }
    
    public boolean isZeroToxic () {
        
        return this.zeroIsToxic;
    }
}
    
