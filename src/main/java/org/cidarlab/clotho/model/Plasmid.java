/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class Plasmid extends SharableObjBase {
    
    
    @Setter
    @Getter
    private String name;
    
    @Setter
    @Getter
    private Sequence sequence;
    
    public Plasmid(String name, String description, Sequence sequence, Person author) {
        super(name, author, description);
        this.sequence = sequence;
    }
    
    public Plasmid(String name, String description, String sequence, Person author) {
        super(name, author, description);
        //plasmid name and sequence should be different?
        this.sequence = new Sequence("seq" + System.currentTimeMillis(), sequence, author);
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.getName());
        map.put("author", this.getAuthor().getName());
        map.put("description", this.getDescription());
        if (this.sequence!=null)
            map.put("sequence", this.sequence.getSequence());
        return map;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        obj.put("author", this.getAuthor().getName());
        obj.put("description", this.getDescription());
        if (this.sequence!=null)
            obj.put("sequence", this.sequence.getSequence());
        return obj;
    }
    
    public String toString(){
        String str = "";
        str += "Name : " + this.getName() + "\n";
        str += "Author : " + this.getAuthor().getName() + "\n";
        str += "Description : " + this.getDescription();
        if (this.sequence!=null)
            str += "\nSequence : " + this.sequence.getSequence();
        return str;
    }
}
