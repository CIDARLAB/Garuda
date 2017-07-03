/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 */
public class AddForm {
    
    @Getter
    @Setter
    String name;
    
    @Getter
    @Setter
    String displayId;
    
    @Getter
    @Setter
    String role;
    
    @Getter
    @Setter
    ArrayList<Parameter> parameters;
    
    @Getter
    @Setter
    String sequence;
    
    
    protected class Parameter{
        
        @Getter
        @Setter
        String name;
        
        @Getter
        @Setter
        double value;
        
        @Getter
        @Setter
        String variable;
        
        @Getter
        @Setter
        String units;
    }
}
