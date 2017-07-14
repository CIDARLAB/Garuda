/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.model;

import org.apache.commons.math3.linear.EigenDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.linear.SingularValueDecomposition;
import org.apache.commons.math3.stat.correlation.Covariance;

/**
 *
 * @author mardian
 */
public class PrincipleComponentAnalysis1 {
    
    public static double[][] pca(double[][] input, int reduction) {
        
        /*RealMatrix a = MatrixUtils.createRealMatrix(new double[][]{
            {0.69, 0.49},
            {-1.31, -1.21},
            {0.39, 0.99},
            {0.09, 0.29},
            {1.29, 1.09},
            {0.49, 0.79},
            {0.19, -0.31},
            {-0.81, -0.81},
            {-0.31, -0.31},
            {-0.71, -1.01},
        });*/
        
        RealMatrix a = MatrixUtils.createRealMatrix(input);
        
        int d_ori = a.getColumnDimension();
        int d_red = d_ori-reduction;
        
        double[][] output = new double[input.length][input[0].length-reduction];
        
        //compute covariance matrix
        RealMatrix cov = new Covariance(a).getCovarianceMatrix();
    //    System.out.println(cov);
        
    //    System.out.println("***************");
        
        //SVD-based method
        RealMatrix u = new SingularValueDecomposition(cov).getU();
    //    System.out.println(u);
        
        RealMatrix u_red = u.getSubMatrix(0, d_ori-1, 0, d_red-1);
    //    System.out.println(u_red);
        
        RealVector x1 = u_red.transpose().operate(a.getRowVector(5));
    //    System.out.println(x1);
        
    //    System.out.println("***************");
        
        //eigen vectors-based method
        RealMatrix u_eig_red = MatrixUtils.createRealMatrix(d_red, d_ori);
        
        EigenDecomposition e = new EigenDecomposition(cov);
        double[] arrayEigenValue = e.getRealEigenvalues();
        //for (int i=0; i<e.getRealEigenvalues().length; i++) {
        for (int i=0; i<d_red; i++) {
    //        System.out.println ("eigenValue with index " + i + " " + arrayEigenValue[i]);
            RealVector arrayEigenVector = e.getEigenvector(i);
            u_eig_red.setRowVector(i, arrayEigenVector);
            //for (int j = 0; j < arrayEigenVector.getDimension(); j++) {
                //System.out.print(arrayEigenVector.getEntry(j) + " ");
    //            System.out.println (arrayEigenVector);
            //}
            //System.out.println();
        }
    //    System.out.println(u_eig_red);
        
        for (int i=0; i<a.getRowDimension(); i++)
        {
            RealVector x1_eig = u_eig_red.operate(a.getRowVector(i));
    //        System.out.println(x1_eig);
            output[i] = x1_eig.toArray();
        }
        
        return output;
        
    }

}
