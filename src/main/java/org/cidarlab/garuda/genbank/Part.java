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
public class Part {

    @Getter
    private String partInstance;
    @Getter
    private PartType partType;
    @Getter
    private Orientation orientation;
    @Getter
    private PartProperty partProperties;

    public Part(String partInstance, PartType partType, Orientation orientation, PartProperty partProperties) {
        this.partInstance = partInstance;
        this.partType = partType;
        this.orientation = orientation;
        this.partProperties = partProperties;
    }

    @Override
    public String toString() {

        return "Part [partInstance=" + partInstance + ", partType=" + partType + ", orientation=" + orientation + ", partProperties=" + partProperties + "]";
    }
    
    public static enum PartType {
	PROMOTER, RBS, RIBOZYME, CDS, TERMINATOR
    }
    
    public static enum Orientation {
	FORWARD, REVERSE
    }

}
