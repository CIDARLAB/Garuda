/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.ClothoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/login")
public class LoginController {
    
    @Autowired
    private ClothoService clotho;
    
        
    @RequestMapping(method=RequestMethod.GET)
    public String getLoginPage(Model model, HttpSession session) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("registerForm", new RegisterForm());
        
        return "login";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String validateFormAndLogin(
            RegisterForm registerForm,
            @Valid LoginForm loginForm,
            BindingResult result,
            Model model,
            HttpSession session) {
        

//        if (result.hasErrors()){
//            for (Object object : result.getAllErrors()){
//                if (object instanceof FieldError) {
//                    FieldError fieldError = (FieldError) object;
//
//                    String message = messageSource.getMessage(fieldError, null);
//                    System.out.println(message);
//                }
//            }
//            System.out.println();
//            return "login";
//        }
            
        Account myAccount = null;
        
        try {
            
            myAccount = clotho.login_post(loginForm, session);

            if (myAccount == null) {
                throw new IllegalArgumentException();
            }

        } catch (IllegalArgumentException e) {
            return "loginerror";
        } finally {
            
            if (myAccount == null){
                return "loginerror";
            }
            
            else {
                return "index";
            }
        }
    }
    
    @ResponseStatus(
            value=HttpStatus.BAD_REQUEST,
            reason="Incorrect username or password")
    @ExceptionHandler(IllegalArgumentException.class)
    public String IllegalArgumentExceptionHandler(){
        return "loginerror";
    }
    

}
