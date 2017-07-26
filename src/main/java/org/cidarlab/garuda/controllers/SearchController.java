/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.forms.SearchForm;
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
@RequestMapping(value="/search")
public class SearchController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(method=RequestMethod.GET)
    public String getSearchPage(HttpSession session, Model model) {     
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        model.addAttribute("searchForm", new SearchForm());
        return "search";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    @ResponseBody
    public String resolveSearch(
            SearchForm searchForm,
            HttpSession session,
            Model model){
        
        if (searchForm.getBiodesignId() != null){
            
            return clotho.getPartById_get(session, searchForm.getBiodesignId());
            
        }
        
        
        return "result";
    }
}

