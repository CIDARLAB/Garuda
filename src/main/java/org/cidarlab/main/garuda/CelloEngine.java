package org.cidarlab.main.garuda;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.cli.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.cidarlab.celloadapter.*;
import org.json.simple.parser.*;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by krishna on 3/16/17.
 */
public class CelloEngine {

    private static List<String> verilogfiles;
    private static String stagingpath = "/Users/krishna/CIDAR/CelloStaging";
    private static String ucfpath = "/Users/krishna/CIDAR/CelloStaging/ucf.json";

    /**
     *
     * @return
     */
    private static Options createCommandLineOptions() {
        final Options options = new Options();
        options.addOption("i", "init", true, "Give the initialization (*.ini) file, 3D initalization (*_3d.ini) file.");
        options.addOption("f", "format", true, "Specify output format (j - json, e - eps, s - svg) (eg. esj, je, sj, etc.");
        options.addOption("o", "out", true, "Specify the output directory, Defaults to the output folder");
        options.addOption("h", "help", false, "Show help information.");
        options.addOption("d", "debug", false, "This enables all the debug printing");
        options.addOption("c", "cello", false, "This enables Cello Mode of operation");
        options.addOption("u", "user", true, "Give the username");
        options.addOption("p", "pass", true, "Give the password");
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
        //first find all the verilog files

        //Process all the agrs
        cl.getArgList();
    }



