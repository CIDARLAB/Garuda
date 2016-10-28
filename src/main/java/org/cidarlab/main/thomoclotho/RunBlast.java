/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.thomoclotho;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author mardian
 */
public class RunBlast {
    
    private Set<String> output = new HashSet<String>();
    
    private double evalue = 10.0;
    private int threshold = 820;
    private double pthreshold = 99.00;
    
    private boolean initdb;
    private Filters outputformat;
    
    public RunBlast(boolean initdb, Filters outputformat) {
        this(10.0, 820, 99.00, initdb, outputformat);
    }
    
    public RunBlast (double evalue, int threshold, double pthreshold, boolean initdb, Filters outputformat) {
        this.output = new HashSet<String>();
        this.evalue = evalue;
        this.threshold = threshold;
        this.pthreshold = pthreshold;
        this.initdb = initdb;
        this.outputformat = outputformat;
    }
        
    
    public String init () {
        
        List<String> makedb = new ArrayList();
        makedb.add("/usr/local/ncbi/blast/bin/makeblastdb");
        makedb.add("-in");
        makedb.add("/Users/mardian/NetBeansProjects/ThomoClotho/resources/output-clotho_constructsdb.fsa");
        makedb.add("-parse_seqids");
        makedb.add("-dbtype");
        makedb.add("nucl");
        
        //exec(makedb);
        
        List<String> runquery = new ArrayList();
        runquery.add("/usr/local/ncbi/blast/bin/blastn");
        runquery.add("-db");
        runquery.add("resources/output-clotho_constructsdb.fsa");
        runquery.add("-query");
        runquery.add("/Users/mardian/documents/CIDAR/clothoquery.fsa");
        
        if (outputformat!=Filters.DEFAULT) {
            runquery.add("-outfmt");
            if (outputformat==Filters.SUBSEQ_ID) {
                runquery.add("6 sseqid");
            }
            else if (outputformat==Filters.SUBSEQ) {
                runquery.add("6 sseq");
            }
            else if (outputformat==Filters.EVALUE) {
                runquery.add("6 sseqid evalue");
            }
            else if (outputformat==Filters.BITSCORE) {
                runquery.add("6 sseqid bitscore");
            }
            else if (outputformat==Filters.RAWSCORE) {
                runquery.add("6 sseqid score");
            }
            else if (outputformat==Filters.PIDENT) {
                runquery.add("6 sseqid pident");
            }
            else if (outputformat==Filters.COVERAGE) {
                runquery.add("6 sseqid qcovus");
            }
            else if (outputformat==Filters.STANDARD) {
                runquery.add("6");
            }
        }
        
        return (exec(runquery, outputformat, threshold, pthreshold, evalue));
    }
    
    private String exec (List<String> input, Filters format, int threshold, double pthreshold, double evalue) {
        
        String fin_out = "";
        try {
            ProcessBuilder builder = new ProcessBuilder(input);
            final Process process = builder.start();
       
            if (process.getErrorStream().read()!=-1 ){
                fin_out = print("Errors", process.getErrorStream(), format, threshold, pthreshold, evalue);
            }
            else {
                fin_out = print("Output", process.getInputStream(), format, threshold, pthreshold, evalue);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            return fin_out;
        }
    }
    
    private String print (String status, InputStream input, Filters format, int threshold, double pthreshold, double evalue) throws IOException{
        
        BufferedReader in = new BufferedReader(new InputStreamReader(input));
        System.out.println("*************** " + status + " ***************");
        String line = null;
        String fin_out = "";
        
        while ((line = in.readLine())!=null) {
            
            if (format==Filters.SUBSEQ_ID) {
                output.add(line);
            }
            else if (format==Filters.SUBSEQ) {
                output.add(line);
            }
            else if (format==Filters.EVALUE) {
                String[] linesplit = line.split("\t");
                double eval = Double.parseDouble(linesplit[1]);
                if (eval <= evalue)
                    output.add(linesplit[0]);
            }
            else if (format==Filters.BITSCORE) {
                String[] linesplit = line.split("\t");
                int bitscore = Integer.parseInt(linesplit[1]);
                if (bitscore >= threshold)
                    output.add(linesplit[0]);
            }
            else if (format==Filters.RAWSCORE) {
                String[] linesplit = line.split("\t");
                int rawscore = Integer.parseInt(linesplit[1]);
                if (rawscore >= threshold)
                    output.add(linesplit[0]);
            }
            else if (format==Filters.PIDENT) {
                String[] linesplit = line.split("\t");
                double pint = Double.parseDouble(linesplit[1]);
                if (pint >= pthreshold)
                    output.add(linesplit[0]);
            }
            else if (format==Filters.COVERAGE) {
                String[] linesplit = line.split("\t");
                double coverage = Double.parseDouble(linesplit[1]);
                if (coverage >= pthreshold)
                    output.add(linesplit[0]);
            }
            else {
                //System.out.println(line);
                fin_out += line + ";";
            }
        }
        
        if (!output.isEmpty()) {
            
            for (String s : output) {
                //System.out.println(s);
                fin_out += s + ";";
            }
        }
        
        in.close();
        
        return fin_out;
    }
    
    /*private static String reverseComplement(String input) {
        
        char[] inputChars = input.toCharArray();
        char[] outputChars = new char[inputChars.length];
        for (int i=0, j=inputChars.length-1; i<inputChars.length; i++, j--) {
            switch (inputChars[i]) {
                case 'A':
                    outputChars[j] = 'T';
                    break;
                case 'T':
                    outputChars[j] = 'A';
                    break;
                case 'C':
                    outputChars[j] = 'G';
                    break;
                case 'G':
                    outputChars[j] = 'C';
                    break;
                default:
                    break;
            }   
        }
        return new String(outputChars);
    }*/
    public static enum Filters {
        DEFAULT, SUBSEQ_ID, SUBSEQ, EVALUE, BITSCORE, RAWSCORE, PIDENT, COVERAGE, STANDARD
    }
}
