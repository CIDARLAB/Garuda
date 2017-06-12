/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.Database;

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

/**
 *
 * @author jayajr
 */


@SpringBootTest
@RunWith(SpringRunner.class)
public class MongoUnitTest {
    
    @Autowired
    private AccountRepository repo;
    
    @Before
    public void setUp() throws Exception {
        
        // Create test accounts
        Account user1= new Account("username1", "email1@email.com", "thisismypassword1");
        Account user2= new Account("username2", "email2@email.com", "thisismypassword2");
        
        
        this.repo.save(user1);
        this.repo.save(user2);
    }
     
    @Test
    public void testMongoGet(){
        /*Test data retrieval*/
        Account userA = repo.findByUsername("username1");
        assertNotNull(userA);
        assertEquals("email1@email.com", userA.getEmail());
        
        /*Get all products, list should only have two*/
        Iterable<Account> users = repo.findAll();
        
        int count = 0;
        for(Account a : users){
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
