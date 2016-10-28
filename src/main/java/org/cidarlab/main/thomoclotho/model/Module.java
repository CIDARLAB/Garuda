/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;

/**
*
* @author Nicholas Roehner
*/
@NoArgsConstructor
public abstract class Module extends SharableObjBase {

    @NotNull
    @Getter
    @Setter
    protected ModuleRole role;

    @Getter
    @Setter
    //@ReferenceCollection
    protected Set<Influence> influences;

    @Getter
    @Setter
    //@Reference
    protected Module parentModule;

    public Module(String name, ModuleRole role, Person author) {
        super(name, author);
        this.role = role;
    }

    public Module(String name, String description, ModuleRole role, Person author) {
        super(name, author, description);
        this.role = role;
    }

    // Feel free to add more of these
    public static enum ModuleRole {
        TRANSCRIPTION, TRANSLATION, EXPRESSION, COMPARTMENTALIZATION, LOCALIZATION, SENSOR, REPORTER, ACTIVATION, REPRESSION;
    }
    
    public void addInfluence(Influence influence) {
    	if (influences == null) {
    		influences = new HashSet<Influence>();
    	}
    	influences.add(influence);
    }

}