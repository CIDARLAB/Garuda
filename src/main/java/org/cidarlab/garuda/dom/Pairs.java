/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.dom;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class Pairs {
    
    @Setter
    @Getter
    private int start;
    
    @Setter
    @Getter
    private int end;
    
    @Setter
    @Getter
    private String label;
    
    @Setter
    @Getter
    private String color;
    
    public Pairs (int s, int e, String label, String color) {
        this.start = s;
        this.end = e;
        this.label = label;
        this.color = color;
    }
}
