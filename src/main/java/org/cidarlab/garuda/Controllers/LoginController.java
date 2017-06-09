/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Controllers;

import org.cidarlab.garuda.Interfaces.MessageInterface;
import org.cidarlab.garuda.Services.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/login")
public class LoginController {
    
    @Autowired
    LoginService log;
    
    @RequestMapping(method=RequestMethod.GET)
    public String login (
            MessageInterface msgInt,
            Model model) {
        
        model.addAttribute("msgInt",msgInt);
        model.addAttribute("username", "");
        model.addAttribute("password", "");
        
        return "login";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String login_post (String username, String passwd) {
        
        if (!log.isValid(username, passwd)) {
            return "login";
        } else {
            return "index";
        }
    }
}
