/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.genbank;

import lombok.Getter;

/**
 *
 * @author mardian
 */
public class PartProperty {

    @Getter
    private String name;
    @Getter
    private String sequence;

    public PartProperty(String name, String sequence) {
        this.name = name;
        this.sequence = sequence;
    }

    @Override
    public String toString() {

        return "PartProperty [name=" + name + ", sequence=" + sequence + "]";
    }

}

