/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.database;

import org.cidarlab.garuda.database.GDbAccount;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.cidarlab.garuda.database.GDbAccountRepository;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 *
 * @author jayajr
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoUnitTest {
    
    @Autowired
    private GDbAccountRepository repo;
    
    @Before
    public void setUp() throws Exception {
        
        // Create test accounts
        GDbAccount user1= new GDbAccount("username1", "email1@email.com", "thisismypassword1");
        GDbAccount user2= new GDbAccount("username2", "email2@email.com", "thisismypassword2");
        
        
        this.repo.save(user1);
        this.repo.save(user2);
    }
     
    @Test
    public void testMongoGet(){
        /*Test data retrieval*/
        GDbAccount userA = repo.findByUsername("username1");
        assertNotNull(userA);
        assertEquals("email1@email.com", userA.getEmail());
        
        /*Get all products, list should only have two*/
        Iterable<GDbAccount> users = repo.findAll();
        
        int count = 0;
        for(GDbAccount a : users){
            count++;
            System.out.println(a.toString());
        }
        
        assertEquals(count, 2);
        System.out.println("thisismypassword1");
        System.out.println(userA.getPasswd());
    }
    
    @After
    public void tearDown() throws Exception {
        this.repo.deleteAll();
    }
}
