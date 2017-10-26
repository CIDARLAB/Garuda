/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jayajr.cidar.neuralnet;

/**
 *
 * @author mardian
 */
public class Vector {

    private double[] vector;

    public Vector(double[] newX) {

        this.vector = newX;
    }

    public void setDimension(double newX, int idx) {

        vector[idx] = newX;
    }

    public double getDimension(int idx) {

        return vector[idx];
    }

    public double[] getArray() {

        double[] dim_copy = new double[vector.length];
        
        for(int i = 0; i < dim_copy.length; i ++) {
            dim_copy[i] = vector[i];
        }
        
        return dim_copy;
    }

    public int getSize() {

        return vector.length;
    }

    @Override
    public String toString() {

        String output = "Vector: ";
        for (int i = 0; i < vector.length; i++) {
            output += "(" + vector[i] + "), ";
        }
        return output;
    }
}
