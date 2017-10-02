/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import com.fasterxml.jackson.core.JsonParser;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.cidarlab.garuda.rest.clotho.model.Parameter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


/**
 *
 * @author jayajr
 */
public class UpdateForm {
    
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
    String parameters;
    
    @Getter
    @Setter
    String sequence;
    
    @Getter
    @Setter
    Boolean userSpace;
    
    @Getter
    @Setter
    String biodesignId;
    
    
    public UpdateForm(){
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.parameters = null;
        this.sequence = null;
        this.userSpace = null;
    }
    
    public void clear(){
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.parameters = null;
        this.sequence = null;
        this.userSpace = null;
    }
    
    @Override
    public String toString(){
        return "{"
                + "name: " + this.name +", "
                + "displayId: " + this.displayId + ", "
                + "role: " + this.role + ", "
                + "parameters:" + this.parameters + ", "
                + "sequence: " + this.sequence + ","
                + "userSpace: " + this.userSpace + "}";
    }
    
    public String toPartJsonString(){        
        return "{"
                + "\"name\": \"" + this.name +"\", "
                + "\"displayId\": \"" + this.displayId + "\", "
                + "\"role\": \"" + this.role + "\", "
                + "\"parameters\": " + this.parameters + ", "
                + "\"sequence\": \"" + this.sequence + "\""
                + "\"userSpace\": \"" + this.userSpace + "\""
                + "}";
    }
    
    public String toDeviceJsonString(){
        
        String string = "{";
        
        
        if (this.role != null || !this.role.isEmpty()){
            string += "\"role\": \"" + this.role + "\",";
        }
        
        if (this.parameters != null || !this.role.isEmpty()){
            string += "\"parameters\": " + this.parameters + ", ";
        } else if (this.parameters.equals("[]")) {
            
        }
        
                return string
                + "\"name\": \"" + this.name +"\", "
                + "\"displayId\": \"" + this.displayId + "\", "
                + "\"userSpace\": " + this.userSpace + ","
                + "}";
    }
    
    public Map toPartMap() throws ParseException{
        
        JSONParser parser = new JSONParser();
        JSONObject map = (JSONObject) parser.parse(this.toPartJsonString());
        
        return map;
    }
        
    public Map toDeviceMap() throws ParseException{
        
        JSONParser parser = new JSONParser();
        JSONObject map = (JSONObject) parser.parse(this.toDeviceJsonString());
        
        return map;
    } 
}
