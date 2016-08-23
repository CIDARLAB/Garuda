/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

/**
 *
 * @author mardian
 */
public class ApplicationStarter {
    
    public static void main(String[] args) {
        
        String input = "input-full";
        String output = "output";
        
        String inputFile = "resources/" + input + ".xlsx";
        String outputFile = "resources/" + output + "-";
        
        Application.initiate("user" + System.currentTimeMillis(), "pass", inputFile, outputFile);
    }
    
}
