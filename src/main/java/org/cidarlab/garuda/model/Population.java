/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.model;

import java.util.Random;

/**
 *
 * @author mardian
 */
public class Population {
    
    private double[] healthy;
    private double[] toxic;
    
    public Population (int healthy_size, int toxic_size, double healthy_mean, double toxic_mean, double healthy_sd, double toxic_sd) {
        
        this.healthy = new double[healthy_size];
        this.toxic = new double[toxic_size];
        
        Random healthy_rand = new Random ();
        Random toxic_rand = new Random ();
        
        for (int i=0; i<healthy.length; i++) {
            healthy[i] = healthy_rand.nextGaussian() * healthy_sd + healthy_mean;
        }
        
        for (int i=0; i<toxic.length; i++) {
            toxic[i] = toxic_rand.nextGaussian() * toxic_sd + toxic_mean;
        }
        
    }
}
