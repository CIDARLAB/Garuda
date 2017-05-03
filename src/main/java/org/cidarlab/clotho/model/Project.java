/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;

/**
 *
 * @author mardian
 */
public class Project extends SharableObjBase {
    
    @Setter
    @Getter
    private String libraryId, purpose, doe, measurement;
    
    @Setter
    @Getter
    private int size;
    
    @Setter
    @Getter
    private Host host;
    
    @Setter
    @Getter
    private Strain strain;
    
    @Setter
    @Getter
    private Vector vector;
    
    @Setter
    @Getter
    private Plasmid plasmid;
    
    @Setter
    @Getter
    private Person PI;
    
    @Setter
    @Getter
    private Set<Person> people;
    
    @Setter
    @Getter
    private Set<Institution> institutions;
    
    @Setter
    @Getter
    private Grant grant;
    
    @Setter
    @Getter
    private Set<Publication> publications;
    
    @Setter
    @Getter
    private Set<ExperimentalDesign> experimentalDesigns;
    
    //unclear objects as enum -- subject to revision
    public static enum Host {
        E_COLI, YEAST
    }
    
    public static enum Strain {
        BL21_DE3
    }
    
    public static enum Vector {
        PLAC861
    }
    
    public Project(String name, String description, Person author) {
        super(name, author, description);
    }
    
    public Project(String name, String description, Person author,
            String libraryId, String purpose, int size, String doe, String measurement,
            String host, String strain, String vector, String sequence) {
        super(name, author, description);
        this.libraryId = libraryId;
        this.purpose = purpose;
        this.size = size;
        this.doe = doe;
        this.measurement = measurement;
        switch (host) {
            case "E Coli":
            case "E coli":
            case "e coli":
                this.host = Host.E_COLI;
                break;
            case "Yeast":
            case "yeast":
                this.host = Host.YEAST;
                break;
            default:
                break;
        }
        switch (strain) {
            case "BL21(DE3)":
                this.strain = Strain.BL21_DE3;
                break;
            default:
                break;
        }
        switch (vector) {
            case "pLac861":
                this.vector = Vector.PLAC861;
                break;
            default:
                break;
        }
        if (sequence!="NA")
            this.plasmid = new Plasmid("pla" + System.currentTimeMillis(), "", sequence, author);
    }
    
    public void addPublication(Publication publication) {
    	if(publications==null) {
            this.publications = new HashSet<Publication>();
    	}
    	publications.add(publication);
    }
    
    public void addExperimentalDesign(ExperimentalDesign experimentalDesign) {
    	if(experimentalDesigns==null) {
            this.experimentalDesigns = new HashSet<ExperimentalDesign>();
    	}
    	experimentalDesigns.add(experimentalDesign);
    }
    
    public Map getMap(){
        Map map = new HashMap();
        map.put("name", this.getName());
        map.put("author", this.getAuthor().getName());
        map.put("description", this.getDescription());
        map.put("libraryId", this.libraryId);
        map.put("purpose", this.purpose);
        map.put("size", this.size);
        map.put("doe", this.doe);
        map.put("measurement", this.measurement);
        if (this.host!=null)
            map.put("host", this.host.toString());
        if (this.strain!=null)
            map.put("host", this.strain.toString());
        if (this.vector!=null)
            map.put("host", this.vector.toString());
        if (this.plasmid!=null)
            map.put("plasmid", this.plasmid.getName());
        return map;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("name", this.getName());
        obj.put("author", this.getAuthor().getName());
        obj.put("description", this.getDescription());
        obj.put("libraryId", this.libraryId);
        obj.put("purpose", this.purpose);
        obj.put("size", this.size);
        obj.put("doe", this.doe);
        obj.put("measurement", this.measurement);
        if (this.host!=null)
            obj.put("host", this.host.toString());
        if (this.strain!=null)
            obj.put("host", this.strain.toString());
        if (this.vector!=null)
            obj.put("host", this.vector.toString());
        if (this.plasmid!=null)
            obj.put("plasmid", this.plasmid.getName());
        return obj;
    }
    
    public String toString(){
        String str = "";
        str += "Name : " + this.getName() + "\n";
        str += "Author : " + this.getAuthor().getName() + "\n";
        str += "Description : " + this.getDescription() + "\n";
        str += "Library ID : " + this.libraryId + "\n";
        str += "Purpose : " + this.purpose + "\n";
        str += "Size : " + this.size + "\n";
        str += "DOE Method: " + this.doe + "\n";
        str += "Measurement : " + this.measurement;
        if (this.host!=null)
            str += "\nHost : " + this.host.toString();
        if (this.strain!=null)
            str += "\nHost : " + this.strain.toString();
        if (this.vector!=null)
            str += "\nHost : " + this.vector.toString();
        if (this.plasmid!=null)
            str += "\nPlasmid : " + this.plasmid.getName();
        return str;
    }
}
