/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.cidarlab.garuda.services.ClothoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/register")
public class RegisterController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(method=RequestMethod.POST)
    public String validateFormAndRegister (
            LoginForm loginForm,
            @Valid RegisterForm registerForm,
            BindingResult result,
            Model model,
            HttpSession session){
        
        System.out.println(registerForm.toString());
        Account myAccount = null;
                
        // If Form has error
//        if (result.hasErrors()) {
//            model.addAttribute("message","Invalid Form");
//            return "/login";
//        }
        
//        else {

        try {
            myAccount = clotho.signup_post(registerForm,session);
        } catch (HttpClientErrorException e){
            System.out.println("message: This username or email is taken");
            return "/login" ;
        }
        
        if (myAccount == null){
            System.out.println("message: This username or email is taken");
            return "/login";
        }

        else{
            System.out.println("message: Register success");
            return "/index";
        }
    }
}
