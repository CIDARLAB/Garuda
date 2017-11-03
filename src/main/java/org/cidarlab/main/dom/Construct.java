/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.dom;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class Construct {
    
    @Setter
    @Getter
    private String _id;
    
    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private List<String> parts;

    @Setter
    @Getter
    private String plate_pos;
    
    public Construct(String name) {
        
        this.name = name;
    }

    public Construct(String name, List<String> parts) {
        
        this(name);
        this.parts = parts;
    }

    public Construct(String name, List<String> parts, String plate_pos) {
        
        this(name, parts);
        this.plate_pos = plate_pos;
    }
    
    public String printParts () {
        
        String print = "";
        for (int i = 0; i < parts.size(); i++) {
            print += parts.get(i) + ", ";
        }
        return (print.substring(0, print.length() - 2));
    }

}
