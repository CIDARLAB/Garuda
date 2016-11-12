/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import org.cidarlab.main.dom.Data;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mardian
 */
public class BruteForce {
    
    private double[][] data;
    private double threshold;
    private int numOfData;
    
    private List<Data> inputData;
    private boolean zeroIsToxic;
    private double[][] partData;
    
    public BruteForce (List<Data> inputData, boolean zeroIsToxic, double[][] partData) {
        this.inputData = inputData;
        this.zeroIsToxic = zeroIsToxic;
        this.partData = partData;
        init ();
    }
    
/*    public BruteForce (double[][] inputData, double threshold) {
        this.data = inputData;
        this.threshold = threshold;
        this.numOfData = inputData.length;
        initArray ();
    }*/
    
    public void init () {
        
    /*    System.out.println (zeroIsToxic);
        int NUM_CLUSTERS = 2;
        //split list depends on their cluster
        for (int i=0; i<NUM_CLUSTERS; i++) {
            
            System.out.println("Cluster " + i + " includes:");
            for(int j=0; j<inputData.size(); j++) {
                
                if (inputData.get(j).cluster()==i) {
                    
                    System.out.println("** " + partData[j][0] + "     "  + partData[j][1] + "     "  + partData[j][2] + "     "  + partData[j][3] + "     " 
                            + (inputData.get(j).getId()+1) + "     " + (inputData.get(j).getPoint().getPoint(0)));
                }
            } // j
            System.out.println();
        } // i*/
            
        Set<Integer> ntList = new HashSet<Integer>();
        Set<Integer> tList = new HashSet<Integer>();
        int nontoxic = 0;
        if (zeroIsToxic)
            nontoxic = 1;
        
        for (int i=0; i<partData.length; i++) {
            if (inputData.get(i).cluster()==nontoxic) {
                //System.out.println("++++++++non toxic");
                for (int j=0; j<partData[0].length; j++) {
                    ntList.add ((int)partData[i][j]);
                    //System.out.print("nt:" + (int)partData[i][j] + "\t");
                }
            }
        }
        for (int i=0; i<partData.length; i++) {
            if (inputData.get(i).cluster()!=nontoxic) {
                //System.out.println("--------toxic");
                for (int j=0; j<partData[0].length; j++) {
                    if (!ntList.contains((int)partData[i][j])) {
                        tList.add ((int)partData[i][j]);
                        //System.out.print("t:" + (int)partData[i][j] + "\t");
                    }
                }
            }
            //System.out.println();
        }
        
        System.out.println("List of toxic genes: ");
        for (int i : tList) {
            System.out.println(i);
        }
    /*    System.out.println("List of non-toxic genes: ");
        for (int i : ntList) {
            System.out.println(i);
        } */
    }
    
/*    public void initArray () {
        
        List<Integer> nontoxic = new ArrayList<Integer>();
        List<Integer> toxic = new ArrayList<Integer>();
        
        for (int i=0; i<data.length; i++) {
            boolean flag = false;
            for (int j=0; j<data[0].length; j++) {
                if (data[i][j]<threshold) {
                    nontoxic.add (i+1);
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                toxic.add (i+1);
            }
        }
        
        System.out.println ("List of toxic genes: ");
        for (int i=0; i<toxic.size(); i++) {
            System.out.println ("**** " + toxic.get(i));
        }
        
        System.out.println("List of non-toxic genes: ");
        
        for (int i=0; i<nontoxic.size(); i++) {
            System.out.println ("**** " + nontoxic.get(i));
        }
        
    }*/
    
}
