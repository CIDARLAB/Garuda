/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.main;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class GenBankFeature { //extends GenBankFile {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private boolean reverseComplement;
    @Getter
    @Setter
    private boolean isSource;
    @Getter
    @Setter
    private int startx;
    @Getter
    @Setter
    private int endx;
    @Getter
    @Setter
    private String dnaSequence;
    @Getter
    @Setter
    private String featureType;
    @Getter
    @Setter
    private String sourceId;
    @Getter
    @Setter
    private String genBankId;
    @Getter
    @Setter
    private String partId;
    @Getter
    @Setter
    private String proteinSequence;
    @Getter
    @Setter
    private String fullSequence;
    @Getter
    @Setter
    private String accession;
    @Getter
    @Setter
    private String oldAccession;
    

    public GenBankFeature() {

    }

    public GenBankFeature(String name, boolean reverseComplement, boolean isSource, int startx, int endx, String dnaSequence, String featureType, String sourceId, String genBankId, String partId, String proteinSequence) {
        //super();
        this.name = name;
        this.reverseComplement = reverseComplement;
        this.isSource = isSource;
        this.startx = startx;
        this.endx = endx;
        this.dnaSequence = dnaSequence;
        this.featureType = featureType;
        this.sourceId = sourceId;
        this.genBankId = genBankId;
        this.partId = partId;
        this.proteinSequence = proteinSequence;
    }

}
