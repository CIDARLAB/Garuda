/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
*
* @author Nicholas Roehner
*/
@NoArgsConstructor
public class ExperimentalCondition {

    @NotNull
    @Size(min=1)
    @Getter
    protected Set<Parameter> parameters;

    public Parameter createParameter(double value, Variable variable) {
        Parameter parameter = new Parameter(value, variable);
        addParameter(parameter);
        return parameter;
    }

    public void addParameter(Parameter parameter) {
    	if (parameters == null) {
    		parameters = new HashSet<Parameter>();
    	}
    	parameters.add(parameter);
    }
}
