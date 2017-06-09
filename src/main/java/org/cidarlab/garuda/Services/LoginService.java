/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Services;

import org.cidarlab.garuda.Database.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jayajr
 */
@Service
public class LoginService {
    
    @Autowired
    private AccountRepository repo;
            
    public boolean isValid(String username, String passwd){
        
        if (repo.findByUsername(username).getUsername() != null){
            if (repo.findByUsername(username).getPasswd() == passwd)
                return true;
        }
        
        return false;
    }
}
