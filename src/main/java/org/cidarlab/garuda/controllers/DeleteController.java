/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.DeleteForm;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.forms.SearchForm;
import org.cidarlab.garuda.legacyutil.FormatExchange;
import org.cidarlab.garuda.model.DNAPlotLib;
import org.cidarlab.garuda.services.ClothoService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author jayajr
 */
@Controller
public class DeleteController {

    @Autowired
    ClothoService clotho;
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public String getDeletePage(HttpSession session, Model model) {

        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");

        if (user == null || authHeader == null) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
            return "login";
        }
        
        model.addAttribute("deleteForm", new DeleteForm());
        
        return "delete";
    }

    @RequestMapping(value = "/delete_in_progress", method = RequestMethod.GET)
    public String resolveDelete(
            DeleteForm deleteForm,
            HttpSession session,
            Model model) throws IOException, ParseException {

        model.addAttribute("searchForm", new SearchForm());
        
        try {
            
            clotho.deleteDeviceById_del(deleteForm.getBiodesignId(), session);
            
            
        } catch (RuntimeException e) {
            model.addAttribute("result", "BioDesign not found");
            System.out.println(e.getMessage());
        }

        return "result";
    }

}
