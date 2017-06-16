/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 * @author jayajr
 */
    public class RegisterForm {
        
        
        @NotEmpty(message="Register: Name may not be empty")
        @Email
        @Getter
        @Setter
        private String name;
        
        @NotEmpty(message="Register: Email may not be empty")
        @Email
        @Getter
        @Setter
        private String email;
                
        @NotEmpty(message="Register: Username may not be empty")
        @Size(min=5, max=25)
        @Getter
        @Setter
        private String username;


        @NotEmpty(message="Register: Password may not be empty")
        @Size(min=5, max = 15)
        @Getter
        @Setter
        private String password;
        
        @NotEmpty(message="Register: Application may not be empty")
        @Size(min=5, max = 15)
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
