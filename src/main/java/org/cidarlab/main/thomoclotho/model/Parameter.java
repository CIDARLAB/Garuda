/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.model;

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
public class Parameter {

    @NotNull
    @Getter
    @Setter
    protected double value;

    @NotNull
    @Getter
    @Setter
    //@Reference
    protected Variable variable;

    /*@NotNull
    @Getter
    @Setter
    //@Reference
    protected Units units;

    @Getter
    @Setter
    @Reference
    protected Derivation derivation;*/

    public Parameter(double value, Variable variable) {
        this.value = value;
        this.variable = variable;
    }

    public Map getMap(){
        Map map = new HashMap();
        map.put("value", this.value);
        if(variable!=null)
            map.put("variable", this.variable.getName());
        /*if(units!=null)
            map.put("units", this.units.getName());
        if(derivation!=null)
            map.put("derivation", this.derivation.getName());*/
        return map;
    }
    
    public JSONObject getJSON(){
        JSONObject obj = new JSONObject();
        obj.put("value", this.value);
        if(variable!=null)
            obj.put("variable", this.variable.getName());
        /*if(units!=null)
            obj.put("units", this.units.getName());
        if(derivation!=null)
            obj.put("derivation", this.derivation.getName());*/
        return obj;
    }
    
    public String toString(){
        String str = "";
        str += "Value : " + this.value;
        if(variable!=null)
            str += "\nVariable : " + this.variable.getName();
        /*if(units!=null)
            str += "\nUnits : " + this.units.getName();
        if(derivation!=null)
            str +=  "\nDerivation : " + this.derivation.getName();*/
        return str;
    }
}