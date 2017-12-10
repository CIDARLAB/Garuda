/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import lombok.Getter;
import lombok.Setter;
import org.cidarlab.garuda.legacyutil.RWR_RecEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jayajr
 */
@Service
public class ParserService {
    @Autowired
    static ClothoService clotho;
    
    @Getter
    @Setter
    Guy_Parser guyParser;    
    
//    @Getter
//    @Setter
//    @Autowired
//    RM_Parser rmParser;
    
    @Getter
    @Setter
    RWR_Parser rwrParser;
    
    @Getter
    @Setter
    RWR_RecEngine rwrRecEngine;

    @Getter
    @Setter
    AquariumParser aqParser;

    
//    ParserService(){
//        guyParser = new Guy_Parser();
//        rmParser = new RM_Parser();
//        rwrParser = new RWR_Parser();
//        rwrRecEngine = new RWR_RecEngine();
//    }
        
    
}
