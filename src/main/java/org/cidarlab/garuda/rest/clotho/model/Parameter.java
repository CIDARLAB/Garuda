/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.rest.clotho.model;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 */
public class Parameter{
        
    @Getter
    @Setter
    String name;

    @Getter
    @Setter
    double value;

    @Getter
    @Setter
    String variable;

    @Getter
    @Setter
    String units;
}
