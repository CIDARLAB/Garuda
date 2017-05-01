/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.CelloAnalysis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author mardian
 */
public class PythonRunner {

    static final String path = "./garuda_python/src/predictor.py";

    public enum SCRIPT_OPTIONS{
        PREDICTOR,
        TRAINER
    }

    String output;

    public PythonRunner() {


    }

    public String runPredictor(String netlist) {

        ArrayList<String> gates = new ArrayList<>();

        String GATE_REGEX = "\\w+_\\w+";

        Pattern regex = Pattern.compile(GATE_REGEX);
        Matcher matcher = regex.matcher(netlist);
        while(matcher.find()){

            gates.add(netlist.substring(matcher.start(),matcher.end()));

        }

        String gatesinput = gates.toString();
        gatesinput.replace('[',' ');
        gatesinput.replace(']',' ');


        String ret= "";


        ProcessBuilder ps = new ProcessBuilder("python", path, gatesinput);

        //From the DOC:  Initially, this property is false, meaning that the
        //standard output and error output of a subprocess are sent to two
        //separate streams

        try {

            ps.redirectErrorStream(true);

            Process pr = ps.start();

            BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                ret += line + "\n";
                System.out.println(line);
            }
            pr.waitFor();

            in.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }
}
