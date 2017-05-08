/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mardian
 */
public class LoginForm {
    
    @Setter
    @Getter
    @Size(min=2, max=20, message="Username size should be in the range [2...30]")
    private String username;
    
    @Setter
    @Getter
    @NotNull
    private String password;
    
}
