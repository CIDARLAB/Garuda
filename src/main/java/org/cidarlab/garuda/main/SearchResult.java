/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class SearchResult {
    
    @Setter
    @Getter
    private int testnum = 10;
    
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
    private String description;
    
    @Setter
    @Getter
    private String sequence;
    
    @Setter
    @Getter
    private String author;
    
    @Setter
    @Getter
    private List<Pairs> annotations;
    
    public SearchResult (String id, String idx, int length, String sequence, List<Integer> annotations, List<String> labels, List<String> colors, String author) {
        this.id = id;
        this.idx = idx;
        this.length = length;
        this.sequence = sequence;
        this.author = author;
        this.annotations = new ArrayList<Pairs>();
        for (int i=0; i<annotations.size()-1; i++) {
            this.annotations.add (new Pairs(annotations.get(i), annotations.get(i+1), labels.get(i), colors.get(i)));
        }
    }
    
    /*public int getAnnoLength() {
        return annotations.length;
    }
    
    public int getAnnoAt(int i) {
        return annotations[i];
    }
    
    public List<Integer> getAnnoList() {
        return new ArrayList<Integer>(Arrays.asList(annotations));
    }*/
    
    public String getAnnostring () {
        String res = "";
        for (int i=0; i<annotations.size(); i++) {
            res += annotations.get(i).getStart() + " ";
        }
        res += annotations.get(annotations.size()-1).getEnd();
        return res;
    }
}
