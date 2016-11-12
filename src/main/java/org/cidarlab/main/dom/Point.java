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
public class Point {
        
    private double[] points;

    public Point (double[] newX) {

        this.points = newX;
    }

    public void setPoint (double newX, int idx) {

        points[idx] = newX;
    }

    public double getPoint (int idx) {

        return points[idx];
    }

    public int pointLength () {

        return points.length;
    }

    public void printPoint () {

        String output = "*** values: ";
        for (int i=0; i<points.length; i++) {
            output += "(" + points[i] + "), ";
        }
        System.out.println (output);
    }
}
