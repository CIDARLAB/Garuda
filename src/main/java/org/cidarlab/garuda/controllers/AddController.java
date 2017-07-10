/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
public class AddController {
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String getAddPage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add";
    }
    
    @RequestMapping(value="/add_constructs", method=RequestMethod.GET)
    public String getAddConstructsPage(HttpSession session, Model model) {
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_constructs";
    }
    
    @RequestMapping(value="/add_gendata", method=RequestMethod.GET)
    public String getAddGendataPage(HttpSession session, Model model) {    
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_gendata";
    }
    
    @RequestMapping(value="/add_parts", method=RequestMethod.GET)
    public String getAddPartsPage(HttpSession session, Model model) {    
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_parts";
    }
    
    @RequestMapping(value="/add_qc", method=RequestMethod.GET)
    public String getAddQcPage(HttpSession session, Model model) {   
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_qc";
    }
    
    @RequestMapping(value="/add_rna", method=RequestMethod.GET)
    public String getaddRnaPage(HttpSession session, Model model) {     
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_rna";
    }
}

