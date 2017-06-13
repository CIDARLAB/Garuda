/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.cidarlab.garuda.Database.Account;
import org.cidarlab.garuda.Database.AccountRepository;
import org.cidarlab.garuda.Forms.LoginForm;
import org.cidarlab.garuda.Forms.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/login")
public class LoginController {
    
    @Autowired
    private AccountRepository repo;
    
    @Autowired
    private MessageSource messageSource;
        
    @RequestMapping(method=RequestMethod.GET)
    public String getLoginPage(Model model, HttpSession session) {
        model.addAttribute("loginForm", new LoginForm());
        model.addAttribute("registerForm", new RegisterForm());
        
        session.setAttribute("username", "testuser");
        return "login";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String validateFormAndLogin(
            @Valid LoginForm loginForm,
            @Valid RegisterForm registerForm,
            BindingResult result,
            Model model ) {
        
        if (result.hasErrors()){
            for (Object object : result.getAllErrors()){
                if (object instanceof FieldError) {
                    FieldError fieldError = (FieldError) object;
                    
                    String message = messageSource.getMessage(fieldError, null);
                    System.out.println(message);
                }
            }
            System.out.println();
            return "login";
        }
        
        if (!repo.exists(loginForm.getUsername())){
            System.out.println("User does not exist");
            return "login";
        }
        
        else {
            Account dbUser = repo.findOne(loginForm.getUsername());
            if (!(loginForm.getPasswd().equals(dbUser.getPasswd()))){
                System.out.println("Wrong Password");
                return "login";
            }
        }
                
        System.out.println("Success!");
        return "index";
    }
}
