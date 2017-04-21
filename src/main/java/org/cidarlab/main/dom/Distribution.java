/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.dom;

/**
 *
 * @author mardian
 */
public class Distribution {
    
    private double mu;
    private double sigma;
    
    public Distribution () {
        this(0.0, 1.0);
    }
    
    public Distribution (double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }
    
    public void setMu (double mu) {
        this.mu = mu;
    }
    
    public void setSigma (double sigma) {
        this.sigma = sigma;
    }
    
    public double getMu () {
        return this.mu;
    }
    
    public double getSigma () {
        return this.sigma;
    }
        
}
