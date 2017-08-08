/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.AddForm;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.services.ClothoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author jayajr
 */
@Controller
public class AddController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String getAddPage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        AddForm form = new AddForm();
        form.setParameters("[]");
        
        model.addAttribute("addForm", form);
        return "add";
    }
    
    /*
     * Note: postAddPage returns a String due to @ResponseBody annotation. 
     *       Remove to route to a different page.
     */    
    
    
    @RequestMapping(value="/add", method=RequestMethod.POST)
    public String postAddPage(
            AddForm addForm,
            HttpSession session,
            Model model) throws IOException {
        
        if (addForm.getParameters() == null){
            addForm.setParameters("[]");
        }
        
        ObjectMapper mapper = new ObjectMapper();
        
        String result = null;
        
        try {
            System.out.println(addForm.toPartJsonString());
            String partId = clotho.createPart_post(addForm.toPartMap(), session);
            
            if (partId == null) {
                throw new RuntimeException();
            } else {
                result = clotho.getPartById_get(partId, session);
            }

        } catch (RuntimeException e) {
            return "/error";
        } finally {
            
            Object jsonObj = mapper.readValue(result, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
            model.addAttribute("result", indented);
            
            return "result";
        }
    }
    
    @RequestMapping(value="/add_device", method=RequestMethod.GET)
    public String getAddDevicePage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        AddForm form = new AddForm();
        form.setParameters("[]");
        form.setPartIds("[]");
        
        model.addAttribute("addForm", form);
        return "add_device";
    }
    
    /*
     * Note: postAddPage returns a String due to @ResponseBody annotation. 
     *       Remove to route to a different page.
     */    
    
    
    @RequestMapping(value="/add_device", method=RequestMethod.POST)
    public String postAddDevicePage(
            AddForm addForm,
            HttpSession session,
            Model model) throws IOException {
        
        if (addForm.getParameters() == null){
            addForm.setParameters("[]");
        }
        
        ObjectMapper mapper = new ObjectMapper();
        
        String result = null;
        
        try {
            System.out.println(addForm.toDeviceJsonString());
            String partId = clotho.createDevice_post(addForm.toDeviceMap(), session);
            
            if (partId == null) {
                throw new RuntimeException();
            } else {
                result = clotho.getDeviceById_get(partId, session);
            }

        } catch (RuntimeException e) {
            return "/error";
        } finally {
            
            Object jsonObj = mapper.readValue(result, Object.class);
            String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
            model.addAttribute("result", indented);
            
            return "result";
        }
    }
    
    @RequestMapping(value="/add_metadata", method=RequestMethod.GET)
    public String getAddMetadataPage(HttpSession session, Model model) {
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_metadata";
    }
    
    @RequestMapping(value="/add_constructs", method=RequestMethod.GET)
    public String getAddConstructsPage(HttpSession session, Model model) {
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_constructs";
    }
    
    @RequestMapping(value="/add_gendata", method=RequestMethod.GET)
    public String getAddGendataPage(HttpSession session, Model model) {    
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_gendata";
    }
    
    @RequestMapping(value="/add_parts", method=RequestMethod.GET)
    public String getAddPartsPage(HttpSession session, Model model) {    
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_parts";
    }
    
    @RequestMapping(value="/add_qc", method=RequestMethod.GET)
    public String getAddQcPage(HttpSession session, Model model) {   
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_qc";
    }
    
    @RequestMapping(value="/add_rna", method=RequestMethod.GET)
    public String getaddRnaPage(HttpSession session, Model model) {     
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        return "add_rna";
    }
}

