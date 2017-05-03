/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.clotho.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
*
* @author mardian
*/
@NoArgsConstructor
public class CompositeModule extends Module {

    @NotNull
    @Size(min=1)
    @Getter
    @Setter
    //@ReferenceCollection
    protected Set<Module> subModules;

    public CompositeModule(String name, ModuleRole role, Set<Module> subModules, Person author) {
        super(name, role, author);
        this.subModules = subModules;
    }

    public CompositeModule(String name, String description, ModuleRole role, Set<Module> subModules,
            Person author) {
        super(name, description, role, author);
        this.subModules = subModules;
    }
    
    public void addSubModule(Module subModule) {
    	if (subModules == null) {
    		subModules = new HashSet<Module>();
    	}
    	subModules.add(subModule);
    }

}
