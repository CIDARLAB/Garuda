/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */


public class PlasmidList {

    @Getter
    @Setter
    private List<Plasmid> plasmidlist;
    
    @Getter
    @Setter
    private String test = "YAHOOOO TEST DONG OY OY";
    
    public PlasmidList() {
        
        plasmidlist = new ArrayList<Plasmid>();
        plasmidlist.add (new Plasmid ("pl" + System.currentTimeMillis() + "1", "entry0", 1000, "TATAGAAGAGATGAACATACGCTCGTAA", new int[]{0, 50, 70, 200, 250, 1000}));
        plasmidlist.add (new Plasmid ("pl" + System.currentTimeMillis() + "2", "entry1", 2000, "TATAGAAGAGATGAACATACGCTCGTAA", new int[]{0, 70, 90, 300, 400, 2000}));
        plasmidlist.add (new Plasmid ("pl" + System.currentTimeMillis() + "3", "entry2", 3000, "TATAGAAGAGATGAACATACGCTCGTAA", new int[]{0, 150, 200, 400, 500, 3000}));
        plasmidlist.add (new Plasmid ("pl" + System.currentTimeMillis() + "4", "entry3", 500, "TATAGAAGAGATGAACATACGCTCGTAA", new int[]{0, 40, 50, 100, 150, 500}));
        plasmidlist.add (new Plasmid ("pl" + System.currentTimeMillis() + "5", "entry4", 700, "TATAGAAGAGATGAACATACGCTCGTAA", new int[]{0, 50, 70, 200, 250, 700}));
    }
    
    public String[] getAnnotations() {
        
        String[] anno = new String[plasmidlist.size()];
        for (int i=0; i<anno.length; i++) {
            anno[i] = plasmidlist.get(i).getAnnostring();
        }
        return anno;
    }

}
