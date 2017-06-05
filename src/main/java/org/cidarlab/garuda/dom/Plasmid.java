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
public class Plasmid {
    
    @Setter
    @Getter
    private String id;
    
    @Setter
    @Getter
    private String idx;
    
    @Setter
    @Getter
    private int length;
    
    @Setter
    @Getter
    private String sequence;
    
    @Setter
    @Getter
    private int[] annotations;
    
    public Plasmid () {
    }
    
    public Plasmid (String id, String idx, int length, String sequence, int[] annotations) {
        this.id = id;
        this.idx = idx;
        this.length = length;
        this.sequence = sequence;
        this.annotations = annotations;
    }
    
    public void randomAssign () {
        this.id = "pl" + System.currentTimeMillis();
        this.length = 2000;
        this.sequence = "ATGTACAACGAGCAGGTGAACTCTGGAAAGTCTATAAAAGAAAAGGAACGTTATTTGGATGCCCTATTGAAAATACTTAAG";
        annotations = new int[] {0, 100, 200, 500, 600, 2000};
    }
    
    public String getAnnostring () {
        String res = "";
        for (int i=0; i<annotations.length; i++) {
            if (i!=annotations.length-1) {
                res += annotations[i] + " ";
            }
            else if (i==annotations.length-1) {
                res += annotations[i];
            }
        }
        return res;
    }
    
}
