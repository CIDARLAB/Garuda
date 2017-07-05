/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.clotho;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.services.ClothoService;
import org.junit.After;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotNull;

/**
 *
 * @author jayajr
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class LoginTest {
    
    HttpSession session;
    
    @Autowired
    ClothoService clotho;
    
    @Before
    public void setup(){
        System.out.println("Starting Login Test");
        System.out.println("===== ===== ===== ===== =====");
    }
    
    @Test
    public void login(){
        LoginForm form = new LoginForm("jerome", "password");
        
        assertNotNull(clotho);
        assertNotNull(form);
        
        Account testAccount = null;
        
        testAccount = clotho.login_post(form, session);
        
        assertNotNull(testAccount);
        
        System.out.println(testAccount.toString());
    }
    
    @After
    public void tearDown(){
        System.out.println("Ending Login Test");
        System.out.println("===== ===== ===== ===== =====");
    }
}
