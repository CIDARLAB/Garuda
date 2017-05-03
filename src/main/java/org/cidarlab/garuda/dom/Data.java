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

public class Data
{
    private int id;
    private Vector vector;
    private int cluster;

    public Data (double[] newX, int id) {

        this.id = id;
        this.vector = new Vector (newX);
    }

    public Data (double[] newX, int id, int cluster) {

        this.id = id;
        this.vector = new Vector (newX);
        this.cluster = cluster;
    }

    public int getId () {

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
    }
}
