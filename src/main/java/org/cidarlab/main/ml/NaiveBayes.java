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
    
    private List<Data> classList;
    private List<Integer> toxicList;
    private double[][] probability;
    
    private List<Data> clusterData;
    private double[][] matrixData;
    private int cluster;
    private int participant;
    private int pnum;
    
    public NaiveBayes (List<Data> clusterData, double[][] inputData, int cluster, int participant, int pnum) {
        
        this.clusterData = clusterData;
        //this.matrixData = matrixData;
        this.cluster = cluster;
        this.participant = participant;
        this.pnum = pnum;
        
        classList = new ArrayList<Data>();
        toxicList = new ArrayList<Integer>();
        
        probability = new double[cluster][pnum];   //P(part|setCluster)
        
        setMatrixData(inputData);
        
        assignProbabilities ();
        classify ();
    }
    
    
    public void assignProbabilities () {
        
        int[] clusterCount = new int[cluster];   //counter for each setCluster size
        double[] probCount = new double[cluster];   //P(setCluster)
        
        for (int i=0; i<cluster; i++) {
            
            int count = 0;
            int[] partCount = new int[pnum];
        
            for(int j=0; j<matrixData.length; j++) {
                
                if (clusterData.get(j).getCluster()==i) {
                    
                    clusterCount[i]++;
                    
                    for(int k=0; k<matrixData[0].length; k++) {
                        if (matrixData[j][k]==1.0) {
                            partCount[k]++;
                            count++;
                        }
                    }
                }
            } // j
            
            //P(setCluster) probability of one cluster to occur
            probCount[i] = (double)clusterCount[i] / (double)matrixData.length;
            
            //P(part|setCluster) probability of one part to occur given the cluster
            for (int j=0; j<partCount.length; j++) {
                probability[i][j] = (double)(partCount[j]+1) / (double)(count+participant);
            }
        }
    }
    
    public void classify () {
        
        //Classification (measure distance between two setCluster - only for two clusters)
        for (int j=0; j<probability[0].length; j++) {
            
            double p1 = probability[0][j];
            double p2 = probability[1][j];
            
            if (p1>p2) {
                classList.add(new Data (new double[]{p1-p2}, j, 0));
            }
            else {
                classList.add(new Data (new double[]{p2-p1}, j, 1));
            }
        }
        
        for (int i=0; i<cluster; i++) {
            System.out.println("***Toxicity " + i + " contains: ");
            for (int j=0; j<classList.size(); j++) {
                if (classList.get(j).getCluster()==i) {
                    System.out.println(classList.get(j).getId()+1 + "\t" + classList.get(j).getVector().getDimension(0));
                }
                if (classList.get(j).getCluster()==0) {
                    toxicList.add(classList.get(j).getId()+1);
                }
            }
        }
    }
    
    public void setMatrixData(double[][] inputData) {
        
        double[][] datacopy = new double[inputData.length][pnum];
        for(int i=0; i<inputData.length; i++) {
            for(int j=2; j<inputData[0].length; j++) {  //j=2 if inputData includes growth rate and performance parameters
                datacopy[i][((int)inputData[i][j]-1)] = 1; //shouldn't it be ++?
            }
        }
        this.matrixData = datacopy;
    }
    
    public List<Integer> getToxicList() {
        return this.toxicList;
    }
}
