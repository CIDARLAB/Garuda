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
    
    //search results
    
    @Getter
    @Setter
    String[] results_name;
    
    @Getter
    @Setter
    String[] results_user;
    
    @Getter
    @Setter
    String[] results_url;
    
    @Getter
    @Setter
    String[] results_sub;
    
    public SearchForm() {
        this.biodesignId = null;
        this.filter = null;
        this.name = null;
        this.displayId = null;
        this.role = null;
        this.sequence = null;
        this.userSpace = null;
        this.blast = false;
        
        this.results_name = null;
        this.results_user = null;
        this.results_url = null;
        this.results_sub = null;
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
    
    public void init(int row) {
        
        this.results_name = new String[row];
        this.results_user = new String[row];
        this.results_url = new String[row];
        this.results_sub = new String[row];
    }
    
    public void set(String which, String value, int idx) {
        switch (which) {
            case "name":
                this.results_name[idx] = value;
                break;
            case "user":
                this.results_user[idx] = value;
                break;
            case "url":
                this.results_url[idx] = value;
                break;
            case "sub":
                this.results_sub[idx] = value;
                break;
            default:
                break;
        }
    }
    
    public String getSubName(int idx) {
        
        return this.results_name[idx] + "\t\t" + this.results_sub[idx];
    }
    
    public String testUrl() {
        
        if (results_url!=null)
            return results_url[2];
        return "Empty!!";
    } 
}
