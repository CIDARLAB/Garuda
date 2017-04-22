package org.cidarlab.main.garuda.CelloAnalysis;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.cidarlab.celloadapter.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by krishna on 3/16/17.
 */
public class CelloEngine {

    private static List<String> verilogfiles;
    private static String stagingpath = "/Users/krishna/CIDAR/CelloStaging";
    private static String ucfpath = "/Users/krishna/CIDAR/CelloStaging/ucf.json";
    private static String username = "babygaruda";
    private static String password = "potter";
    private static String inputcsvpath = "";
    private static String outputcsvpath = "";
    private static HashMap<String, GarudaResults> garudaResults = new HashMap<>();
    private static boolean COMPUTE_FLAG = false;

    private static Job defaultcellojob = new Job();

    /**
     *
     * @return
     */
    private static Options createCommandLineOptions() {
        final Options options = new Options();
        options.addOption("c", "compute", false, "Computes the genetic circuit for the given file(s)");
        options.addOption("i", "inputs", true, "Specify the inputs csv");
        options.addOption("o", "outputs", true, "Specify the outputs csv");
        options.addOption("u", "user", true, "Give the Cello username");
        options.addOption("p", "pass", true, "Give the Cello password");
        options.addOption("x", "ucf", true, "Give the path for the ucf file" );
        return options;
    }

    /**
     *
     * @param options
     * <p>
     * <p>
     * <p>
     */
    private static void outputCommandLineHelp(final Options options) {
        final HelpFormatter formater = new HelpFormatter();
        formater.printHelp("babygaruda blah blah blah", options);
        System.exit(0);
    }

    /**
     *
     * @param cl
     * @param options
     * <p>
     * @throws IllegalArgumentException
     */
    private static void processCommandline(final CommandLine cl, Options options) throws IllegalArgumentException {

        if (null == cl) {
            return;
        }

        if (cl.hasOption("inputs")) {
            //outputFormat = cl.getOptionValue("out").toLowerCase();
            inputcsvpath = cl.getOptionValue("inputs");
            if (null == stagingpath) {
                System.exit(666);
            }
        } else {
            //Set the detfault input values for the job
            setDefaultJobInputs();
        }

        if (cl.hasOption("outputs")) {
            //outputFormat = cl.getOptionValue("out").toLowerCase();
            outputcsvpath = cl.getOptionValue("outputs");
            if (null == stagingpath) {
                System.exit(666);
            }
        } else {
            //Set the default output values for the job
            setDefaultJobOutputs();

        }

        if (cl.hasOption("compute")) {
            COMPUTE_FLAG = true;
        }

        //Process all the agrs
        stagingpath = cl.getArgList().get(0);
    }



