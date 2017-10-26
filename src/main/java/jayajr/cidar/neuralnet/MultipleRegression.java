/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jayajr.cidar.neuralnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.linear.SingularMatrixException;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;
import org.apache.commons.math3.util.FastMath;

/**
 *
 * @author mardian
 */
public class MultipleRegression {

    public MultipleRegression() {
    }

    public List<String> pyRegression(String file) {
        
        List<String> output = new ArrayList<String>();

        String path = "resources/" + file;
        try {

            //Process p = Runtime.getRuntime().exec("python " + path);
            ProcessBuilder pb = new ProcessBuilder("python", path);
            //pb.redirectOutput(Redirect.INHERIT);
            Process p = pb.start();
            
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            //BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            String ret = in.readLine();
            while (ret != null) {
                System.out.println(ret);
                output.add(ret);
                //message += ret + "\n";
                ret = in.readLine();
            }
            System.out.println("***Python executed!!!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return output;
        }
    }

    public void jvRegression(double[][] data, double[] label) {

        try {

            OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();

            /*double[][] x = {
                { 1, 1, 1, 0 },
                { 1, 0, 1, 1 },
                { 1, 0, 1, 1 },
                { 1, 0, 1, 0 },
                { 1, 1, 1, 1 },
                { 1, 1, 1, 0 },
                { 1, 0, 1, 0 },
                { 1, 1, 1, 0 },
                { 1, 1, 0, 0 },
                { 1, 0, 1, 0 },
                { 0, 0, 1, 1 },
                { 1, 0, 1, 1 },
                { 1, 1, 0, 0 },
                { 0, 1, 1, 1 },
                { 0, 1, 1, 0 },
                { 1, 1, 0, 1 },
                { 0, 1, 1, 0 },
                { 1, 1, 0, 0 },
                { 1, 0, 1, 0 },
                { 1, 1, 1, 0 }
            };

            double[] y = {1.29059717, 0.70544841, 0.75630101, 0.97549496, 0.43153817, 0.44917647,
                0.99678689, 0.90981844, 0.80678932, 0.83838037, 1.34614472, 1.43276749, 0.93183209,
                0.89969879, 0.98147421, 0.66831309, 1.28875777, 0.958625, 0.66133257,  0.71738615};

            System.out.print("**Label**\n[ ");
            for(int i = 0; i < label.length-1; i++) {
                System.out.print(label[i] + ", ");
            }
            System.out.println(label[label.length-1] + " ]");

            System.out.print("**Data**\n[ ");
            for(int i = 0; i < data.length; i++) {
                System.out.print("[ ");
                for(int j = 0; j < data[0].length-1; j++) {
                    System.out.print(data[i][j] + ", ");
                }
                System.out.println(data[i][data[0].length-1] + " ],");
            }*/
            ols.newSampleData(label, data);
            ols.setNoIntercept(true);

            double[] beta = ols.estimateRegressionParameters();
            double[] residual = ols.estimateResiduals();
            double[] errors = ols.estimateRegressionParametersStandardErrors();
            //double[] tstat = new double[beta.length];

            int rdf = residual.length - beta.length;

            TDistribution tdist = new TDistribution(rdf);

            System.out.println("***Regression***");
            for (int i = 0; i < beta.length; i++) {
                //System.out.println(beta[i]);
                double tstat = beta[i] / errors[i];
                double pvalue = tdist.cumulativeProbability(-FastMath.abs(tstat)) * 2;

                System.out.println("p-value(" + i + ") : " + pvalue);
            }
        } catch (SingularMatrixException ex) {
            System.out.println("Matrix is singular");
        }
    }
}
