/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
public class AddController {
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String getAddPage(HttpSession session) {        
        return "add";
    }
    
    @RequestMapping(value="/add_constructs", method=RequestMethod.GET)
    public String getAddConstructsPage(HttpSession session) {        
        return "add_constructs";
    }
    
    @RequestMapping(value="/add_gendata", method=RequestMethod.GET)
    public String getAddGendataPage(HttpSession session) {        
        return "add_gendata";
    }
    
    @RequestMapping(value="/add_parts", method=RequestMethod.GET)
    public String getAddPartsPage(HttpSession session) {        
        return "add_parts";
    }
    
    @RequestMapping(value="/add_qc", method=RequestMethod.GET)
    public String getAddQcPage(HttpSession session) {        
        return "add_qc";
    }
    
    @RequestMapping(value="/add_rna", method=RequestMethod.GET)
    public String getaddRnaPage(HttpSession session) {        
        return "add_rna";
    }
}

