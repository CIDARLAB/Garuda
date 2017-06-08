/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.MongoDb;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 *
 * @author jayajr
 */
public interface AccountRepository extends MongoRepository<Account, String>{
    
    public Account findByUserName(String userName);
    public Account findByEmail(String email);
        
}
