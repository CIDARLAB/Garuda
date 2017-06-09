/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jayajr
 */
@Repository
public interface AccountRepository extends MongoRepository<Account, String>{
    
    public Account findByUsername(String username);
    public Account findByEmail(String email);
        
}
