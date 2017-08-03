/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jayajr
 */
public class SearchForm {
    
    @Getter
    @Setter
    String biodesignId;
    
    @Getter
    @Setter
    String filter;
    
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
    String sequence;
    
    @Getter
    @Setter
    String userSpace;
    
    @Getter
    @Setter
    String parameters;
    
    @Getter
    @Setter
    Boolean blast;
    
    public SearchForm() {
        this.biodesignId = null;
        this.filter = null;
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.sequence = null;
        this.userSpace = null;
        this.blast = false;
    }
    
    public String toFilteredQueryString(){
        
        String query = "{";
        
        if (this.name != null && !this.name.isEmpty()){
            query += "\"name\":\"" + this.name + "\",";
        };
        
        if (this.displayId != null && !this.displayId.isEmpty()){
            query += "\"displayId\":\"" + this.displayId + "\",";
        };
        
        if (this.role != null && !this.role.isEmpty()){
            query += "\"role\":\"" + this.role + "\",";
        };
        
        if (this.sequence != null && !this.sequence.isEmpty()){
            if (blast.equals(false)){
                query += "\"sequence\":\"" + this.sequence + "\",";
            } else {
                query += "\"BLASTsequence\":\"" + this.sequence + "\",";
            }
        };
        
        if (this.userSpace != null && !this.userSpace.isEmpty()){
            query += "\"userSpace\":\"" + this.userSpace + "\",";
        };
        
        query += "}";
        
        System.out.println(query);
        
        return query;
    }
    
    public Map toMap() throws ParseException{
        
        JSONParser parser = new JSONParser();
        JSONObject map = (JSONObject) parser.parse(this.toFilteredQueryString());
        
        return map;
    } 
}
