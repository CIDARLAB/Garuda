/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.database;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author jayajr
 */
@Repository
public interface GDbAccountRepository extends MongoRepository<GDbAccount, String>{
    
    public GDbAccount findByUsername(String username);
    public GDbAccount findByEmail(String email);
        
}