    public static void main(String[] args) throws IOException, InterruptedException, org.json.simple.parser.ParseException {

        verilogfiles = new ArrayList<>();

        final Options options = createCommandLineOptions();

        CommandLineParser parser = new DefaultParser();
        CommandLine cl = null;
        try {
            cl = parser.parse(options, args);
            processCommandline(cl, options);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Cello celloapiclient = new Cello(username, password);

        File stagingfilepath = new File(stagingpath);

        Collection<File> files;
        String[] extensions = new String[] { "v" };

        //Check if this is a verilog file or a directory
        if(stagingfilepath.isDirectory()){
            files = FileUtils.listFiles(new File(stagingpath), extensions, true );
        } else {
             files = new ArrayList<>();
            files.add(stagingfilepath);
        }


        File ucffile = new File(ucfpath);
        String ucftext = FileUtils.readFileToString(ucffile);
        UCF ucf = new UCF(ucffile.getName(), ucftext);

        List<String> ucfGateNames = ucf.getGateNames();


        //Loop through each of the files and then

        for(File file : files){

            String verilogstring = FileUtils.readFileToString(file);
            String justfilename = FilenameUtils.removeExtension(file.getName());
            defaultcellojob.setJobID("bg"+justfilename);
            defaultcellojob.addFlag(Job.JobOptions.BREADTH_FIRST);
            //defaultcellojob.addFlag(Job.JobOptions.NO_FIGURES);

            defaultcellojob.setVerilog(verilogstring);
            System.out.println(file.getName());

            //Dispatch the job
            if(COMPUTE_FLAG) {
                try {
                    celloapiclient.submitJob(defaultcellojob);
                } catch (UnirestException e) {
                    e.printStackTrace();
                }

                //Sleep for a little while
                TimeUnit.MINUTES.sleep(2);
            }

            //Serialize the results for the python process
            try {
                System.out.println("Printing the output");
                GarudaResults garudaResult = celloapiclient.getGarudaResults(defaultcellojob.getJobID());
                garudaResults.put(defaultcellojob.getJobID(), garudaResult);

                System.out.println("Finished adding the output to the results.");

            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }


        //Generate CSV for figuring out if the gate is present in a circuit or not
        //First loop though all the garuda results
        List<String> circuitgates;

        ArrayList<Boolean> rowdata;
        File outputcsv = new File("output.csv");
        String outputstring = "Circuit/Gate";

        for(String gate : ucfGateNames){
            outputstring+= ", " + gate;
        }

        outputstring += "\n";

        for(String key : garudaResults.keySet()){

            GarudaResults garudaResult = garudaResults.get(key);

            //Then extract the netlist (list)
            circuitgates = garudaResult.gateNetList.getGates();

            //Put the name of the circuit
            outputstring += key;

            //Compare fill out 0's/1's based on if the part is present or not.
            for(String gate : ucfGateNames){
                if(circuitgates.contains(gate)){
                    outputstring += ", " + "1";
                } else {
                    outputstring += ", " + "0";
                }
            }

            outputstring += "\n";
        }

        FileUtils.writeStringToFile(outputcsv,outputstring);

    }

    /*
    Private Helper Methods
     */

    private static void setDefaultJobOutputs() {
        defaultcellojob.addOutput("YFP","CTGAAGCTGTCACCGGATGTGCTTTCCGGTCTGATGAGTCCGTGAGGACGAAACAGCCTCTACAAATAATTTTGTTTAATACTAGAGAAAGAGGGGAAATACTAGATGGTGAGCAAGGGCGAGGAGCTGTTCACCGGGGTGGTGCCCATCCTGGTCGAGCTGGACGGCGACGTAAACGGCCACAAGTTCAGCGTGTCCGGCGAGGGCGAGGGCGATGCCACCTACGGCAAGCTGACCCTGAAGTTCATCTGCACCACAGGCAAGCTGCCCGTGCCCTGGCCCACCCTCGTGACCACCTTCGGCTACGGCCTGCAATGCTTCGCCCGCTACCCCGACCACATGAAGCTGCACGACTTCTTCAAGTCCGCCATGCCCGAAGGCTACGTCCAGGAGCGCACCATCTTCTTCAAGGACGACGGCAACTACAAGACCCGCGCCGAGGTGAAGTTCGAGGGCGACACCCTGGTGAACCGCATCGAGCTGAAGGGCATCGACTTCAAGGAGGACGGCAACATCCTGGGGCACAAGCTGGAGTACAACTACAACAGCCACAACGTCTATATCATGGCCGACAAGCAGAAGAACGGCATCAAGGTGAACTTCAAGATCCGCCACAACATCGAGGACGGCAGCGTGCAGCTCGCCGACCACTACCAGCAGAACACCCCAATCGGCGACGGCCCCGTGCTGCTGCCCGACAACCACTACCTTAGCTACCAGTCCGCCCTGAGCAAAGACCCCAACGAGAAGCGCGATCACATGGTCCTGCTGGAGTTCGTGACCGCCGCCGGGATCACTCTCGGCATGGACGAGCTGTACAAGTAACTCGGTACCAAATTCCAGAAAAGAGGCCTCCCGAAAGGGGGGCCTTTTTTCGTTTTGGTCC");
        defaultcellojob.addOutput("RFP","CTGAAGTGGTCGTGATCTGAAACTCGATCACCTGATGAGCTCAAGGCAGAGCGAAACCACCTCTACAAATAATTTTGTTTAATACTAGAGTCACACAGGAAAGTACTAGATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAACAGATAAAAAAAATCCTTAGCTTTCGCTAAGGATGATTTCT");
        defaultcellojob.addOutput("BFP","CTGAAGTTCCAGTCGAGACCTGAAGTGGGTTTCCTGATGAGGCTGTGGAGAGAGCGAAAGCTTTACTCCCGCACAAGCCGAAACTGGAACCTCTACAAATAATTTTGTTTAAGAGTCACACAGGAAAGTACTAGATGAGCGAGCTGATTAAGGAGAACATGCACATGAAGCTGTACATGGAGGGCACCGTGGACAACCATCACTTCAAGTGCACATCCGAGGGCGAAGGCAAGCCCTACGAGGGCACCCAGACCATGAGAATCAAGGTGGTCGAGGGCGGCCCTCTCCCCTTCGCCTTCGACATCCTGGCTACTAGCTTCCTCTACGGCAGCAAGACCTTCATCAACCACACCCAGGGCATCCCCGACTTCTTCAAGCAGTCCTTCCCTGAGGGCTTCACATGGGAGAGAGTCACCACATACGAAGATGGGGGCGTGCTGACCGCTACCCAGGACACCAGCCTCCAGGACGGCTGCCTCATCTACAACGTCAAGATCAGAGGGGTGAACTTCACATCCAACGGCCCTGTGATGCAGAAGAAAACACTCGGCTGGGAGGCCTTCACCGAGACGCTGTACCCCGCTGACGGCGGCCTGGAAGGCAGAAACGACATGGCCCTGAAGCTCGTGGGCGGGAGCCATCTGATCGCAAACATCAAGACCACATATAGATCCAAGAAACCCGCTAAGAACCTCAAGATGCCTGGCGTCTACTATGTGGACTACAGACTGGAAAGAATCAAGGAGGCCAACAACGAGACCTACGTCGAGCAGCACGAGGTGGCAGTGGCCAGATACTGCGACCTCCCTAGCAAACTGGGGCACTAACCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATA");
    }

    private static void setDefaultJobInputs() {
        defaultcellojob.addInput("pTac", 0.0034 ,2.8,"AACGATCGTTGGCTGTGTTGACAATTAATCATCGGCTCGTATAATGTGTGGAATTGTGAGCGCTCACAATT");
        defaultcellojob.addInput("pTet",0.0013,4.4,"TACTCCACCGTTGGCTTTTTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATAATGAGCAC");
        defaultcellojob.addInput("pLuxStar",0.025,0.31,"ATAGCTTCTTACCGGACCTGTAGGATCGTACAGGTTTACGCAAGAAAATGGTTTGTTACTTTCGAATAAA");
        defaultcellojob.addInput("pBAD",0.0082,2.5,"ACTTTTCATACTCCCGCCATTCAGAGAAGAAACCAATTGTCCATATTGCATCAGACATTGCCGTCACTGCGTCTTTTACTGGCTCTTCTCGCTAACCAAACCGGTAACCCCGCTTATTAAAAGCATTCTGTAACAAAGCGGGACCAAAGCCATGACAAAAACGCGTAACAAAAGTGTCTATAATCACGGCAGAAAAGTCCACATTGATTATTTGCACGGCGTCACACTTTGCTATGCCATAGCATTTTTATCCATAAGATTAGCGGATCCTACCTGACGCTTTTTATCGCAACTCTCTACTGTTTCTCCATACCCGTTTTTTTGGGCTAGC");
    }
}
