/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.dom;

/**
 *
 * @author mardian
 */
public class Vector {

    private double[] dimensions;

    public Vector(double[] newX) {

        this.dimensions = newX;
    }

    public void setDimension(double newX, int idx) {

        dimensions[idx] = newX;
    }

    public double getDimension(int idx) {

        return dimensions[idx];
    }

    public double[] getArray() {

        double[] dim_copy = new double[dimensions.length];
        
        for(int i = 0; i < dim_copy.length; i ++) {
            dim_copy[i] = dimensions[i];
        }
        
        return dim_copy;
    }

    public int getSize() {

        return dimensions.length;
    }

    @Override
    public String toString() {

        String output = "Vector: ";
        for (int i = 0; i < dimensions.length; i++) {
            output += "(" + dimensions[i] + "), ";
        }
        return output;
    }
}
