/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.rest.clotho.model.Account;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
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
    
    private static final String URL = "http://localhost:9000/api";
//    private static final String URL = "http://alpha.clothocad.org/api"
    
    /*
        Constructor
    */
    
    public ClothoService(RestTemplateBuilder restTemplateBuilder){
        this.restTemplate = restTemplateBuilder.build();
    }
    
    
    /* ===== ===== =====
        Login Services
     * ===== ===== ===== */ 
    
    public Account login_post(LoginForm loginForm, HttpSession session) {
        
        String URI = URL + "/login";
        
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI, loginForm, Account.class);
        
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader());     
        }
        
        return myAccount;
    }
    
    public Account signup_post(RegisterForm registerForm, HttpSession session){
        
        String URI = URL + "/signup";
        
        ResponseEntity<Account> responseEntity = restTemplate.postForEntity(URI, registerForm, Account.class);
      
        Account myAccount = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            
            myAccount = responseEntity.getBody();
            
            session.setAttribute("username", myAccount.getUser().getUsername());
            session.setAttribute("authHeader", myAccount.getAuthHeader()); 
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
    
    
    /* ===== ===== =====
        Utilities
     * ===== ===== ===== */ 
    
    /* ===== =====
        BLAST
     * ===== ===== */
    
    public String getBlast_post(Map json, HttpSession session){
        
        String URI = URL + "/blast";
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        HttpEntity<?> request = new HttpEntity(json,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    
    /* ===== ===== =====
        Convenience Methods
     * ===== ===== ===== */
    
    /* ===== ===== 
         CREATE
     * ===== =====  */ 
    
    public String createDevice_post(Map json, HttpSession session){
        
        String URI = URL + "/device";
        
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        HttpEntity<Map<String, String>> request = new HttpEntity<>(json, postHeaders);
        
        ResponseEntity<String> responseEntity = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);

        String constructId = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            constructId = responseEntity.getBody();
        }

        return constructId;
    }

    public String createPart_post(Map json, HttpSession session) {

        String URI = URL + "/part";
        
        HttpHeaders postHeaders = new HttpHeaders();
        postHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        HttpEntity<Map<String, String>> request = new HttpEntity<>(json, postHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
        
        String constructId = null;
        
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            constructId = responseEntity.getBody();
        }
        
        return constructId;
    }

    /* ===== ===== 
         READ
     * ===== =====  */ 
    
    public String getPartById_get(String biodesignId, HttpSession session){
        
        String URI = URL + "/part/" + biodesignId;
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        Map map = new HashMap<>();
        
        HttpEntity<?> request = new HttpEntity(map,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.GET, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    public String getPartWithFilter_put(Map json, String filter, HttpSession session){
        
        String URI = URL + "/part/" + filter;       
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        HttpEntity<?> request = new HttpEntity(json,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.PUT, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    public String getDeviceById_get(String biodesignId, HttpSession session){
        
        String URI = URL + "/device/" + biodesignId;
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        Map map = new HashMap<>();
        
        HttpEntity<?> request = new HttpEntity(map,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.GET, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    public String getDeviceWithFilter_put(Map json, String filter, HttpSession session){
        
        String URI = URL + "/part/" + filter;       
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        HttpEntity<?> request = new HttpEntity(json,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.PUT, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    public String getBioDesignWithFilter_put(Map json, String filter, HttpSession session){
        
        String URI = URL + "/bioDesign/search";       
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        HttpEntity<?> request = new HttpEntity(json,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.PUT, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    /* ===== ===== 
         UPDATE
     * ===== =====  */ 
    
        public String updateDeviceById_put(String biodesignId, Map map, HttpSession session){
        
        String URI = URL + "/part/update/" + biodesignId;
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        
        HttpEntity<?> request = new HttpEntity(map,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.PUT, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    /* ===== ===== 
         DELETE
     * ===== =====  */ 
        
        public String deleteDeviceById_del(String biodesignId, HttpSession session){
        
        String URI = URL + "/part/" + biodesignId;
        
        HttpHeaders getHeaders = new HttpHeaders();
        getHeaders.add("Authorization", (String) session.getAttribute("authHeader"));
        Map map = new HashMap<>();
        
        HttpEntity<?> request = new HttpEntity(map,getHeaders);
        
        ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.DELETE, request, String.class);
        
        String partDetails = null;
        
        if (response.getStatusCode() == HttpStatus.OK){
            partDetails = response.getBody();    
        }
        
        return partDetails;
        
    }
    
    public void test(){
        System.out.println("The ClothoService is connected to this class.");
    }
    
}
