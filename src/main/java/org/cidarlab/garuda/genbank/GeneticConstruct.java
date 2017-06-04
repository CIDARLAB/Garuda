/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.genbank;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 *
 * @author mardian
 */
public class GeneticConstruct {

    @Getter
    private final String name;
    @Getter
    private String sequence = "";
    @Getter
    private List<Part> partList;
    @Getter
    private List<Coordinate> coordinateList;

    public GeneticConstruct(String name, List<Part> partList) {
        List<Coordinate> coords = new ArrayList<Coordinate>();

        int counter = 0;
        this.name = name;
        for (Part part : partList) {
            this.sequence += part.getPartProperties().getSequence();
            Coordinate coord = new Coordinate(counter + 1, counter + part.getPartProperties().getSequence().length());
            coords.add(coord);
            counter += part.getPartProperties().getSequence().length();
        }
        this.coordinateList = coords;
        this.partList = partList;
    }

    @Override
    public String toString() {

        return "GeneticConstruct [name=" + name + ", sequence=" + sequence + ", partList=" + partList + "]";
    }

}
