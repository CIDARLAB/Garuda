/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.rest;


/**
 *
 * @author mardian
 */
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.cidarlab.garuda.forms.LoginForm;
import org.cidarlab.garuda.main.ApplicationInit;
import org.cidarlab.garuda.main.ApplicationUsage;
import org.cidarlab.garuda.main.RecommendationEngine;
import org.cidarlab.garuda.main.RunBlast;
import org.cidarlab.garuda.services.NotificationService;
import org.cidarlab.garuda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RESTController {

    //@Autowired
    private UserService userService;

    //@Autowired
    private NotificationService notifyService;

    @RequestMapping(value = "/users/login", method=RequestMethod.GET)
    public String login(LoginForm login) {
        return "login_test";
    }
    
    @RequestMapping(value = "/users/login", method=RequestMethod.POST)
    public String loginPage(@Valid LoginForm loginForm, BindingResult bindingResult) {
        System.out.println("**********It got here!!!");
        
        System.out.println("+++++++++++++++++++++++");
        boolean test = loginForm.getUsername()==loginForm.getPassword();
        System.out.println("+++++++++++++++++++++++" + test);
        
        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "login_test";
        }

        if (!userService.authenticate(
            loginForm.getUsername(), loginForm.getPassword())) {
            notifyService.addErrorMessage("Invalid login!");
            return "login_test";
        }

        System.out.println("**********It got here!!!");
        notifyService.addInfoMessage("Login successful");
        return "redirect:/";
    }
    
    ///for testing purpose
    

    @RequestMapping(value="/blast", method=RequestMethod.GET)
    public String indexBlast (Model model) {
        String blastpath = "/Users/mardian/Documents/CIDAR/Garuda/Spring/Garuda";
        RunBlast runblast = new RunBlast (false, RunBlast.Filters.SUBSEQ_ID, blastpath);
        model.addAttribute("runblast", runblast);
        return runblast.init();
    }
    
    @RequestMapping(value="/resource")
    public Map<String,Object> home () {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }
    
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String init (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "index";
    }
    
    @RequestMapping(value="/index", method=RequestMethod.GET)
    public String index (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "index";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.GET)
    public String login (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "login";
    }
    
    @RequestMapping(value="/login", method=RequestMethod.POST)
    public String login_post (@ModelAttribute ApplicationInit app, Model model) {
        
        app.login ();
        model.addAttribute("app", app);
        
        return "login";
    }
    
    @RequestMapping(value="/register", method=RequestMethod.POST)
    public String register (@ModelAttribute ApplicationInit app, Model model) {
        
        app.register ();
        model.addAttribute("app", app);
        
        return "login";
    }
    
    @RequestMapping(value="/home", method=RequestMethod.GET)
    public String home (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "index";
    }
    
    @RequestMapping(value="/add", method=RequestMethod.GET)
    public String add () {
        
        return "add";
    }
    
    @RequestMapping(value="/add_parts", method=RequestMethod.GET)
    public String add_parts () {
        
        return "add_parts";
    }
    
    @RequestMapping(value="/add_constructs", method=RequestMethod.GET)
    public String add_constructs () {
        
        return "add_constructs";
    }
    
    @RequestMapping(value="/add_gendata", method=RequestMethod.GET)
    public String add_gendata () {
        
        return "add_gendata";
    }
    
    @RequestMapping(value="/add_rna", method=RequestMethod.GET)
    public String add_rna () {
        
        return "add_rna";
    }
    
    @RequestMapping(value="/add_qc", method=RequestMethod.GET)
    public String add_qc () {
        
        return "add_qc";
    }
    
    @RequestMapping(value="/import", method=RequestMethod.GET)
    public String spreadsheet (Model model) {
        
        model.addAttribute("app", new ApplicationInit());
        return "import";
    }
    
    @RequestMapping(value="/import", method=RequestMethod.POST)
    public String entry (@ModelAttribute ApplicationInit app, Model model) {
        
        String user = "robwarden";
        String pass = "pass";
        
        //String input = "input";
        String output = "output";
        //String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        //app.init(user, pass, outputFile);
        long startTime = System.currentTimeMillis();
        app.createPart(user);
        System.out.println("*********** Running time for create parts and constructs: " + (System.currentTimeMillis()-startTime) + " ms.");
        model.addAttribute("app", app);
        return "import";
    }
    
    @RequestMapping(value="/search", method=RequestMethod.GET)
    public String search (Model model) {
        
        model.addAttribute("app", new ApplicationUsage());
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
    
    @RequestMapping(value="/result", method=RequestMethod.GET)
    public String result_get (Model model) {
        
        model.addAttribute("app", new ApplicationUsage());
        return "search";
        
    }
    
    @RequestMapping(value="/recommender", method=RequestMethod.GET)
    public String recommender () {
        
        return "recommender";
    }
    
    @RequestMapping(value="/recommendation", method=RequestMethod.POST)
    public String recommendation (@ModelAttribute RecommendationEngine app, Model model) {
        
        model.addAttribute("app", app);
        return "recommendation";
    }
    
    
    @RequestMapping(value="/synbiohub", method=RequestMethod.GET)
    public String synbiohub_search (Model model) {
        
        ApplicationUsage app = new ApplicationUsage();
        model.addAttribute("app", app);
        
        return "synbiohub_search";
    }
    
    @RequestMapping(value="/synbiohub", method=RequestMethod.POST)
    public String synbiohub_result (@ModelAttribute ApplicationUsage app, Model model) {
        
        app.fetchSynbioHub();
        model.addAttribute("app", app);
        
        return "synbiohub_result";
    }
    
    @RequestMapping(value="/synbiohubpost", method=RequestMethod.POST)
    public String synbiohub_post (@ModelAttribute ApplicationUsage app, Model model) {
        
        String username = "mardian@bu.edu";
        String password = "mardian";
        
        app.postSynbioHub(username, password);
        model.addAttribute("app", app);
        
        return "synbiohub_result";
    }
    
}
