/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class Part {
    
    @Getter
    @Setter
    private String _id;
    
    @Getter
    @Setter
    private String name;
    
    @Getter
    @Setter
    private String role;
    
    public Part() {
        this("", "");
    }
    
    public Part(String name, String role) {
        this("", name, role);
    }
    
    public Part(String _id, String name, String role) {
        this._id = _id;
        this.name = name;
        this.role = role;
    }
}
