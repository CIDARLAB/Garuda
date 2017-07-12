/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.ClothoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jayajr
 */
@Controller
public class AddController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String getAddPage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        model.addAttribute("addForm", new AddForm());
        return "add";
    }
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    @ResponseBody
    public String postAddPage(
            AddForm addForm,
            HttpSession session,
            Model model) {
        
        String result = null;
        
        try {
            
            System.out.println("1");
            String partId = clotho.createPart_post(addForm.toMap(), session);
            System.out.println("2");
            if (partId == null) {
                System.out.println("null");
                throw new RuntimeException();
            } else {
                System.out.println("3");
                result = clotho.getPart_get(session, partId);
                System.out.println("4");
            }

        } catch (RuntimeException e) {
            System.out.println("e");
            return "/error";
        } finally {
            System.out.println("out");
            return result;
//            return "/index";
        }
    }
    
    @RequestMapping(value="/add_metadata", method=RequestMethod.GET)
    public String getAddMetadataPage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_metadata";
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

