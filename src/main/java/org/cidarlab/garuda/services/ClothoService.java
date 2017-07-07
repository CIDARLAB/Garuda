/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.util.Map;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.cidarlab.garuda.rest.clotho.model.Construct;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jayajr
 */

@Service
public class ClothoService {
    
    private final RestTemplate restTemplate;
    
    private static final String URL =  "http://localhost:9000/api";
    
    /*
        Constructor
    */
    
    public ClothoService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }
    
    
    /*
        Login Services
    */
    
    public Account login_post(LoginForm loginForm, HttpSession session) {
        
        String URI = URL + "/login";
        
        HttpHeaders postHeaders = new HttpHeaders();
                
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI, loginForm, Account.class, postHeaders);
        
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader());
            
            postHeaders.add("Authorization", myAccount.getAuthHeader());        
        }
        
        return myAccount;
    }
    
    public Account signup_post(RegisterForm registerForm, HttpSession session){
        
        String URI = URL + "/signup";
        
        HttpHeaders postHeaders = new HttpHeaders();
        
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI, registerForm, Account.class, postHeaders);
      
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader());
            
            postHeaders.add("Authorization", myAccount.getAuthHeader());       
        }
        
        return myAccount;
    }
        
    public void logout_delete(HttpSession session){
        
        String URI = URL + "/logout";
        
        HttpHeaders delHeaders = new HttpHeaders();
        delHeaders.add("Authorization", (String)session.getAttribute("authHeader"));
        HttpEntity<String> request = new HttpEntity<>(delHeaders);
        
        restTemplate.exchange(URI, HttpMethod.DELETE, request, String.class);
        
        return;        
    }
    
    
    /*
        Convenience Methods
    */
    
    
    public void getPart_get(String json){
        
        String URI = URL + "";
        
        HttpHeaders getHeaders = new HttpHeaders();
        //getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        restTemplate.getForEntity(URI, Construct.class, getHeaders);
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        
    }
    
    public String createDevice_post(String json){
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String testing(){
        return "testing!";
    }

    public String createPart_post(Map addForm, HttpSession session) {

        String URI = URL + "/part";
        
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        HttpEntity<Map<String, String>> request = new HttpEntity<>(addForm, postHeaders);
        

        ResponseEntity<String> responseEntity = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
        System.out.println("after restTemplate");
        String constructId = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            System.out.println("httpstatus is ok");
            constructId = responseEntity.getBody();
               
        }
        
        System.out.println("The status code is: " + responseEntity.getStatusCode().value() + " " + responseEntity.getStatusCodeValue());
        
        return constructId;
    }

    public void getDeviceId_get(String query_jsonString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
