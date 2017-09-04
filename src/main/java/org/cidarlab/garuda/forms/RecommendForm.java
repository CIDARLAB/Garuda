/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 * @author mardian
 */
public class RecommendForm {

    @Getter
    @Setter
    private String[] partnames;

    @Getter
    @Setter
    private double[] probabilities;

    public RecommendForm() {
        this.partnames = null;
        this.probabilities = null;
    }

    public String result() {
        return "TESTING";
    }

}
