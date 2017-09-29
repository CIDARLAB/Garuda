/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.dom;

import java.util.Comparator;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */

public class Feature
{
    @Getter
    @Setter
    private int id;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private Vector vector;
    
    private double[] dimension;
    
    @Getter
    @Setter
    private double label;

    @Getter
    @Setter
    private int cluster;
    
    public Feature (int id, double[] newX) {

        this.id = id;
        this.vector = new Vector (newX);
    }

    public Feature (int id, double[] newX, int cluster) {

        this.id = id;
        this.vector = new Vector (newX);
        this.cluster = cluster;
    }

    public Feature (int id, String name, double[] newX, double newY, int cluster) {

        this.id = id;
        this.name = name;
        this.setDimension (newX);
        this.label = newY;
        this.cluster = cluster;
    }

    public void setDimension(double[] vector) {
        
        dimension = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            dimension[i] = vector[i];
        }
    }
    
    public void setDimensionAt(int idx, double value) {
        
        dimension[idx] = value;
    }
    
    public double[] getDimension() {
        
        double[] output = new double[dimension.length];
        for (int i = 0; i < dimension.length; i++) {
            output[i] = dimension[i];
        }
        return output;
    }
     
    public double getDimensionAt(int idx) {
        
        return dimension[idx];
    }
    
    /*public int getId () {

        return this.id;
    }

    public Vector getVector () {

        return this.vector;
    }

    public void setCluster (int clusterNumber) {

        this.cluster = clusterNumber;
    }

    public int getCluster () {

        return this.cluster;
    }*/
}
