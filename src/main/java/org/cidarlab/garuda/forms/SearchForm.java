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
 */
public class SearchForm {
    
    @Getter
    @Setter
    String biodesignId;
    
    public SearchForm() {
        this.biodesignId = null;
    }
    
    @Override
    public String toString(){
        return "searchForm{"
                + "biodesignId: " + biodesignId
                + "}";
    }
}
