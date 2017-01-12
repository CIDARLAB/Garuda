/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

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
    
    public Pairs (int s, int e) {
        this.start = s;
        this.end = e;
    }
}
