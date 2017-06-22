/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.model;

/**
 *
 * @author mardian
 */
public class Vector {
        
    private double[] dimensions;

    public Vector (double[] newX) {

        this.dimensions = newX;
    }

    public void setDimension (double newX, int idx) {

        dimensions[idx] = newX;
    }

    public double getDimension (int idx) {

        return dimensions[idx];
    }

    public int getSize () {

        return dimensions.length;
    }

    public void print () {

        String output = "*** values: ";
        for (int i=0; i<dimensions.length; i++) {
            output += "(" + dimensions[i] + "), ";
        }
        System.out.println (output);
    }
}
