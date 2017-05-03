/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.util.Objects;
import org.springframework.stereotype.Service;

/**
 *
 * @author mardian
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Override
    public boolean authenticate(String username, String password) {
        return Objects.equals(username, password);
    }
}
