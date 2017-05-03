/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
@NoArgsConstructor
public class Institution extends SharableObjBase {

    /**
     * Constructor from raw data
     * @param name
     * @param city
     * @param state
     * @param country
     */
    
    @Setter
    @Getter
    @NotNull
    private String city, state, country;
    
    public Institution(String name, String description, Person author) {
        super(name, author, description);
    }
    
    //TODO:unique name constraint
    public Institution(String name, String city, String state, String country) {
        super(name, null);
        this.city = city;
        this.state = state;
        this.country = country;
    }

    public Map getMap(){
        Map map = new HashMap();
        //map.put("id", this.getId().getValue());
        map.put("name", this.getName());
        map.put("author", this.getAuthor().getName());
        map.put("description", this.getDescription());
        map.put("city", this.city);
        map.put("state", this.state);
        map.put("country", this.country);
        return map;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        //obj.put("id", this.getId().getValue());
        obj.put("name", this.getName());
        obj.put("author", this.getAuthor().getName());
        obj.put("description", this.getDescription());
        obj.put("city", this.city);
        obj.put("state", this.state);
        obj.put("country", this.country);
        return obj;
    }
    
    public String toString(){
        String str = "";
        //str += "ID : " + this.getId().getValue() + "\n";
        str += "Name : " + this.getName() + "\n";
        str += "Author : " + this.getAuthor().getName() + "\n";
        str += "Description : " + this.getDescription() + "\n";
        str += "City : " + this.city + "\n";
        str += "State : "+ this.state + "\n";
        str += "Country : " + this.country;
        return str;
    }
}
