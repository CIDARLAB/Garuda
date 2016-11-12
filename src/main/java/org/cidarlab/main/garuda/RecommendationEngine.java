/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.util.List;
import org.cidarlab.main.dom.Data;
import org.cidarlab.main.ml.Backpropagation;
import org.cidarlab.main.ml.ExpertSystem;
import org.cidarlab.main.ml.NaiveBayes;
import org.cidarlab.main.ml.KMeansClustering;

/**
 *
 * @author mardian
 */
public class RecommendationEngine {
    
    public RecommendationEngine (String inputFile) {
        
        //Backpropagation.neuralNetwork();
        //init(inputFile);
    }
    
    public RecommendationEngine () {
        
        String folderName = "20161108/";
        int numOfTrials = 1;
        
        for (int ext=1; ext<=numOfTrials;  ext++) {
            
            String inputFile = "resources/" + folderName + "input-" + ext + ".xlsx";
            int parts = 4;
            int pnum = 20;
            int cnum = 1000;
            int cluster = 2;
            
    //        System.out.println(parts);
    //        System.out.println(pnum);
    //        System.out.println(cnum);
    //        System.out.println();
            
    //        for (double i=0.1; i<=1.0; i=i+0.2) {

                double threshold = 0.3;
    //            double threshold = i;
                
    //            for (int j=0; j<=pnum; j=j+5) {
                    
                    int numtoxic = 9;
    //                int numtoxic = j;
    //                if (j==0)
    //                    numtoxic = 1;

    //                System.out.println((numtoxic*100)/pnum);
    //                System.out.println(threshold);

                    SpreadsheetParser parser = new SpreadsheetParser(inputFile, parts, pnum, cnum, threshold, numtoxic);
                    //CollaborativeFake.init(parser.getData());

                    double[][] growthData = parser.getGrowth();
                    /*for (int i=0; i<growthData.length; i++) {
                        for (int j=0; j<growthData[0].length; j++) {
                            //if (tempData[i][j]==0.0)
                            //    tempData[i][j] = 1.0;
                            System.out.print(growthData[i][j] + "\t");
                        }
                        System.out.println();
                    }*/
                    
                    Backpropagation nn = new Backpropagation (growthData, cluster);
                    
                    
                    /*List<Data> clusterData = nn.getData();
                    
                    for (int i=0; i<cluster; i++) {
                        System.out.println ("+Cluster " + i + " consists of: ");
                        for (int j=0; j<clusterData.size(); j++) {
                            if (clusterData.get(j).cluster()==i) {
                                System.out.println (clusterData.get(j).getId());
                            }
                        }
                    }*/

                    /*double[][] subsetData = new double[tempData.length][2];
                    for (int i=0; i<subsetData.length; i++) {
                        for (int j=0; j<subsetData[0].length; j++) {
                            subsetData[i][j] = tempData[i][j];
                        }
                    }*/

                    double[][] nbData = parser.getPartCount();
                    
                    //double[][] inputData = new double[][] {{1.0, 1.0}, {1.5, 2.0}, {3.0, 4.0}, {5.0, 7.0}, {3.5, 5.0}, {4.5, 5.0}, {3.5, 4.5}};

                //    KMeansClustering clustering = new KMeansClustering (cluster, growthData);
                //  BruteForce bruteforce = new BruteForce (clustering.getData(), clustering.isZeroToxic(), parser.getPartData());
                
                //    ExpertSystem exp = new ExpertSystem (growthData, cluster, threshold);
                
                //   System.out.println("K-Means:");
                //    NaiveBayes kmeans = new NaiveBayes (clustering.getData(), nbData, cluster, parser.getNonEmpty());
                    
                    
                    System.out.println("Backprop:");
                    NaiveBayes backprop = new NaiveBayes (nn.getData(), nbData, cluster, parser.getNonEmpty());
                
                    
                    List<Integer> l1 = parser.getList();
                    List<Integer> l2 = backprop.getList();
                    
                    
                    int error = 0;
                    System.out.println ();
                    for (int x=0; x<l1.size(); x++) {
                        if (!l2.contains(l1.get(x))) {
                            error++;
                        }
                    }
                        System.out.println ("xxxxx" + error);
                    
                //    System.out.println("Expert:");
                //    NaiveBayes expbayes = new NaiveBayes (exp.getData(), nbData, cluster, parser.getNonEmpty());
                
    //            }
    //        }
        }
    }
    
    /*public String toString() {
        return ""; //New Recommendation Engine is succesfully created";
    }*/
    
}
