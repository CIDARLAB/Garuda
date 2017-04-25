package org.cidarlab.main.thomoclotho.rest;

import org.cidarlab.main.thomoclotho.ApplicationInit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by krishna on 4/25/17.
 */

@Controller
public class CelloController {

    @RequestMapping(value="/cello", method= RequestMethod.GET)
    public String cello_home (Model model) {


        return "cello";
    }

    @RequestMapping(value="/cello/alternate", method= RequestMethod.GET)
    public String cello_alternate (Model model) {


        return "cello_alternate";
    }

    @RequestMapping(value="/cello/generate", method= RequestMethod.GET)
    public String cello_generate (Model model) {


        return "cello_generate";
    }

    @RequestMapping(value="/cello/predict", method= RequestMethod.GET)
    public String cello_predict (Model model) {


        return "cello_predict";
    }



}
