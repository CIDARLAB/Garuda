package org.cidarlab.main.thomoclotho.rest;

import org.cidarlab.main.CelloAnalysis.PythonRunner;
import org.cidarlab.main.thomoclotho.ApplicationInit;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import java.util.Map;

/**
 * Created by krishna on 4/25/17.
 */

@Controller
public class CelloController {

    @RequestMapping(value="/cello", method= RequestMethod.GET)
    public String loadCelloHomePage (Model model) {


        return "cello";
    }

    @RequestMapping(value="/cello/alternate", method= RequestMethod.GET)
    public String loadCelloAlternatePage (Model model) {


        return "cello_alternate";
    }

    @RequestMapping(value="/cello/generate", method= RequestMethod.GET)
    public String loadCelloGeneratePage (Model model) {


        return "cello_generate";
    }

    @RequestMapping(value="/cello/predict", method= RequestMethod.GET)
    public String loadCelloPredictPage (Model model) {


        return "cello_predict";
    }

    @RequestMapping(value = "/api/v1/cello/predict", method = RequestMethod.POST)
    public ResponseEntity<String> calculatePrediction ( @RequestParam Map<String, String> params){
        //TODO: Run the Python scripts and pipe the output here
        PythonRunner pythonRunner = new PythonRunner();
        String netlist = params.get("netlist");
        String response = pythonRunner.runPredictor(netlist);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @RequestMapping(value = "/api/v1/cello/ucfs", method = RequestMethod.GET)
    public ResponseEntity<String> getUCFs ( @RequestParam Map<String, String> params){
        //TODO: Get user's ucfs and then send the ids to the user
        return new ResponseEntity<>("['Hello', 'World']", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/cello/ucf", method = RequestMethod.POST)
    public ResponseEntity<String> submitUCF ( @RequestParam Map<String, String> params){
        //TODO: Upload the UCF
        return new ResponseEntity<>("['Hello', 'World']", HttpStatus.OK);
    }

    @RequestMapping(value = "/api/v1/cello/generate", method = RequestMethod.POST)
    public ResponseEntity<String> runSimulations ( @RequestParam Map<String, String> params){
        //TODO: Upload the UCF
        return new ResponseEntity<>("['Hello', 'World']", HttpStatus.OK);
    }


}
