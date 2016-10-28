/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.model;

/**
 *
 * @author mardian
 */
public class ObjectId {
    
    public ObjectId(String value){
        this.value = value;
    }
    
    public ObjectId(Object value){
        if (value == null) throw new IllegalArgumentException();
        this.value = value.toString();
    }
    
    /*public ObjectId(){
        this.value = new org.bson.types.ObjectId().toString();
    }*/
    
    public String getValue(){
        return value;
    }

    public String toString() {
        return value;
    }
    
    private final String value;
}

