/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author jayajr
 */

@Service
public class ClothoService {
    
    private final RestTemplate restTemplate;
    
    private static final String URL =  "http://localhost:9000/api";
    private static final HttpHeaders POST_HEADERS = new HttpHeaders();
    private static final HttpHeaders DEL_HEADERS = new HttpHeaders();
        
    public ClothoService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        
//        DEL_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        POST_HEADERS.setContentType(MediaType.APPLICATION_JSON);
        POST_HEADERS.setCacheControl("no-cache");
    }
    
    
    public Account login_post(LoginForm loginForm, HttpSession session) {
        
        String URI_LOGIN = URL + "/login";
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI_LOGIN, loginForm, Account.class, POST_HEADERS);
        
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("sessionId", myAccount.getSession().get_id());
            session.setAttribute("userId", myAccount.getSession().getUserId());
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader());
            
            POST_HEADERS.add("Authorization", myAccount.getAuthHeader());            
//            DEL_HEADERS.add("Authorization", myAccount.getAuthHeader());            
        }
        
        return myAccount;
    }
    
    public Account signup_post(RegisterForm registerForm, HttpSession session){
                
        //Check for availability first
        
        
        String URI_SIGNUP = URL + "/signup";
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI_SIGNUP, registerForm, Account.class, POST_HEADERS);
      
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("sessionId", myAccount.getSession().get_id());
            session.setAttribute("userId", myAccount.getSession().getUserId());
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader());
            
            POST_HEADERS.add("Authorization", myAccount.getAuthHeader());
//            DEL_HEADERS.add("Authorization", myAccount.getAuthHeader());            
        }
        
        return myAccount;
    }
        
    public void logout_delete(HttpSession session){
        String URI_LOGOUT = URL + "/logout";
        
        HttpHeaders DEL_HEADERS = new HttpHeaders();
        DEL_HEADERS.add("Authorization", (String)session.getAttribute("authHeader"));
        
        HttpEntity<String> request = new HttpEntity<>(DEL_HEADERS);
        restTemplate.exchange(URI_LOGOUT, HttpMethod.DELETE, request, String.class);
        
        return;        
    }
}