    public static void main(String[] args) throws IOException, InterruptedException, org.json.simple.parser.ParseException {

        verilogfiles = new ArrayList<>();

        final Options options = createCommandLineOptions();

        CommandLineParser parser = new DefaultParser();

        String username="babygaruda",password="potter";

        Cello cellosomething = new Cello(username, password);

        String[] extensions = new String[] { "v" };



        Collection<File> files = FileUtils.listFiles(new File(stagingpath), extensions, true );

        //Loop through each of the files and then
        File ucffile = new File(ucfpath);
        String ucftext = FileUtils.readFileToString(ucffile);
        UCF ucf = new UCF(ucffile.getName(), ucftext);
        Job j =  new Job();

        j.addInput("pTac", 0.0034 ,2.8,"AACGATCGTTGGCTGTGTTGACAATTAATCATCGGCTCGTATAATGTGTGGAATTGTGAGCGCTCACAATT");
        j.addInput("pTet",0.0013,4.4,"TACTCCACCGTTGGCTTTTTTCCCTATCAGTGATAGAGATTGACATCCCTATCAGTGATAGAGATAATGAGCAC");
        j.addInput("pLuxStar",0.025,0.31,"ATAGCTTCTTACCGGACCTGTAGGATCGTACAGGTTTACGCAAGAAAATGGTTTGTTACTTTCGAATAAA");
        j.addInput("pBAD",0.0082,2.5,"ACTTTTCATACTCCCGCCATTCAGAGAAGAAACCAATTGTCCATATTGCATCAGACATTGCCGTCACTGCGTCTTTTACTGGCTCTTCTCGCTAACCAAACCGGTAACCCCGCTTATTAAAAGCATTCTGTAACAAAGCGGGACCAAAGCCATGACAAAAACGCGTAACAAAAGTGTCTATAATCACGGCAGAAAAGTCCACATTGATTATTTGCACGGCGTCACACTTTGCTATGCCATAGCATTTTTATCCATAAGATTAGCGGATCCTACCTGACGCTTTTTATCGCAACTCTCTACTGTTTCTCCATACCCGTTTTTTTGGGCTAGC");

        j.addOutput("YFP","CTGAAGCTGTCACCGGATGTGCTTTCCGGTCTGATGAGTCCGTGAGGACGAAACAGCCTCTACAAATAATTTTGTTTAATACTAGAGAAAGAGGGGAAATACTAGATGGTGAGCAAGGGCGAGGAGCTGTTCACCGGGGTGGTGCCCATCCTGGTCGAGCTGGACGGCGACGTAAACGGCCACAAGTTCAGCGTGTCCGGCGAGGGCGAGGGCGATGCCACCTACGGCAAGCTGACCCTGAAGTTCATCTGCACCACAGGCAAGCTGCCCGTGCCCTGGCCCACCCTCGTGACCACCTTCGGCTACGGCCTGCAATGCTTCGCCCGCTACCCCGACCACATGAAGCTGCACGACTTCTTCAAGTCCGCCATGCCCGAAGGCTACGTCCAGGAGCGCACCATCTTCTTCAAGGACGACGGCAACTACAAGACCCGCGCCGAGGTGAAGTTCGAGGGCGACACCCTGGTGAACCGCATCGAGCTGAAGGGCATCGACTTCAAGGAGGACGGCAACATCCTGGGGCACAAGCTGGAGTACAACTACAACAGCCACAACGTCTATATCATGGCCGACAAGCAGAAGAACGGCATCAAGGTGAACTTCAAGATCCGCCACAACATCGAGGACGGCAGCGTGCAGCTCGCCGACCACTACCAGCAGAACACCCCAATCGGCGACGGCCCCGTGCTGCTGCCCGACAACCACTACCTTAGCTACCAGTCCGCCCTGAGCAAAGACCCCAACGAGAAGCGCGATCACATGGTCCTGCTGGAGTTCGTGACCGCCGCCGGGATCACTCTCGGCATGGACGAGCTGTACAAGTAACTCGGTACCAAATTCCAGAAAAGAGGCCTCCCGAAAGGGGGGCCTTTTTTCGTTTTGGTCC");
        j.addOutput("RFP","CTGAAGTGGTCGTGATCTGAAACTCGATCACCTGATGAGCTCAAGGCAGAGCGAAACCACCTCTACAAATAATTTTGTTTAATACTAGAGTCACACAGGAAAGTACTAGATGGCTTCCTCCGAAGACGTTATCAAAGAGTTCATGCGTTTCAAAGTTCGTATGGAAGGTTCCGTTAACGGTCACGAGTTCGAAATCGAAGGTGAAGGTGAAGGTCGTCCGTACGAAGGTACCCAGACCGCTAAACTGAAAGTTACCAAAGGTGGTCCGCTGCCGTTCGCTTGGGACATCCTGTCCCCGCAGTTCCAGTACGGTTCCAAAGCTTACGTTAAACACCCGGCTGACATCCCGGACTACCTGAAACTGTCCTTCCCGGAAGGTTTCAAATGGGAACGTGTTATGAACTTCGAAGACGGTGGTGTTGTTACCGTTACCCAGGACTCCTCCCTGCAAGACGGTGAGTTCATCTACAAAGTTAAACTGCGTGGTACCAACTTCCCGTCCGACGGTCCGGTTATGCAGAAAAAAACCATGGGTTGGGAAGCTTCCACCGAACGTATGTACCCGGAAGACGGTGCTCTGAAAGGTGAAATCAAAATGCGTCTGAAACTGAAAGACGGTGGTCACTACGACGCTGAAGTTAAAACCACCTACATGGCTAAAAAACCGGTTCAGCTGCCGGGTGCTTACAAAACCGACATCAAACTGGACATCACCTCCCACAACGAAGACTACACCATCGTTGAACAGTACGAACGTGCTGAAGGTCGTCACTCCACCGGTGCTTAATAACAGATAAAAAAAATCCTTAGCTTTCGCTAAGGATGATTTCT");
        j.addOutput("BFP","CTGAAGTTCCAGTCGAGACCTGAAGTGGGTTTCCTGATGAGGCTGTGGAGAGAGCGAAAGCTTTACTCCCGCACAAGCCGAAACTGGAACCTCTACAAATAATTTTGTTTAAGAGTCACACAGGAAAGTACTAGATGAGCGAGCTGATTAAGGAGAACATGCACATGAAGCTGTACATGGAGGGCACCGTGGACAACCATCACTTCAAGTGCACATCCGAGGGCGAAGGCAAGCCCTACGAGGGCACCCAGACCATGAGAATCAAGGTGGTCGAGGGCGGCCCTCTCCCCTTCGCCTTCGACATCCTGGCTACTAGCTTCCTCTACGGCAGCAAGACCTTCATCAACCACACCCAGGGCATCCCCGACTTCTTCAAGCAGTCCTTCCCTGAGGGCTTCACATGGGAGAGAGTCACCACATACGAAGATGGGGGCGTGCTGACCGCTACCCAGGACACCAGCCTCCAGGACGGCTGCCTCATCTACAACGTCAAGATCAGAGGGGTGAACTTCACATCCAACGGCCCTGTGATGCAGAAGAAAACACTCGGCTGGGAGGCCTTCACCGAGACGCTGTACCCCGCTGACGGCGGCCTGGAAGGCAGAAACGACATGGCCCTGAAGCTCGTGGGCGGGAGCCATCTGATCGCAAACATCAAGACCACATATAGATCCAAGAAACCCGCTAAGAACCTCAAGATGCCTGGCGTCTACTATGTGGACTACAGACTGGAAAGAATCAAGGAGGCCAACAACGAGACCTACGTCGAGCAGCACGAGGTGGCAGTGGCCAGATACTGCGACCTCCCTAGCAAACTGGGGCACTAACCAGGCATCAAATAAAACGAAAGGCTCAGTCGAAAGACTGGGCCTTTCGTTTTATCTGTTGTTTGTCGGTGAACGCTCTCTACTAGAGTCACACTGGCTCACCTTCGGGTGGGCCTTTCTGCGTTTATA");

        for(File file : files){

            String verilogstring = FileUtils.readFileToString(file);
            String justfilename = FilenameUtils.removeExtension(file.getName());
            j.setJobID("bg"+justfilename);
            j.addFlag(Job.JobOptions.HILL_CLIMBING);
            //j.addFlag(Job.JobOptions.NO_FIGURES);

            j.setVerilog(verilogstring);
            System.out.println(file.getName());

            //Dispatch the job
            try {
                cellosomething.submitJob(j);
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            //Sleep for a little while
            TimeUnit.HOURS.sleep(1);

            //Serialize the results for the python process
            try {
                GarudaResults garudaResults = cellosomething.getGarudaResults(j.getJobID());
            } catch (UnirestException e) {
                e.printStackTrace();
            }

            System.exit(5000);
        }


    }
}
