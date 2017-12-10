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
import org.cidarlab.garuda.model.Part;
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
    public String getSearchPage(HttpSession session, Model model) {

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
        JSONArray results = null;

        try {
            //if search is by ID
            if (searchForm.getBiodesignId() != null && !searchForm.getBiodesignId().isEmpty()) {

                String json = clotho.getDeviceById_get(searchForm.getBiodesignId(), session);
                
                Object jsonObj = mapper.readValue(json, Object.class);
                String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);
                
                results = new JSONArray(json);
                searchForm.init(1);  //initialize array of entries with size=1 as there would only be 1 result
                
                JSONObject entry = results.getJSONObject(0);
                String _id = entry.getString("_id");
                String name = entry.getString("name");
                String type = entry.getString("type");
                
                searchForm.set("name", name, 0);
                
                /*boolean flag = true;
                try {

                    JSONArray subBioDesign = entry.getJSONArray("subBioDesignIds");
                    if (flag) {

                        //DNAPlotLib dnaplotlib = new DNAPlotLib(name);
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

                            //dnaplotlib.addPartRole(partname, role);
                            //tabular = tabular + partname + "\t" + role + ",";
                            sub = sub + partname + ",";

                            //FormatExchange.writeDPL("" + 0, dnaplotlib);
                        }
                        //tabular += "\n";

                        //FormatExchange.dnaplotlib_exec("" + 0);
                        //searchForm.set("url", "images/generated/0.png", 0);
                        searchForm.set("sub", sub.substring(0, sub.length() - 1), 0);
                    }
                    
                } catch (JSONException jx) {
                    flag = false;
                    searchForm.set("url", "", 0);
                    searchForm.set("sub", "", 0);
                }*/
                
                model.addAttribute("result", indented);

            }
            //if search is by sequence
            //not verified yet
            else if (searchForm.getSequence() != null && !searchForm.getSequence().isEmpty()) {

                searchForm.setBlast(true);

                String filter = searchForm.getFilter();

                Map json = searchForm.toMap();

                if (filter != null && !filter.isEmpty()) {
                    model.addAttribute("result", clotho.getDeviceWithFilter_put(json, filter, session));
                } else {

                    String p1 = clotho.getBlast_post(json, session);
                    model.addAttribute("result", p1);

                }
            }
            //if search is neither by ID nor sequence
            else {

                //String filter = searchForm.getFilter();

                Map json = searchForm.toMap();

                /*if (filter != null && !filter.isEmpty()) {
                    model.addAttribute("result", clotho.getDeviceWithFilter_put(json, filter, session));
                    
                } else {*/

                    String output = clotho.getBioDesignWithFilter_put(json, "name", session);
                    
                    Object jsonObj = mapper.readValue(output, Object.class);
                    String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObj);

                    String tabular = indented + "\n\nName\t\tSubparts\n\n";
                    
                    System.out.println(indented);
                    
                /*    results = new JSONArray(output); //array of match entries
                    searchForm.init(results.length());  //initialize array of entries as big as the returned results
                    
                    //iterate through all results
                    for (int i = 0; i < results.length(); i++) {

                        JSONObject entry = results.getJSONArray(i).getJSONObject(0);    //each entry is a JSONarray
                        String _id = entry.getString("_id");
                        String name = entry.getString("name");
                        String type = entry.getString("type");
                        
                        searchForm.set("name", name, i);
                        
                        List<Part> parts = new ArrayList<Part>();
                        
                        if (type.equals("PART")) {
                            
                            //what a crazy way just to obtain a part
                            JSONObject jsonobj = (new JSONArray(clotho.getPartById_get(_id, session))).getJSONObject(0);
                            String role = jsonobj.getJSONArray("modules").getJSONObject(0).getString("role");
                            if (role.equals("PROMOTER")) {
                                role = "Promoter";
                            } else if (role.equals("GENE")) {
                                role = "CDS";
                            } else if (role.equals("TERMINATOR")) {
                                role = "Terminator";
                            }

                            parts.add(new Part(name, role));
                        }
                        
                        if (type.equals("DEVICE")) {
                            
                            parts = parseDevice(parts, entry, session);
                        }
                        
                        ////
                        System.out.println("+++" + _id + "  " + name + "   " + type);
                        tabular = tabular + name + "\t\t";
                        
                        //String sub = "";
                        for (int x = 0; x < parts.size(); x++) {
                            tabular = tabular + parts.get(x).getName() + ",";
                            //sub = sub + parts.get(x).getName() + ",";
                            System.out.print("(" + parts.get(x).getName() + ", " + parts.get(x).getRole() + "); ");
                        }
                        tabular = tabular + "\n";
                        System.out.println();
                        ////
                        

                //    }
                    model.addAttribute("result", tabular);
                }*/
            }
        }
        catch (RuntimeException e) {
            
            model.addAttribute("result", "BioDesign not found");
            System.out.println(e.getMessage());
        }
        finally {
        
            if (results != null) {
                model.addAttribute("row", "Found " + results.length() + " match results!!!");
            } else {
                model.addAttribute("row", "There is something wrong. Found no match results!!!");
            }

            return "result";
        }
    }
    
    //getting map of assembly order
    /*private Map<String, String> retrieveOrder(Map<String, String> map, JSONObject jsonobj) {
        
        JSONArray subBioDesignIds = jsonobj.getJSONArray("subBioDesignIds");
        for (int j = 0; j < subBioDesignIds.length(); j++) {
            
            String part = subBioDesignIds.getString(j);
        }
        
        return map;
    }*/
    
    //getting list of parts
    private List<Part> parseDevice(List<Part> parts, JSONObject jsonobj, HttpSession session) {
        
        //String objId = jsonobj.getString("_id");
        JSONArray subBioDesignIds = jsonobj.getJSONArray("subBioDesignIds");
        
        for (int i = 0; i < subBioDesignIds.length(); i++) {
            
            String partId = subBioDesignIds.getString(i);
            JSONObject entry = (new JSONArray(clotho.getPartById_get(partId, session))).getJSONObject(0);
            
            String _id = entry.getString("_id");
            String name = entry.getString("name");
            String type = entry.getString("type");
            
            if (type.equals("PART")) {

                String role = entry.getJSONArray("modules").getJSONObject(0).getString("role");
                if (role.equals("PROMOTER")) {
                    role = "Promoter";
                } else if (role.equals("GENE")) {
                    role = "CDS";
                } else if (role.equals("TERMINATOR")) {
                    role = "Terminator";
                }

                parts.add(new Part(_id, name, role));

            }
            
            else if(type.equals("DEVICE")) {
                parts = parseDevice(parts, entry, session);
            }
        }
        
        
        
        /*try {
            JSONArray subDesign = jsonobj.getJSONArray("subdesigns");
            
            //System.out.println(subBioDesignIds);

            for (int i = 0; i < subBioDesignIds.length(); i++) {

                String partId = subBioDesignIds.getString(i);

                for (int j = 0; j < subDesign.length(); j++) {

                    JSONObject entry = subDesign.getJSONObject(j);

                    String _id = entry.getString("_id");
                    System.out.println("subbiodesign id: " + partId + ", subdesign id: " + _id);

                    if (!partId.equals(_id)) {
                        continue;
                    }

                    String name = entry.getString("name");
                    String type = entry.getString("type");
                    
                    System.out.println("Parse: " + name);
                    
                    if (type.equals("PART")) {

                        String role = entry.getJSONArray("modules").getJSONObject(0).getString("role");
                        if (role.equals("PROMOTER")) {
                            role = "Promoter";
                        } else if (role.equals("GENE")) {
                            role = "CDS";
                        } else if (role.equals("TERMINATOR")) {
                            role = "Terminator";
                        }

                        parts.add(new Part(_id, name, role));

                    } else if (type.equals("DEVICE")) {
                        
                        parts = parseDevice(parts, entry, session);
                    }
                }
            }
        }
        catch(Exception ex) {
            //for some reasons the search function will throw an error (incomplete JSON without subdesign) when searching devices together with parts.
            //  this try-catch captures the missing object and re-run the search of that device by its ID
            
            System.out.println("**Warning: an exception happened!");
            
            JSONObject expObj = (new JSONArray(clotho.getPartById_get(objId, session))).getJSONObject(0);
            String type = expObj.getString("type");
            
            if(type.equals("DEVICE")) {
                parts = parseDevice(parts, expObj, session);
            }
        }*/
        
        return parts;
        
    }

}
