/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mardian
 */
@NoArgsConstructor
public abstract class SharableObjBase extends ObjBase implements Sharable {
    
    public SharableObjBase(String name, Person author){
        setName(name);
        this.author = author;
    }

    public SharableObjBase(String name, Person author, String description){
        this(name, author);
        this.description = description;
    }
    
    @Getter @Setter
    private Person author;

    @Getter @Setter
    private String description, icon;
    
}

