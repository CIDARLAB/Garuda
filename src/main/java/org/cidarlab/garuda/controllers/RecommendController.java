/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.MLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jayajr
 */
@Controller
public class RecommendController {
    
    @Autowired
    MLService ml;
    
    private String fileLocation;
    
    @RequestMapping(value="/recommender", method=RequestMethod.GET)
    public String getRecommendPage(HttpSession session, Model model) { 
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "recommender";
    }
    
    @RequestMapping(value="/recommendation", method=RequestMethod.GET)
    public String recommendation_get (Model model, HttpSession session) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "recommender";
    }
    
    @RequestMapping(value="/recommendation", method=RequestMethod.POST)
    public String recommendation (
            @RequestParam("multipartFile") MultipartFile multipartFile,
            HttpSession session,
            Model model) throws IOException {
        
        if (multipartFile.isEmpty()){
            System.out.println("File empty!");
            return "import";
        }
        
    //{ Uploading the file
        InputStream in = multipartFile.getInputStream();
        File currDir = new File(".");

        String path = currDir.getAbsolutePath();

        fileLocation = path.substring(0, path.length() - 1) + multipartFile.getOriginalFilename();
        //fileLocation = "/resources/" + multipartFile.getOriginalFilename();

        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;

        while ((ch=in.read()) != -1){
            f.write(ch);
        }

        f.flush();
        f.close();

        System.out.println("file uploaded!");
        System.out.println("location at " + currDir.getAbsolutePath());
        model.addAttribute("message", "File: " + multipartFile.getOriginalFilename() + " has been uploaded successfully!");

    //} Uploading the file
        
        
    //{ Parsing the file
        
        String user = (String) session.getAttribute("username");
        
        System.out.println(user);
        
        
        long startTime = System.currentTimeMillis();
        ml.recommend_nn (user, fileLocation, session);
        System.out.println("********** Running time for recommendation engine: " + (System.currentTimeMillis()-startTime) + " ms.");
        
        
        System.out.println("done parsing");
    
    //} Parsing the file

        
        return "recommendation";
    }
}
