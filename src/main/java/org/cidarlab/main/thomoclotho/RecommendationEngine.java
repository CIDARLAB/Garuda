/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.util.List;
import org.cidarlab.main.ml.Backpropagation;
import org.cidarlab.main.ml.ExpertSystem;
import org.cidarlab.main.ml.NaiveBayes;

/**
 *
 * @author mardian
 */
public class RecommendationEngine {
    
    public RecommendationEngine () {
        
    }
    
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
                    Backpropagation backprop = new Backpropagation (growthData, cluster);
                    ExpertSystem expert = new ExpertSystem (growthData, threshold);
               
                    List<Integer> target = parser.getList();
                    
                //  BruteForce bruteforce = new BruteForce (kmeans.getClusterData(), kmeans.isZeroToxic(), parser.getPart());
                 
                /*    System.out.println("K-Means:");
                    NaiveBayes kmbayes = new NaiveBayes (kmeans.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, kmbayes.getList()));*/
                    
                    System.out.println("Backprop:");
                    NaiveBayes bpbayes = new NaiveBayes (backprop.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, bpbayes.getList()));
                    
                    System.out.println("Expert:");
                    NaiveBayes expbayes = new NaiveBayes (expert.getClusterData(), data, cluster, parser.getParticipant());
                    System.out.println("#error: " + error (target, expbayes.getList()));
                
                
                
                    
            /*    //    double[] x = new double[]{2,9,4,7,3,1,5,9,6,6,5,6,8};
                //    double[] y = new double[]{9,23,13,19,11,7,15,23,17,17,15,17,21};
                //    LinearRegression.init(x, y);
                
                    
                    double[][] x = {{  1.0,  1.0,  1.0,  0.0 },
                                    {  1.0,  0.0,  1.0,  1.0 },
                                    {  1.0,  0.0,  1.0,  1.0 },
                                    {  1.0,  0.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  1.0,  1.0 },
                                    {  1.0,  1.0,  1.0,  0.0 },
                                    {  1.0,  0.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  0.0,  0.0 },
                                    {  1.0,  0.0,  1.0,  0.0 },
                                    {  0.0,  0.0,  1.0,  1.0 },
                                    {  1.0,  0.0,  1.0,  1.0 },
                                    {  1.0,  1.0,  0.0,  0.0 },
                                    {  0.0,  1.0,  1.0,  1.0 },
                                    {  0.0,  1.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  0.0,  1.0 },
                                    {  0.0,  1.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  0.0,  0.0 },
                                    {  1.0,  0.0,  1.0,  0.0 },
                                    {  1.0,  1.0,  1.0,  0.0 }};
                    double[] y = {  1.290597, 0.705448, 0.756301, 0.975495, 0.431538,
                                    0.449176, 0.996787, 0.909818, 0.806789, 0.838380,
                                    1.346145, 1.432767, 0.931832, 0.899699, 0.981474,
                                    0.668313, 1.288758, 0.958625, 0.661333, 0.717386 };
                    
                    double[] actual = new double[growthData.length];
                    for(int i=0; i<actual.length; i++) {
                        actual[i] = growthData[i][0];
                    }
                    
                //    MultipleLinearRegression regression = new MultipleLinearRegression(countData, actual);
                    
                    OLSMultipleLinearRegression regression = new OLSMultipleLinearRegression();
                    regression.newSampleData(y, x);
                    double[] beta = regression.estimateRegressionParameters();
                    double[] stderr = regression.estimateRegressionParametersStandardErrors();
                    
                    double[] predicted = new double[x.length];
                    for (int i=0; i<x.length; i++) {
                        predicted[i] = beta[0];
                        for (int j=1; j<beta.length; j++) {
                            predicted[i] += (beta[j] * x[i][j-1]);
                        }
                    }
                    
                    
                    for (int i=0; i<stderr.length; i++) {
                        System.out.println ("Std: " + stderr[i]);
                    }
                    
                    double sse = 0.0;
                    for (int i=0; i<y.length; i++) {
                        sse += ((y[i] - predicted[i]) * (y[i] - predicted[i]));
                    }
                    double mse = sse / (pnum - psize);
                    
                    ////////////
                    
                    int p = x[0].length;
                    
                    double[][] testBeta = new QRDecomposition(new Matrix(x)).solve(new Matrix(y, y.length)).getArray();
                    double[] testBeta2 = new org.apache.commons.math3.linear.QRDecomposition(new Array2DRowRealMatrix(x)).getSolver().solve(new ArrayRealVector(y)).toArray();
                    
                    
                    for (int i=0; i<testBeta2.length; i++) {
                            System.out.println("$////////////" + beta[i] + "   " + testBeta2[i]);
                    }
                    
                    
                    Matrix Raug = new QRDecomposition(new Matrix(x)).getR().getMatrix(0, p-1 , 0, p-1);
                    LUDecomposition lud = new LUDecomposition(Raug);
                    int dimension = lud.getPivot().length;
                    Matrix B = Matrix.identity(dimension, dimension);
                    Matrix Rinv = lud.solve(B);
                    
                    double[][] betaVar = Rinv.times(Rinv.transpose()).getArray();
                    
                    RealMatrix Raug2 = new org.apache.commons.math3.linear.QRDecomposition(new Array2DRowRealMatrix(x)).getR().getSubMatrix(0, p-1 , 0, p-1);
                    RealMatrix Rinv2 = new org.apache.commons.math3.linear.LUDecomposition(Raug2).getSolver().getInverse();
                    
                    double[][] betaVar2 = Rinv2.multiply(Rinv2.transpose()).getData();
                    
                    for (int i=0; i<betaVar.length; i++) {
                        for (int j=0; j<betaVar[0].length; j++) {
                            System.out.println("$$$" + betaVar[i][j] + "      " + betaVar2[i][j]);
                        }
                    }
                    
                    int len = betaVar[0].length;
                    double[] rslt = new double[len];
                    for (int i = 0; i < len; i++) {
                        rslt[i] = Math.sqrt(mse * betaVar[i][i]);
                        System.out.println("******" + rslt[i]);
                    }
                    
                    
                    //////////
                    
                    RealVector vb = new ArrayRealVector(beta);
                    
                //   double[] stderr = regression.estimateRegressionParametersStandardErrors();
                /*     for (int i=0; i<stderr.length; i++) {
                        System.out.println("***" + stderr[i]);
                    }

                /*    double[] beta = new double[pnum+1];
                    for (int i=0; i<=pnum; i++) {
                        beta[i] = regression.beta(i);
                        System.out.println("***" + i);
                    }
                    
                    int[] tracker = parser.getTracker();
                    
                   for (int i=0; i<predicted.length; i++) {
                        if (tracker[i]>0)
                            System.out.println(actual[i] + "\t" + predicted[i]);
                        else
                            System.out.println(actual[i] + "\t\t" + predicted[i]);
                    }
                   
                    System.out.println(TestStatistic.chisq(predicted, actual));*/
            
                    //System.out.println(predicted[1][0] + "     " + predicted[1][1]);
                    //System.out.println(predicted[2][0] + "     " + predicted[2][1]);
                    //System.out.println(predicted[3][0] + "     " + predicted[3][1]);
                    //System.out.println(predicted[4][0] + "     " + predicted[4][1]);
                    
                    
                    //double[][] yy = {{243,243}, {483,483}, {508,508}, {1503,1503}, {1764,1764}, {2129,2129}};
                    
                //    KMeansClustering nmeans = new KMeansClustering (predicted, cluster);
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
