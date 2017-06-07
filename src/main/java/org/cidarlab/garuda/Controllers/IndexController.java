/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value={"/", "/index"})
public class IndexController {
    
    @RequestMapping(method=RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }
}
