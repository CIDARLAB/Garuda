/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author jayajr
 */
public class DeleteForm {
    
    @Getter
    @Setter
    String biodesignId;
    
    public DeleteForm() {
        this.biodesignId = null;
    }
    
}
