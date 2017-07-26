/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author jayajr
 */
public class LoginForm {
    @Getter
    @Setter
    private String username;

    @Getter
    @Setter
    private String password;

    @Getter
    @Setter
    private String application;
    
    public LoginForm(
            String username,
            String password) {
        this.username=username;
        this.password=password;
        this.application="garuda";
    }

    public LoginForm() {
        this.application="garuda";
    };

    @Override
    public String toString(){
        return "loginform{"
                + "username= " + username + ", "
                + "password= " + password + ", "
                + "application= " + application
                + "}";
                
    }
}
