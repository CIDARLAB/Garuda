/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 *
 * @author mardian
 */
public class PythonRunner {
    
    public PythonRunner() {
        
        String path = "resources/garuda_sm.py";
        try {

            Process p = Runtime.getRuntime().exec("python " + path);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String ret = in.readLine();
            while (ret!=null) {
                System.out.println("value is : " + ret);
                ret = in.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
