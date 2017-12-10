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
@RequestMapping(value = "/search")
public class SearchController {

    @Autowired
    ClothoService clotho;

    @RequestMapping(method = RequestMethod.GET)
    public String getSearchPage(SearchController searchController, HttpSession session, Model model) {

        String user = (String) session.getAttribute("username");
        String authHeader = (String) session.getAttribute("authHeader");

        if (user == null || authHeader == null) {
            model.addAttribute("loginForm", new LoginForm());
            model.addAttribute("registerForm", new RegisterForm());
            return "login";
        }

        model.addAttribute("searchForm", new SearchForm());
        return "search";
    }

    @RequestMapping(method = RequestMethod.POST)

    public String resolveSearch(
            SearchForm searchForm,
            HttpSession session,
            Model model) throws IOException, ParseException {

        ObjectMapper mapper = new ObjectMapper();

        try {
            if (searchForm.getBiodesignId() != null && !searchForm.getBiodesignId().isEmpty()) {

                String json = clotho.getDeviceById_get(searchForm.getBiodesignId(), session);
                Object jsonObj = mapper.readValue(json, Object.class);
                String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
                model.addAttribute("result", indented);
                
                JSONArray out_arr = new JSONArray(json);
                JSONObject entry = out_arr.getJSONObject(0);
                String _id = entry.getString("_id");
                String name = entry.getString("name");

                searchForm.init(1);  //initialize array of outputs
                searchForm.set("name", name, 0);
                
                boolean flag = true;
                try {

                    JSONArray subBioDesign = entry.getJSONArray("subBioDesignIds");
                    if (flag) {

                        DNAPlotLib dnaplotlib = new DNAPlotLib(name);
                        String sub = "";

                        for (int j = 0; j < subBioDesign.length(); j++) {

                            String part = clotho.getPartById_get(subBioDesign.getString(j), session);
                            JSONObject object = (new JSONArray(part)).getJSONObject(0);
                            String partname = object.getString("name");
                            String role = object.getJSONArray("modules").getJSONObject(0).getString("role");

                            if (role.equals("PROMOTER")) {
                                role = "Promoter";
                            } else if (role.equals("GENE")) {
                                role = "CDS";
                            } else if (role.equals("TERMINATOR")) {
                                role = "Terminator";
                            }

                            dnaplotlib.addPartRole(partname, role);
                            //tabular = tabular + partname + "\t" + role + ",";
                            sub = sub + partname + ",";

                            FormatExchange.writeDPL("" + 0, dnaplotlib);
                        }
                        //tabular += "\n";

                        FormatExchange.dnaplotlib_exec("" + 0);
                        searchForm.set("url", "images/generated/0.png", 0);
                        searchForm.set("sub", sub.substring(0, sub.length() - 1), 0);
                    }
                } catch (JSONException jx) {
                    flag = false;
                    searchForm.set("url", "", 0);
                    searchForm.set("sub", "", 0);
                }
                

            } else if (searchForm.getSequence() != null && !searchForm.getSequence().isEmpty()) {

                searchForm.setBlast(true);

                String filter = searchForm.getFilter();

                Map json = searchForm.toMap();

                if (filter != null && !filter.isEmpty()) {
                    model.addAttribute("result", clotho.getPartWithFilter_put(json, filter, session));
                } else {

                    String p1 = clotho.getBlast_post(json, session);

                    model.addAttribute("result", p1);

                }
            } else {

                String filter = searchForm.getFilter();

                Map json = searchForm.toMap();

                if (filter != null && !filter.isEmpty()) {
                    model.addAttribute("result", clotho.getPartWithFilter_put(json, filter, session));
                } else {

                    String output = clotho.getDeviceWithFilter_put(json, "name", session);
                    Object jsonObj = mapper.readValue(output, Object.class);
                    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);

                    String tabular = "Name\t\tSubparts\n\n";
                    
                    JSONArray out_arr = new JSONArray(output);
                    
                    searchForm.init(out_arr.length());  //initialize array of outputs

                    for (int i = 0; i < out_arr.length(); i++) {

                        JSONObject entry = out_arr.getJSONArray(i).getJSONObject(0);
                        String _id = entry.getString("_id");
                        String name = entry.getString("name");
                        
                        searchForm.set("name", name, i);
                        //tabular = tabular + (i + 1) + "\t" + _id + "\t" + name + "\t\n";
                        tabular = tabular + name + "\t";

                        boolean flag = true;
                        try {
                            
                            JSONArray subBioDesign = entry.getJSONArray("subBioDesignIds");
                            if (flag) {
                                
                                DNAPlotLib dnaplotlib = new DNAPlotLib(name);
                                String sub = "";

                                for (int j = 0; j < subBioDesign.length(); j++) {

                                    String part = clotho.getPartById_get(subBioDesign.getString(j), session);
                                    JSONObject object = (new JSONArray(part)).getJSONObject(0);
                                    String partname = object.getString("name");
                                    String role = object.getJSONArray("modules").getJSONObject(0).getString("role");
                                    
                                    if(role.equals("PROMOTER")) role = "Promoter";
                                    else if(role.equals("GENE")) role = "CDS";
                                    else if(role.equals("TERMINATOR")) role = "Terminator";
                                    
                                    dnaplotlib.addPartRole(partname, role);
                                    //tabular = tabular + partname + "\t" + role + ",";
                                    tabular = tabular + partname + ",";
                                    sub = sub + partname + ",";
                                    
                                    FormatExchange.writeDPL("" + i, dnaplotlib);
                                }
                                tabular += "\n";

                                FormatExchange.dnaplotlib_exec("" + i);
                                searchForm.set("url", "images/generated/" + i + ".png", i);
                                searchForm.set("sub", sub.substring(0, sub.length()-1), i);
                            }
                        } catch (JSONException jx) {
                            flag = false;
                            searchForm.set("url", "", i);
                            //searchForm.set("sub", "", i);
                        }

                    }
                    model.addAttribute("row", "Found " + out_arr.length() + " matches objects!!!");
                    model.addAttribute("result", tabular);
                }
            }
        } catch (RuntimeException e) {
            model.addAttribute("result", "BioDesign not found");
            System.out.println(e.getMessage());
        }

        return "result";
    }

}
