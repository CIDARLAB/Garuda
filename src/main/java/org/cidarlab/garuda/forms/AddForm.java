/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author jayajr
 */
public class AddForm {
    
    @NotEmpty(message="Import: Name may not be empty")
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
    
    public AddForm(){
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.parameters = null;
        this.sequence = null;
    }
    
    public void clear(){
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.parameters = null;
        this.sequence = null;
    }
    
    @Override
    public String toString(){
        return "{"
                + "name: " + this.name +", "
                + "displayId: " + this.displayId + ", "
                + "role: " + this.role + ", "
                + "parameters: ... , "
                + "sequence: " + this.sequence + "}";
    }
    
}
