/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.ClothoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/logout")
public class LogoutController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(method=RequestMethod.DELETE)
    public String logOut(
            HttpSession session,
            Model model) {
        
        clotho.logout_delete(session);
        
        LoginForm loginForm = new LoginForm();
        RegisterForm registerForm = new RegisterForm();
        model.addAttribute(loginForm);
        model.addAttribute(registerForm);
        
        session.invalidate();
        
        return "/login";
    }
}
