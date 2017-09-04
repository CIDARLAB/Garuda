/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class DNAPlotLib {
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private List<String> partnames;
    
    @Getter
    @Setter
    private List<String> roles;
    
    public DNAPlotLib() {
        this("d" + System.currentTimeMillis());
    }
    
    public DNAPlotLib(String name) {
        this.name = name;
        this.partnames = new ArrayList<String>();
        this.roles = new ArrayList<String>();
    }
    
    public DNAPlotLib(String name, List<String> partnames, List<String> roles) {
        this.partnames = partnames;
        this.roles = roles;
    }
    
    public void addPart(String partname) {
        if (this.partnames==null) {
            this.partnames = new ArrayList<String>();
        }
        this.partnames.add(partname);
    }
    
    public void addRole(String role) {
        if (this.roles==null) {
            this.roles = new ArrayList<String>();
        }
        this.roles.add(role);
    }
    
    public void addPartRole(String partname, String role) {
        addPart(partname);
        addRole(role);
    }
    
}
