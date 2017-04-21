/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mardian
 */
public class GaussianMembership {
    
    private static final int num_of_classes = 5;
    private static double healthy_class_width;
    private static double toxic_class_width;
    
    public static List<Integer> test(double[][] value) {
        
        boolean first_healthy = true;
        boolean first_toxic = true;
        
        double[] healthy_separator = new double[num_of_classes+1];
        double[] toxic_separator = new double[num_of_classes+1];
        
        //finding maximum and minimum values for each healthy and toxic population
        for (int i=0; i<value.length; i++) {
            if (value[i][1]!=0.0) {
                if (first_healthy) {
                    healthy_separator[num_of_classes] = value[i][0];
                    healthy_separator[0] = value[i][0];
                    first_healthy = false;
                }
                else {
                    if (value[i][0]>healthy_separator[num_of_classes])
                        healthy_separator[num_of_classes] = value[i][0];
                    if (value[i][0]<healthy_separator[0])
                        healthy_separator[0] = value[i][0];
                }
            }
            else if (value[i][1]==0.0) {
                if (first_toxic) {
                    toxic_separator[num_of_classes] = value[i][0];
                    toxic_separator[0] = value[i][0];
                    first_toxic = false;
                }
                else {
                    if (value[i][0]>toxic_separator[num_of_classes])
                        toxic_separator[num_of_classes] = value[i][0];
                    if (value[i][0]<toxic_separator[0])
                        toxic_separator[0] = value[i][0];
                }
            }
        }
        
        System.out.println ("*** Min healthy " + healthy_separator[0] + " max healthy " + healthy_separator[num_of_classes]);
        System.out.println ("*** Min toxic " + toxic_separator[0] + " max toxic " + toxic_separator[num_of_classes]);
    
        //assign classes' separator from width
        healthy_class_width = (healthy_separator[num_of_classes] - healthy_separator[0]) / num_of_classes;
        toxic_class_width = (toxic_separator[num_of_classes] - toxic_separator[0]) / num_of_classes;
    
        for (int i=1; i<num_of_classes; i++) {
            healthy_separator[i] = healthy_separator[0] + (i * healthy_class_width);
            toxic_separator[i] = toxic_separator[0] + (i * toxic_class_width);
        }
        
        //System.out.println ("*** Number of classes is " + num_of_classes + " healthy width " + healthy_class_width + " toxic width " + toxic_class_width);
    
        //count cummulative frequency from each class
        int[] healthy_population = new int[num_of_classes];
        int[] toxic_population = new int[num_of_classes];
        //healthy_count + toxic_count should equal to total constructs
        int healthy_count = 0;
        int toxic_count = 0;
        
        for (int i=0; i<value.length; i++) {
            if (value[i][1]!=0.0) {
                for (int j=0; j<healthy_separator.length-1; j++) {
                    if (j<healthy_separator.length-2) {
                        if (value[i][0]>=healthy_separator[j] && value[i][0]<healthy_separator[j+1]) {
                            healthy_population[j]++;
                            healthy_count++;
                            break;
                        }
                    }
                    else {
                        if (value[i][0]>=healthy_separator[j] && value[i][0]<=healthy_separator[j+1]) {
                            healthy_population[j]++;
                            healthy_count++;
                            break;
                        }
                    }
                }
            }
            else if (value[i][1]==0.0) {
                for (int j=0; j<toxic_separator.length-1; j++) {
                    if (j<toxic_separator.length-2) {
                        if (value[i][0]>=toxic_separator[j] && value[i][0]<toxic_separator[j+1]) {
                            toxic_population[j]++;
                            toxic_count++;
                            break;
                        }
                    }
                    else {
                        if (value[i][0]>=toxic_separator[j] && value[i][0]<=toxic_separator[j+1]) {
                            toxic_population[j]++;
                            toxic_count++;
                            break;
                        }
                    }
                }
            }
        }
        
        for(int i=0; i<healthy_population.length; i++) {
            //System.out.println(i + "\t" + healthy_population[i]);
        }
            
        for(int i=0; i<toxic_population.length; i++) {
            //System.out.println(i + "\t" + toxic_population[i]);
        }
        
        System.out.println("+++Total healthy population is " + healthy_count + " and toxic population is " + toxic_count);
        
        //collect constructs' indexes for healthy population only
        List<Integer> healthy_for_sure = new ArrayList<Integer>();
        
        for(int i=0; i<value.length; i++) {
            if (value[i][0]>toxic_separator[num_of_classes]) {
                healthy_for_sure.add(i);
            }
        }
        System.out.println("Healthy for sure = " + healthy_for_sure.size());
        
        return healthy_for_sure;
          
    }
    
}
