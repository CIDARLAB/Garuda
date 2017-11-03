/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author mardian
 */
public class PythonRunner {
    
    public PythonRunner(String filename, List<String> arguments) {
        
        String path = "resources/" + filename;
        
        for (int i = 0; i < arguments.size(); i++) {
            path = path + " " + arguments.get(i);
        }
        
        try {

            Process p = Runtime.getRuntime().exec("python " + path);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getErrorStream()));
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
