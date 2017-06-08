/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Controllers;

import org.cidarlab.garuda.MongoDb.AccountRepository;
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
@RequestMapping(value="/register")
public class RegisterController {
    
    @Autowired
    private AccountRepository repository;
    
    @RequestMapping(method=RequestMethod.POST)
    public String Register (String userName, String email, String passwd) {
        
        if (repository.findByUserName(userName).getUserName() == userName){
            System.out.println("Username taken");
            return "login";
        }
        
        
        if (repository.findByUserName(email).getUserName() == email){
            System.out.println("Email is in use");
            return "login";
        }
        
        
        return "login";
    }
}
