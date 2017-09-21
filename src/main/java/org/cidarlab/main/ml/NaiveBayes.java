/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import org.cidarlab.main.dom.Data;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.main.dom.Feature;
import org.cidarlab.main.dom.Vector;

/**
 *
 * @author mardian
 */
public class NaiveBayes {

    @Getter
    @Setter
    private List<Feature> classList;
    
    private List<Integer> toxicList;
    private double[][] probability;

    private List<Feature> features;
    //private double[][] matrixData;
    
    @Getter
    @Setter
    private int classes;
    private int participants;
    private int pnum;

    public NaiveBayes(List<Feature> features, int classes, int parts) {

        this.features = features;
        this.classes = classes;
        this.participants = parts;
        this.pnum = parts;

        //setMatrixData(inputData, 2);      //uncomment this if inputData contains classes in the first offset columns
        //this.matrixData = inputData;        //use inputData as it is (if input data already contains part-count) and without classes

        classList = new ArrayList<Feature>();
        toxicList = new ArrayList<Integer>();

        probability = new double[classes][pnum];    //P(part|setCluster)

        init();
        classify();
    }

    public void init() {

        int num_of_constructs = features.size();
        
        int[] clusterCount = new int[classes];      //counter for each setCluster size
        double[] probCount = new double[classes];   //P(setCluster)

        for (int i = 0; i < classes; i++) {

            int count = 0;
            int[] partCount = new int[pnum];

            for (int j = 0; j < num_of_constructs; j++) {

                if (features.get(j).getCluster() == i) {

                    clusterCount[i]++;
                    Vector features = this.features.get(j).getVector();
                    
                    for (int k = 0; k < features.getSize(); k++) {
                        if (features.getDimension(k) == 1.0) {
                            partCount[k]++;
                            count++;
                        }
                    }
                }
            }

            //P(setCluster)
            probCount[i] = (double) clusterCount[i] / (double) num_of_constructs;

            //P(part|setCluster)
            for (int j = 0; j < partCount.length; j++) {
                probability[i][j] = (double) (partCount[j] + 1) / (double) (count + participants);
            }
        }
    }

    public void classify() {

        //Classification (measure distance between two setCluster - only for two clusters)
        for (int j = 0; j < probability[0].length; j++) {

            double p1 = probability[0][j];
            double p2 = probability[1][j];

            if (p1 > p2) {
                classList.add(new Feature(j, new double[]{p1 - p2, p1, p2}, 0));
            } else {
                classList.add(new Feature(j, new double[]{p2 - p1, p2, p1}, 1));
            }
        }
        
        //printing
        /*for (int i = 0; i < classes; i++) {
            System.out.println("***Toxicity " + i + " contains: ");
            for (int j = 0; j < classList.size(); j++) {
                if (classList.get(j).getCluster() == i) {
                    System.out.println(classList.get(j).getId() + 1 + "\t" + classList.get(j).getVector().getDimension(0));
                }
                //add to the toxic list
                if (classList.get(j).getCluster() == 0) {
                    toxicList.add(classList.get(j).getId() + 1);
                }
            }
        }*/
    }

    /*public void setMatrixData(double[][] inputData, int offset) {

        double[][] datacopy = new double[inputData.length][pnum];
        for (int i = 0; i < inputData.length; i++) {
            for (int j = offset; j < inputData[0].length; j++) {  //j=offset if inputData includes labels in the first offset columns
                datacopy[i][((int) inputData[i][j] - 1)] = 1;   //original data contains part-number information instead of count
            }
        }
        this.matrixData = datacopy;
    }*/

    public List<Integer> getToxicList() {
        return this.toxicList;
    }

    public int error(List<Integer> target) {

        int error = 0;
        for (int x = 0; x < target.size(); x++) {
            if (!toxicList.contains(target.get(x))) {
                error++;
            }
        }
        return error;
    }
}
