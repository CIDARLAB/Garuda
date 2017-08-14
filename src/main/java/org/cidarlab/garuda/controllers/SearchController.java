/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.forms.RegisterForm;
import org.cidarlab.garuda.forms.SearchForm;
import org.cidarlab.garuda.services.ClothoService;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
@RequestMapping(value="/search")
public class SearchController {
    
    @Autowired
    ClothoService clotho;
    
    @RequestMapping(method=RequestMethod.GET)
    public String getSearchPage(HttpSession session, Model model) {     
        
        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");
        
        if (user == null || authHeader == null){
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
        
            return "login";
        }
        
        model.addAttribute("searchForm", new SearchForm());
        return "search";
    }
    
    @RequestMapping(method=RequestMethod.POST)

    public String resolveSearch(
            SearchForm searchForm,
            HttpSession session,
            Model model) throws IOException, ParseException{
        
        ObjectMapper mapper = new ObjectMapper();
        
        try {
            if (searchForm.getBiodesignId() != null && !searchForm.getBiodesignId().isEmpty()){

                String json = clotho.getPartById_get(searchForm.getBiodesignId(), session);
                Object jsonObj = mapper.readValue(json, Object.class);
                String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
                model.addAttribute("result", indented);
                
            } else {
                
                if (searchForm.getSequence() != null && !searchForm.getSequence().isEmpty()){
                    searchForm.setBlast(true);
                
                    String filter = searchForm.getFilter();
                    Map json = searchForm.toMap();

                    if (filter != null && !filter.isEmpty()){
                        model.addAttribute("result", clotho.getPartWithFilter_put(json, filter, session));
                    } else {

                        String p1 = clotho.getBlast_post(json, session);

                        model.addAttribute("result", p1);

                    }
                } else {
                    String filter = searchForm.getFilter();
                    Map json = searchForm.toMap();

                    if (filter != null && !filter.isEmpty()){
                        model.addAttribute("result", clotho.getPartWithFilter_put(json, filter, session));
                    } else {

                        String p1 = clotho.getPartWithFilter_put(json, "_id", session);
                        String p2 = clotho.getPartWithFilter_put(json, "name", session);
                        String p3 = clotho.getPartWithFilter_put(json, "subparts", session);


                        String[] l1 = p1.split(",");
                        String[] l2 = p2.split(",");
                        String[] l3 = p3.split("},");


                        String output = "ID\t\t\t\tName\t\tSubparts\n";

                        int length = l1.length;

                        for (int i = 0; i < length; i++){
                            output = output + l1[i] + "\t" + l2[i]+ "\t\t\n";
                        }

                        model.addAttribute("result", output);

                    }
                }
                
            }
        } catch (RuntimeException e){
            model.addAttribute("result", "BioDesign not found");
            System.out.println(e.getMessage());
        }
        
        return "result";
    }
}

