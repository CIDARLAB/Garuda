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
@RequestMapping(value={"/", "/index", "/home"})
public class IndexController {
    
    @RequestMapping(method=RequestMethod.GET)
    public String getIndexPage(HttpSession session) {
        if (session.getAttribute("counter") != null){
            int count = (int)session.getAttribute("counter");
            session.setAttribute("counter", count += 1);
        }
        else {
            session.setAttribute("counter", 1);
        }
        
        return "index";
    }
}