/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.garuda.machinelearning;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

/**
 *
 * @author mardian
 */
public class CollaborativeFiltering {
   
    private static Table<String, String, Double> matrix;
    
    public static void start (String filename) {
        
        String line = "";
        matrix = HashBasedTable.create();
        
        Set<String> users = new HashSet<String>();
        Set<String> titles = new HashSet<String>();
        
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            while ((line = br.readLine())!=null) {
                // use comma as separator
                String[] lineArr = line.split(",");
                
                users.add(lineArr[0]);
                titles.add(lineArr[1]);
                matrix.put(lineArr[0], lineArr[1], Double.parseDouble(lineArr[2]));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        //int data_size = users.size() * titles.size();
        for (String target : users) {
            
            System.out.println("\n=======Agreement for gene: "+ target + "=======");
            
            Map tarMap = matrix.row(target);

            PearsonsCorrelation pear = new PearsonsCorrelation();

            Map corMap = new HashMap();

            for (String reference : users) {
                if (!reference.equals(target)) {

                    Map refMap = matrix.row(reference);

                    int data_size = refMap.size();
                    if (tarMap.size()<refMap.size())
                        data_size = tarMap.size();

                    double[] x = new double[data_size];
                    double[] y = new double[data_size];

                    int i = 0;
                    //for (Map.Entry<String, Double> mapEntry : smallMap.entrySet()) {
                    for (String title : titles) {
                        //String title = mapEntry.getKey();
                        if (tarMap.get(title)!=null && refMap.get(title)!=null) {
                            x[i] = (double) tarMap.get(title);
                            y[i] = (double) refMap.get(title);
                            i++;
                        }
                        /*else
                            System.out.println(target + "      " + reference + "       " + title);*/
                    }

                    corMap.put(reference, pear.correlation(x, y));
                    //System.out.println(reference + " : " + pear.correlation(x, y));
                }
            }

            for (String title : titles) {
                if (tarMap.get(title)==null) {
                    double addValue = 0.0;
                    double sumCorval = 0.0;
                    for (String user : users) {
                        if (matrix.get(user, title)!=null) {
                            addValue += (matrix.get(user, title) * (double) corMap.get(user));
                            sumCorval += (double) corMap.get(user);
                        }
                    }
                    System.out.println(title + "   " + (addValue / sumCorval));
                }
                /*else
                    System.out.println("N/A");*/
            }
        }
    }
    
}
