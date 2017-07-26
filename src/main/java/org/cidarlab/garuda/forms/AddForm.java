/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.garuda.rest.clotho.model.Parameter;

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
    
//    @Getter
//    @Setter
//    ArrayList<Map> parameters;
    
    @Getter
    @Setter
    String sequence;
    
    public AddForm(){
        this.name = null;
        this.displayId = null;
        this.role = null;
//        this.parameters = null;
        this.sequence = null;
    }
    
    public void clear(){
        this.name = null;
        this.displayId = null;
        this.role = null;
//        this.parameters = null;
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
    
    public Map toMap(){
        
        Map map = new HashMap<>();
        
        map.put("name", name);
        map.put("displayId", displayId);
        map.put("role", role);
        //map.put("parameters", parameters);
        map.put("sequence", sequence);
        
        return map;
    }
    
}
