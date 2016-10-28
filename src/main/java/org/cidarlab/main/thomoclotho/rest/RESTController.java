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
import org.cidarlab.main.thomoclotho.RunBlast;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RESTController {

        
    @RequestMapping(value="/", method=RequestMethod.GET)
    public String index (Model model) {
        RunBlast runblast = new RunBlast (false, RunBlast.Filters.SUBSEQ_ID);
        model.addAttribute("runblast", runblast);
        return runblast.init();
    }
    
    @RequestMapping(value="/clotho", method=RequestMethod.GET)
    public String indexClotho (Model model) {
        
        String user = "user123";
        String pass = "pass";
        
        String input = "input";
        String output = "output";
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        ApplicationInit app = new ApplicationInit(user);
        
        //RunBlast runblast = new RunBlast (false, RunBlast.Filters.SUBSEQ_ID);
        model.addAttribute("appinit", app);
        return app.init(user, pass, inputFile, outputFile);
    }
    
    
    
    /*@RequestMapping("/runblast")
    public String greeting(@RequestParam(value="runblast", defaultValue="seq1") String runblast) {
        return "runblast";
    }*/
}
