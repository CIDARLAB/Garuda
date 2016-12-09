/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho.rest;


/**
 *
 * @author mardian
 */
import org.cidarlab.main.thomoclotho.ApplicationInit;
import org.cidarlab.main.thomoclotho.ApplicationUsage;
import org.cidarlab.main.thomoclotho.RunBlast;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RESTController {

    @RequestMapping(value="/blast", method=RequestMethod.GET)
    public String indexBlast (Model model) {
        String blastpath = "/Users/mardian/Documents/CIDAR/Garuda/Spring/Garuda";
        RunBlast runblast = new RunBlast (false, RunBlast.Filters.SUBSEQ_ID, blastpath);
        model.addAttribute("runblast", runblast);
        return runblast.init();
    }
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String init (Model model) {
        
        //String user = "user123";
        model.addAttribute("app", new ApplicationInit());
        return "index";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login () {
        
        return "login";
        
    }
    
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public String index () {
        
        return "index";
    }
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String add () {
        
        return "add";
    }
    
    @RequestMapping(value="/import", method=RequestMethod.GET)
    public String spreadsheet () {
        
        return "import";
    }
    
    @RequestMapping(value="/entry", method=RequestMethod.POST)
    public String entry (@ModelAttribute ApplicationInit app, Model model) {
        
        String user = "user123";
        String pass = "pass";
        
        String input = "input";
        String output = "output";
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        app.init(user, pass, inputFile, outputFile);
        model.addAttribute("app", app);
        return "index";
    }
    
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public String search (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "search";
        
    }
    
    @RequestMapping(value="/result", method=RequestMethod.POST)
    public String result (@ModelAttribute ApplicationUsage app, Model model) {
        
        String user = "user123";
        String pass = "pass";
        
        app.init (user, pass);
        model.addAttribute("app", app);
        return "result";
        
    }
    
    @RequestMapping(value="/recommender", method=RequestMethod.GET)
    public String recommender () {
        
        return "recommender";
    }
    
}
