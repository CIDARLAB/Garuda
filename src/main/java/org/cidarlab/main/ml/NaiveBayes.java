/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import org.cidarlab.main.dom.Data;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mardian
 */
public class NaiveBayes {
    
    private List<Data> clList;
    //private List<Data> cluster2;
    private List<Integer> clList2;
    
    private List<Data> clusterData;
    private double[][] matrixData;
    private int cluster;
    private int nonempty;
    private int pnum;
    
    public NaiveBayes (List<Data> clusterData, double[][] matrixData, int cluster, int nonempty) {
        
        this.clusterData = clusterData;
        this.matrixData = matrixData;
        this.cluster = cluster;
        this.nonempty = nonempty;
        this.pnum = matrixData[0].length-2;
        
        clList = new ArrayList<Data>();
        //cluster2 = new ArrayList<Data>();
        clList2 = new ArrayList<Integer>();
        
        init ();
    }
    
    
    public void init () {
        
        int[] clusterCount = new int[cluster];
        double[] probCount = new double[cluster];
        
        double[][] probability = new double[cluster][pnum];
        
        for (int i=0; i<cluster; i++) {
            
            int count = 0;
            int[] partCount = new int[pnum];
        
        //    System.out.println("-Cluster " + i + " includes:");
            
            for(int j=0; j<matrixData.length; j++) {
                
                if (clusterData.get(j).cluster()==i) {
                    clusterCount[i]++;
                //    System.out.println((clusterData.get(j).getId()+1) + "\t" + clusterData.get(j).getPoint().getPoint(0));
                    
                    for(int k=2; k<matrixData[0].length; k++) {
                        if (matrixData[j][k]==1.0) {
                            partCount[k-2]++;
                            count++;
                        }
                    }
                }
            } // j
            
            
            for (int j=0; j<partCount.length; j++) {
                probability[i][j] = (double)(partCount[j]+1) / (double)(count+nonempty);
                //System.out.println ("+++Probability of part " + (j+1) + " given cluster: " + i + " is " + probability[i][j]);
            }
            
            probCount[i] = (double) clusterCount[i] / (double) matrixData.length;
            //System.out.println ("***Probability of cluster " + i + ": " + probCount[i] + ", number of occurence: " + count + "    " + nonempty);
        }
        
        for (int j=0; j<probability[0].length; j++) {
            if (probability[0][j]>probability[1][j]) {
                clList.add(new Data (new double[]{probability[0][j]}, j, 0));
            }
            else {
                clList.add(new Data (new double[]{probability[1][j]}, j, 1));
            }
        }
        
        for (int i=0; i<cluster; i++) {
            System.out.println("***Toxicity " + i + " contains: ");
            for (int j=0; j<clList.size(); j++) {
                if (clList.get(j).cluster()==i) {
                    System.out.println(clList.get(j).getId()+1 + "\t" + clList.get(j).getPoint().getPoint(0));
                }
                if (clList.get(j).cluster()==0) {
                    clList2.add(clList.get(j).getId()+1);
                }
            }
        }/**/
    
        for (int i=0; i<cluster; i++) {
            System.out.println(clusterCount[i]);
        }
    }
    
    public List<Integer> getList() {
        return this.clList2;
    }
}
