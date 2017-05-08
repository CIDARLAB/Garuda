/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.validation.Valid;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.services.NotificationService;
import org.cidarlab.garuda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author mardian
 */
@Controller
public class LoginController {
    
    //@Autowired
    private UserService userService;

    //@Autowired
    private NotificationService notifyService;

    @RequestMapping(value = "/users/login", method=RequestMethod.GET)
    public String login() {
        return "login_test";
    }

    @RequestMapping(value = "/users/login", method=RequestMethod.POST)
    public String loginPage(@Valid LoginForm loginForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "login_test";
        }

        if (!userService.authenticate(
            loginForm.getUsername(), loginForm.getPassword())) {
            notifyService.addErrorMessage("Invalid login!");
            return "login_test";
        }
        System.out.println("*************It got here!!!!");
        
        notifyService.addInfoMessage("Login successful");
        return "redirect:/";
    }
}
