/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.services;

import java.util.List;
import javax.servlet.http.HttpSession;
import org.cidarlab.garuda.legacyutil.CategoricalRecEngine;
import org.cidarlab.garuda.legacyutil.RWR_RecEngine;
import org.springframework.stereotype.Service;

/**
 *
 * @author jayajr
 */
@Service
public class MLService {
    
        private String message;
        private String[] partnames;
        private double[] probabilities;
    
        public void recommend(String username, String fileLocation, HttpSession session) {

        switch (username) {
            case "mardian":

                int num_of_parts = 21;
                int num_of_constructs = 14;
                int size_of_constructs = 2;
                String labelSheet = "Results";
                String featuresSheet = "Results";
                int labelIdx = 17;
                int[] featuresIdx = new int[]{2, 3};
                String null_flag = "N/A";

                CategoricalRecEngine rec = new CategoricalRecEngine(username, fileLocation, labelSheet, featuresSheet, labelIdx, featuresIdx, num_of_parts, num_of_constructs, size_of_constructs, null_flag);
                //this.message = rec.recommend_expert();
                List<String> output_ = rec.mRegression();

                String[] part_temp_ = output_.get(0).split(",");
                String[] prob_string_ = output_.get(1).split(",");
                
                String[] part_all_ = rec.getPartnames();
                
                this.partnames = new String[part_temp_.length];
                for (int i = 0; i < part_temp_.length; i++) {
                    int idx = Integer.parseInt(part_temp_[i].substring(1));
                    this.partnames[i] = part_all_[idx];
                }
                
                this.probabilities = new double[prob_string_.length];
                for (int i = 0; i < this.probabilities.length; i++) {
                    //System.out.println (prob_temp[i] + "  " + max + "  " + min);
                    this.probabilities[i] = Double.parseDouble(prob_string_[i]);
                }

                
                break;

            case "robwarden":
                
                //this.message = RWR_RecEngine.nnbackprop("resources/" + this.filename, username);
                //this.message = RWR_RecEngine.expert("resources/" + this.filename, username);
                //this.message = RWR_RecEngine.naivebayes("resources/" + this.filename, username);
                
                List<String> output = RWR_RecEngine.mRegression(fileLocation, username);

                String[] part_temp = output.get(0).split(",");
                String[] prob_string = output.get(1).split(",");
                
                String[] part_all = RWR_RecEngine.getPartnames();
                
                this.partnames = new String[part_temp.length];
                for (int i = 0; i < part_temp.length; i++) {
                    int idx = Integer.parseInt(part_temp[i].substring(1));
                    this.partnames[i] = part_all[idx];
                }
                
                //////
                double[] prob_temp = new double[prob_string.length];
                double min = 1/Double.parseDouble(prob_string[0]);
                double max = 1/Double.parseDouble(prob_string[0]);
                for (int i = 0; i < prob_string.length; i++) {
                    prob_temp[i] = 1/Double.parseDouble(prob_string[i]);
                    if(prob_temp[i] < min) {
                        min = prob_temp[i];
                    }
                    if(prob_temp[i] > max) {
                        max = prob_temp[i];
                    }
                }
                //////
                
                this.probabilities = new double[prob_string.length];
                for (int i = 0; i < this.probabilities.length; i++) {
                    //System.out.println (prob_temp[i] + "  " + max + "  " + min);
                    //double val = 1/Double.parseDouble(prob_string[i]);
                    //System.out.println(val + "   " + min + "   " + max);
                    this.probabilities[i] = Double.parseDouble(prob_string[i]);
                }

                break;
            case "guy":
                System.out.println("ERROR: recommendation engine is not available for this user!");
                break;
            default:
                System.out.println("ERROR: username not found!");
                break;
        }
        this.message = "Recommendation generated!";
    }

    public void recommend_nn(String username, String fileLocation, HttpSession session) {

        switch (username) {
            case "mardian":

                int num_of_parts = 21;
                int num_of_constructs = 14;
                int size_of_constructs = 2;
                String labelSheet = "Results";
                String featuresSheet = "Results";
                int labelIdx = 17;
                int[] featuresIdx = new int[]{2, 3};
                String null_flag = "N/A";

                CategoricalRecEngine rec = new CategoricalRecEngine(username, fileLocation, labelSheet, featuresSheet, labelIdx, featuresIdx, num_of_parts, num_of_constructs, size_of_constructs, null_flag);
                //this.message = rec.recommend_expert();

                List<String> output_ = rec.mRegression();

                System.out.println("****Pass this2!!");
                
                String[] part_temp_ = output_.get(0).split(",");
                String[] prob_string_ = output_.get(1).split(",");
                
                String[] part_all_ = rec.getPartnames();
                
                this.partnames = new String[part_temp_.length];
                for (int i = 0; i < part_temp_.length; i++) {
                    int idx = Integer.parseInt(part_temp_[i].substring(1));
                    this.partnames[i] = part_all_[idx];
                }
                
                this.probabilities = new double[prob_string_.length];
                for (int i = 0; i < this.probabilities.length; i++) {
                    //System.out.println (prob_temp[i] + "  " + max + "  " + min);
                    this.probabilities[i] = Double.parseDouble(prob_string_[i]);
                }

                
                break;

            case "robwarden":
                
                this.message = RWR_RecEngine.nnbackprop("resources/" + fileLocation, username);
                
                /*String[] part_temp = output.get(0).split(",");
                String[] prob_string = output.get(1).split(",");
                
                String[] part_all = rec.getPartnames();*/
                
                this.partnames = new String[]{"A", "B"};
                /*for (int i = 0; i < part_temp.length; i++) {
                    int idx = Integer.parseInt(part_temp_[i].substring(1));
                    this.partnames[i] = part_all[idx];
                }*/
                
                this.probabilities = new double[]{35.0, 72.0};
                /*for (int i = 0; i < this.probabilities.length; i++) {
                    //System.out.println (prob_temp[i] + "  " + max + "  " + min);
                    this.probabilities[i] = Double.parseDouble(prob_string_[i]);
                }*/


                break;
            case "guy":
                System.out.println("ERROR: recommendation engine is not available for this user!");
                break;
            default:
                System.out.println("ERROR: username not found!");
                break;
        }
        this.message = "Recommendation generated!";
    }
}
