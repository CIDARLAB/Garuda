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

public class Feature
{
    private int id;
    private Vector vector;
    private int label;

    public Feature (int id, double[] newX) {

        this.id = id;
        this.vector = new Vector (newX);
    }

    public Feature (int id, double[] newX, int cluster) {

        this.id = id;
        this.vector = new Vector (newX);
        this.label = cluster;
    }

    public int getId () {

        return this.id;
    }

    public Vector getVector () {

        return this.vector;
    }

    public void setCluster (int clusterNumber) {

        this.label = clusterNumber;
    }

    public int getCluster () {

        return this.label;
    }
}
