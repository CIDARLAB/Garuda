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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
@RequestMapping(value="/register")
public class RegisterController {
    
    @Autowired
    private AccountRepository repo;
    
    @RequestMapping(method=RequestMethod.POST)
    public String validateFormAndRegister (
            @Valid LoginForm loginForm,
            @Valid RegisterForm registerForm,
            BindingResult result,
            Model model,
            HttpSession session){
     
        // If Form has error
        if (result.hasErrors()) {
            return "login";
        }
        
        if (repo.exists(registerForm.getUsername())){
            return "login";
        }
        
        else{
            Account newAccount = new Account(
                registerForm.getUsername(),
                registerForm.getEmail(),
                registerForm.getPasswd());
            
            repo.insert(newAccount);
        }
        
        return "login";
    }
}
