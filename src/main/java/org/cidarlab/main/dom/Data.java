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

public class Data
{
    private int id;
    private Point point;
    private int mCluster;

    public Data (double[] newX, int id) {

        this.id = id;
        this.point = new Point (newX);
    }

    public Data (double[] newX, int id, int cluster) {

        this.id = id;
        this.point = new Point (newX);
        this.mCluster = cluster;
    }

    public int getId () {

        return this.id;
    }

    public Point getPoint () {

        return this.point;
    }

    public void cluster (int clusterNumber) {

        this.mCluster = clusterNumber;
    }

    public int cluster () {

        return this.mCluster;
    }
}
