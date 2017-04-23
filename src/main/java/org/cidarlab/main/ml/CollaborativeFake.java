/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mardian
 */
public class CollaborativeFake {
    
    public static void init(double[][] perfgroData) {
        
        int toxic = 0;
        int small = 0;
        
        List<Integer> toxicC = new ArrayList<Integer>();
        List<Integer> nontoxicC = new ArrayList<Integer>();
        List<Integer> uperformC = new ArrayList<Integer>();
        
        for (int i=0; i<perfgroData.length; i++) {
            if (perfgroData[i][1]<=0.3) {
                toxic++;
                toxicC.add(i);
            }
            else
                nontoxicC.add(i);
            
            if (perfgroData[i][0]<50.0) {
                small++;
                uperformC.add(i);
            }
        }
        System.out.println("**********total: " + perfgroData.length);
        System.out.println(toxic);
        System.out.println(small);
        
        int partNum = 20;
        int[] partOccurCount = new int [partNum];
        int[] partOccurCount2 = new int [partNum];
        
        for (int j=0; j<toxicC.size(); j++)
        {
            partOccurCount[(int)(perfgroData[toxicC.get(j)][2])-1]++;
            partOccurCount[(int)(perfgroData[toxicC.get(j)][3])-1]++;
            partOccurCount[(int)(perfgroData[toxicC.get(j)][4])-1]++;
            partOccurCount[(int)(perfgroData[toxicC.get(j)][5])-1]++;
        }
        
        for (int j=0; j<nontoxicC.size(); j++)
        {
            partOccurCount2[(int)(perfgroData[nontoxicC.get(j)][2])-1]++;
            partOccurCount2[(int)(perfgroData[nontoxicC.get(j)][3])-1]++;
            partOccurCount2[(int)(perfgroData[nontoxicC.get(j)][4])-1]++;
            partOccurCount2[(int)(perfgroData[nontoxicC.get(j)][5])-1]++;
        }
        
        for (int x=0; x<partOccurCount.length; x++)
        {
            int a = partOccurCount2[x];
            int b = partOccurCount[x];
            System.out.println(Math.abs(a-b) + "    " + a + "   " + b);
        }
        
    }
} 
