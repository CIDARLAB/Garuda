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
import org.cidarlab.garuda.services.Guy_Parser;
import org.cidarlab.garuda.services.RM_Parser;
import org.cidarlab.garuda.services.RWR_Parser;
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
@RequestMapping(value="/import")
public class ImportController {
    
    @Autowired
    private static Guy_Parser guyparser;
    
    @Autowired
    private static RM_Parser rmparser;
    
    @Autowired
    private static RWR_Parser rwrparser;
    
    private String fileLocation;
    
    @RequestMapping(method=RequestMethod.GET)
    public String getImportPage(HttpSession session, Model model) { 
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "import";
    }
    
    @RequestMapping(method=RequestMethod.POST)
    public String importFile(
            @RequestParam("multipartFile") MultipartFile multipartFile,
            HttpSession session,
            Model model)
            throws IOException{
        
        
        if (multipartFile.isEmpty()){
            System.out.println("File empty!");
            return "recommender";
        }
        
    //{ Uploading the file
        InputStream in = multipartFile.getInputStream();
        File currDir = new File(".");

        String path = currDir.getAbsolutePath();

        fileLocation = path.substring(0, path.length() - 1) + multipartFile.getOriginalFilename();

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
        
        
        if (user.equals("robwarden")){
            System.out.println("parsing with RW");
            rwrparser.parse(fileLocation, user, session);
        } else if (user.equals("guy")){
            System.out.println("parsing with Guy");
            guyparser.parse(fileLocation, "resources/output-", user, session);
        } else {
            System.out.println("parsing with RM");
            rmparser.parse(fileLocation, user, session);
        }
        
        System.out.println("done parsing");
    
    //} Parsing the file
        
        return "/upload";
    }
    
    @RequestMapping(value = "/upload", method=RequestMethod.GET)
    public String getUploadPage(HttpSession session, Model model) {  
        return "upload";
    }
}
