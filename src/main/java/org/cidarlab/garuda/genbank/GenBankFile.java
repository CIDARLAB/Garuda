/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.genbank;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class GenBankFile {

    @Getter
    @Setter
    private String fullSequence;
    @Getter
    @Setter
    private String accession;
    @Getter
    @Setter
    private String oldAccession;

}
