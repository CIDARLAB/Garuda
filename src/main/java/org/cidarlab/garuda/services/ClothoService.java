/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
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
    private static final HttpHeaders POST_HEADERS = new HttpHeaders();
    
    public ClothoService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
        
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
        }
        
        return myAccount;
    }    
}
