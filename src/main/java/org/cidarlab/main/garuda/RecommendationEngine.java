/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.util.List;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.cidarlab.main.ml.Backpropagation;
import org.cidarlab.main.ml.ExpertSystem;
import org.cidarlab.main.ml.NaiveBayes;
import org.cidarlab.main.ml.KMeansClustering;
import org.cidarlab.main.ml.LinearRegression;
import org.cidarlab.main.ml.MultipleLinearRegression;

/**
 *
 * @author mardian
 */
public class RecommendationEngine {
    
    public RecommendationEngine (String folderName) {
        
        int numOfTrials = 1;
        
        for (int ext=1; ext<=numOfTrials;  ext++) {
            
            String inputFile = "resources/" + folderName + "/input-" + ext + ".xlsx";
            
            int cluster = 2;
            int cnum = 1000;
            int pnum = 20;
            int psize = 4;
            
    //        for (double i=0.1; i<=1.0; i=i+0.2) {

                double threshold = 0.3;
    //            double threshold = i;
                
    //            for (int j=0; j<=pnum; j=j+5) {
                    
                    int toxic = 4;
    //                int toxic = j;
    //                if (j==0)
    //                    toxic = 1;

                    SpreadsheetParser parser = new SpreadsheetParser (inputFile, cnum, pnum, psize, threshold, toxic);
                    double[][] growthData = parser.getGrowth();
                    double[][] data = parser.getData();
                    double[][] countData = parser.getCount();
                    
                //    KMeansClustering kmeans = new KMeansClustering (growthData, cluster);
                //    Backpropagation backprop = new Backpropagation (growthData, cluster);
                //    ExpertSystem expert = new ExpertSystem (growthData, threshold);
               
                    List<Integer> target = parser.getList();
                    
                //  BruteForce bruteforce = new BruteForce (kmeans.getClusterData(), kmeans.isZeroToxic(), parser.getPart());
                 
                /*    System.out.println("K-Means:");
                    NaiveBayes kmbayes = new NaiveBayes (kmeans.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, kmbayes.getList()));
                    
                    System.out.println("Backprop:");
                    NaiveBayes bpbayes = new NaiveBayes (backprop.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, bpbayes.getList()));
                    
                    System.out.println("Expert:");
                    NaiveBayes expbayes = new NaiveBayes (expert.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, expbayes.getList()));*/
                
                
                
                    
                //    double[] x = new double[]{2,9,4,7,3,1,5,9,6,6,5,6,8};
                //    double[] y = new double[]{9,23,13,19,11,7,15,23,17,17,15,17,21};
                //    LinearRegression.init(x, y);
                
                    
                /*    double[][] x = { {  1,  10,  20 },
                                     {  1,  20,  40 },
                                     {  1,  40,  15 },
                                     {  1,  80, 100 },
                                     {  1, 160,  23 },
                                     {  1, 200,  18 } };
                    double[] y = { 243, 483, 508, 1503, 1764, 2129 };*/
                
                    double[] linGrowth = new double[growthData.length];
                    for(int i=0; i<linGrowth.length; i++) {
                        linGrowth[i] = growthData[i][0];
                    }
                
            /*        MultipleLinearRegression regression = new MultipleLinearRegression(countData, linGrowth);

                    double[] beta = new double[pnum];
                    for (int i=0; i<pnum; i++) {
                        beta[i] = regression.beta(i);
                    }
                    
                    double[][] reg = new double[growthData.length][2];
                    for (int i=0; i<growthData.length; i++) {
                        reg[i][0] = growthData[i][0];
                        reg[i][1] = 0;
                        for (int j=0; j<beta.length; j++) {
                            reg[i][1] += beta[j] * countData[i][j];
                        }
                    }
                    
                    int[] tracker = parser.getTracker();*/
                    
                    OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
                    double[] y = new double[]{11.0, 12.0, 13.0, 14.0, 15.0, 16.0};
                    double[][] x = new double[6][];
                    x[0] = new double[]{0, 0, 0, 0, 0};
                    x[1] = new double[]{2.0, 0, 0, 0, 0};
                    x[2] = new double[]{0, 3.0, 0, 0, 0};
                    x[3] = new double[]{0, 0, 4.0, 0, 0};
                    x[4] = new double[]{0, 0, 0, 5.0, 0};
                    x[5] = new double[]{0, 0, 0, 0, 6.0};          
                    regression.newSampleData(linGrowth, countData);
                    
                    double[] beta = regression.estimateRegressionParameters();
                    double[] residuals = regression.estimateResiduals();
                    double[][] parametersVariance = regression.estimateRegressionParametersVariance();
                    double regressandVariance = regression.estimateRegressandVariance();
                    double rSquared = regression.calculateRSquared();
                    double sigma = regression.estimateRegressionStandardError();
                    
                /*    for (int i=0; i<reg.length; i++) {
                        if (tracker[i]>0)
                            System.out.println(reg[i][0] + "\t" + reg[i][1]);
                        else
                            System.out.println(reg[i][0] + "\t\t" + reg[i][1]);
                    }*/
                    //System.out.println(reg[1][0] + "     " + reg[1][1]);
                    //System.out.println(reg[2][0] + "     " + reg[2][1]);
                    //System.out.println(reg[3][0] + "     " + reg[3][1]);
                    //System.out.println(reg[4][0] + "     " + reg[4][1]);
                    
                    
                    //double[][] yy = {{243,243}, {483,483}, {508,508}, {1503,1503}, {1764,1764}, {2129,2129}};
                    
                //    KMeansClustering nmeans = new KMeansClustering (reg, cluster);
    //            }
    //        }
        }
    }
    
    public int error (List<Integer> target, List<Integer> actual) {
        
        int error = 0;
        for (int x=0; x<target.size(); x++) {
            if (!actual.contains(target.get(x))) {
                error++;
            }
        }
        return error;
    }
    
}
