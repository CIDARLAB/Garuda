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
public class QC extends SharableObjBase {
    
    @Setter
    @Getter
    private Sequence sequence;
    
    @Setter
    @Getter
    private BioDesign bioDesign;
    
    @Setter
    @Getter
    private Feature feature;
    
    public QC(String name, String description, Person author) {
        super(name, author, description);
    }
    
    public QC(String name, String description, Sequence sequence, BioDesign bioDesign, Feature feature, Person author) {
        super(name, author, description);
        this.sequence = sequence;
        this.bioDesign = bioDesign;
        this.feature = feature;
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.getName());
        map.put("author", this.getAuthor().getName());
        map.put("description", this.getDescription());
        if (this.sequence!=null)
            map.put("sequence", this.sequence.getSequence());
        if (this.bioDesign!=null)
            map.put("bioDesign", this.bioDesign.getName());
        if (this.feature!=null)
            map.put("feature", this.feature.getName());
        return map;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        obj.put("author", this.getAuthor().getName());
        obj.put("description", this.getDescription());
        if (this.sequence!=null)
            obj.put("sequence", this.sequence.getSequence());
        if (this.bioDesign!=null)
            obj.put("bioDesign", this.bioDesign.getName());
        if (this.feature!=null)
            obj.put("feature", this.feature.getName());
        return obj;
    }
    
    public String toString(){
        String str = "";
        str += "Name : " + this.getName() + "\n";
        str += "Author : " + this.getAuthor().getName() + "\n";
        str += "Description : " + this.getDescription();
        if (this.sequence!=null)
            str += "\nSequence : " + this.sequence.getSequence();
        if (this.bioDesign!=null)
            str += "\nBio-design : " + this.bioDesign.getName();
        if (this.sequence!=null)
            str += "\nFeature : " + this.feature.getName();
        return str;
    }
}
