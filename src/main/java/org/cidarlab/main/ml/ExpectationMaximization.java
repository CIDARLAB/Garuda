/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import org.cidarlab.main.dom.Distribution;

/**
 *
 * @author mardian
 */
public class ExpectationMaximization {
    
    private Distribution[] distribution;
    private double[] p_prior;
    //private int num_of_distribution;
    
    private final int max_iter = 100;
    private final double error = 0.0005;
    
    //create one distribution with mu=0 and sigma=1
    public ExpectationMaximization () {
        this.distribution = new Distribution[]{new Distribution()};
        this.p_prior = new double[]{1.0};
    }
    
    //create numbers of distribution with given arrays of mu and sigma
    public ExpectationMaximization (double[] mu, double[] sigma, boolean uniform) {
        if (mu.length==sigma.length) {
            
            this.distribution = new Distribution[mu.length];
            this.p_prior = new double[mu.length];
            
            for (int i=0; i<mu.length; i++) {
                distribution[i] = new Distribution(mu[i], sigma[i]);
                if (uniform) {
                    this.p_prior[i] = 0.5;
                }
            }
        }
        else {
            System.out.println("... Something is not right. mu and sigma length are not the same!");
        }
    }
    
    //create n-number distributions with random mu and sigma
    public ExpectationMaximization (int num_of_distribution, boolean uniform, boolean randomize) {
        
        this.distribution = new Distribution[num_of_distribution];
        this.p_prior = new double[num_of_distribution];
        
        if (!randomize) {
            double width = 1 / num_of_distribution;
            double sigma = 0.2;
            for (int i = 0; i < num_of_distribution; i++) {
                //pick random mu and sigma between 0.0 and 1.0
                double mu = 0.0 + (i*width);
                this.distribution[i] = new Distribution(mu, sigma);

                if (uniform) {
                    this.p_prior[i] = 0.5;
                }
            }
        }
        else {
            for (int i = 0; i < num_of_distribution; i++) {
                //pick random mu and sigma between 0.0 and 1.0
                double mu = Math.random();
                double sigma = Math.random();
                this.distribution[i] = new Distribution(mu, sigma);

                if (uniform) {
                    this.p_prior[i] = 0.5;
                }
            }
        }
    }
    
    private double pdf (double x) {
        return Math.exp(-x*x/2)/Math.sqrt(2*Math.PI);
    }

    // return pdf(x, mu, sigma) = Gaussian pdf with mean mu and stddev sigma
    private double pdf (double x, double mu, double sigma) {
        return pdf((x-mu)/sigma)/sigma;
    }
    
    public int iterate (double[] x) {
        
        boolean end_of_iteration = false;
        boolean valid_sigma = true;
        
        //int max_iter = 1000;
        System.out.println("Initially picked distributions:");
        for (int k=0; k < this.distribution.length; k++) {
            System.out.println("** " + k + "-th: mu = " + this.distribution[k].getMu() + ", sigma = " + this.distribution[k].getSigma());
        }
        
        for (int iter=0; iter<this.max_iter && !end_of_iteration && valid_sigma ; iter++) {
        
            
            System.out.println("*****" + iter + "*****");
            
            double[][] p_x_dist = new double[x.length][this.distribution.length];   //P(Xi|distribution)
            double[][] p_dist_x = new double[this.distribution.length][x.length];   //P(distribution|Xi)

            double[] p_x = new double[x.length];
            double[] p_dist = new double[this.distribution.length];
            
            //compute P(Xi|distribution)
            for (int i=0; i < x.length; i++) {
                p_x[i] = 0.0;
                for (int j = 0; j < this.distribution.length; j++) {
                    p_x_dist[i][j] = pdf(x[i], distribution[j].getMu(), distribution[j].getSigma());
                    p_x[i] += (p_x_dist[i][j] * this.p_prior[j]);
                }
            }
            
            //compute P(distribution|Xi)
            for (int i = 0; i < this.distribution.length; i++) {
                p_dist[i] = 0.0;
                
                System.out.println("**Population " + i + "***");
                    
                for (int j = 0; j < x.length; j++) {
                    p_dist_x[i][j] = (p_x_dist[j][i] * this.p_prior[i] / p_x[j]);
                    
                    System.out.println(x[j] + "\t" + p_dist_x[i][j]);
                    
                    p_dist[i] += p_dist_x[i][j];
                }
            }

            //re-compute mu for each distribution
            for (int i=0; i < this.distribution.length; i++) {
                double p_temp = 0.0;
                for (int j = 0; j < x.length; j++) {
                    p_temp += (p_dist_x[i][j] * x[j]);
                }
                double mu_temp = p_temp / p_dist[i];
                
                if ((mu_temp-this.distribution[i].getMu())*(mu_temp-this.distribution[i].getMu())>(this.error*this.error)) {
                    
                    System.out.println ("++ " + i + "-th " + mu_temp + "  " + this.distribution[i].getMu() + "  " + (this.error*this.error));
                    this.distribution[i].setMu(mu_temp);
                }
                else {
                    end_of_iteration = true;
                }
            }
            
            //re-compute sigma for each distribution
            for (int i = 0; i < this.distribution.length; i++) {
                double p_temp = 0.0;
                for (int j = 0; j < x.length; j++) {
                    p_temp += (p_dist_x[i][j] * (x[j] - this.distribution[i].getMu()) * (x[j] - this.distribution[i].getMu()));
                }
                double sigma_temp = p_temp / p_dist[i];
                
                if ((sigma_temp-this.distribution[i].getSigma())*(sigma_temp-this.distribution[i].getSigma())<=(this.error*this.error) || sigma_temp <= 0.01) {
                    
                    end_of_iteration = true;
                }
                else {
                    
                    System.out.println("-- " + i + "-th " + sigma_temp + "  " + this.distribution[i].getSigma() + "  " + (sigma_temp-this.distribution[i].getSigma()) + " " + (this.error*this.error));
                    this.distribution[i].setSigma(sigma_temp);
                }
                /*else {
                    end_of_iteration = true;
                }
                if (sigma_temp <= 0.01) {
                    end_of_iteration = true;
                }*/
                /*if (sigma_temp <= 0.0) {
                    valid_sigma = false;
                    System.out.println ("*** Warning: EM terminated with zero sigma...!!!");
                }*/
            }
            
            //System.out.println ("**num of iter: " + iter);
        }
        
        //return -1 if ended with sigma<=0.0, 1 if no change in mu&sigma, 0 if reaches max iteration
        
        if (!valid_sigma) return -1;
        else if (end_of_iteration) return 1;
        return 0;
    }
    
    public Distribution[] getDistribution() {
        
        return this.distribution;
    }
    
}
