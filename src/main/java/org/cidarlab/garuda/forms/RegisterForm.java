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
    public class RegisterForm {
        
        @Getter
        @Setter
        private String name;
        
        @Getter
        @Setter
        private String email;
                
        @Getter
        @Setter
        private String username;

        @Getter
        @Setter
        private String password;
        
        @Getter
        @Setter
        private String application;
        
        public RegisterForm(
                String name,
                String email,
                String username,
                String password,
                String application) {
            this.name = name;
            this.email = email;
            this.username = username;
            this.password = password;
            this.application = application;
        }

        public RegisterForm() {
            this.application = "garuda";
        }
        
        
        @Override
        public String toString(){
        return "registerForm{"
                + "name= "     + name     + ", "
                + "email= "    + email    + ", "
                + "username= " + username + ", "
                + "password= " + password + ", "
                + "application= " + application
                + "}";
                
        }
    }
