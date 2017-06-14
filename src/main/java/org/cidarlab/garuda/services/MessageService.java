/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import org.springframework.stereotype.Service;

/**
 *
 * @author jayajr
 */
@Service
public class MessageService {
    public String getLoginSuccess(){
        return "Login Successful";
    }
    
    public String getLoginFailure(){
        return "Login Failed!";
    }
}
