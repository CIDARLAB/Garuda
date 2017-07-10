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
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/about")
public class AboutController {
    
    @ResponseBody
    @RequestMapping(method=RequestMethod.GET)
    public String getAboutPage(HttpSession session) {
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            return "login";
        }
        
        return "about";
    }
}
