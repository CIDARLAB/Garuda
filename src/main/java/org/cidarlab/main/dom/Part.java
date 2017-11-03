/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.dom;

import java.util.Comparator;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class Part implements Comparable<Part> {
    
    @Setter
    @Getter
    private String _id;
    
    @Setter
    @Getter
    private String name;
    
    @Setter
    @Getter
    private int assoc_idx;
    
    @Setter
    @Getter
    private double ave_growth;
    
    @Setter
    @Getter
    private double std_dev;
    
    @Setter
    @Getter
    private double min;
    
    @Setter
    @Getter
    private double max;
    
    @Setter
    @Getter
    private double probability;
    
    @Setter
    @Getter
    private double coefficient; //for regression
    
    public Part (String name, int assoc_idx) {
        
        this.name = name;
        this.assoc_idx = assoc_idx;
    }
    
    public Part (String name, double ave_growth, double std_dev, double probability) {
        
        this.name = name;
        this.ave_growth = ave_growth;
        this.std_dev = std_dev;
        this.probability = probability;
    }
    
    public Part (String name, double ave_growth, double std_dev, double probability, double coefficient) {
        
        this.name = name;
        this.ave_growth = ave_growth;
        this.std_dev = std_dev;
        this.probability = probability;
        this.coefficient = coefficient;
    }
    
    @Override
    public int compareTo(Part f) {
        return Comparators.labelComparator.compare(this, f);
        //return Double.compare(this.getLabel(), f.getLabel());
    }

    public static class Comparators {

        public static Comparator<Part> nameComparator = new Comparator<Part>() {
            @Override
            public int compare(Part f1, Part f2) {
                return f1.getName().compareTo(f2.getName());
            }
        };
        public static Comparator<Part> labelComparator = new Comparator<Part>() {
            @Override
            public int compare(Part f1, Part f2) {
                return Double.compare(f1.getAve_growth(), f2.getAve_growth());
            }
        };
    }

    
}
