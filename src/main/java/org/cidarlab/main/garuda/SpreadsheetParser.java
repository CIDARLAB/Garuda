/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.garuda;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.cidarlab.main.dom.Distribution;
import org.cidarlab.main.ml.ExpectationMaximization;
import org.cidarlab.main.ml.GaussianMembership;

/**
 *
 * @author mardian
 */
public class SpreadsheetParser {

    private int cnum;
    private int pnum;
    private int size;
    private int toxic;
    private int participant;
    private double[][] data;

    List<Integer> tlist;    //list of randomly picked random genes
    List<Integer> ntlist;

    //private int[] tracker;
    private List<Integer> healthy_for_sure;

    public SpreadsheetParser(String inputUrl, int cnum, int pnum, int size, int toxic, double healthy_mean, double healthy_sd, double toxic_mean, double toxic_sd) {

        this.cnum = cnum;
        this.pnum = pnum;
        this.size = size;
        //this.threshold = threshold;
        this.toxic = toxic;
        this.participant = 0;
        this.data = new double[cnum][size + 2];
        //this.tracker = new int[cnum];

        init(inputUrl, healthy_mean, healthy_sd, toxic_mean, toxic_sd);
    }

    private void init(String inputUrl, double healthy_mean, double healthy_sd, double toxic_mean, double toxic_sd) {

        generateToxic();

        int[] partCount = new int[pnum];
        //double[][] value = new double[data.length][2];

        /////temporary lines start here
        double[] em_val = new double[data.length];
        /*    double[] em_val = new double[] {
            0.832929805,
            0.390600905,
            0.114650134,
            0.28567905,
            1.103741955,
            0.65547295,
            0.175233945,
            0.018946372,
            -0.012313338,
            0.889151427,
        };*/
        double[] mu = new double[]{
            0.0,
            1.0
        };
        double[] sigma = new double[]{
            0.1,
            0.1
        };
        /////temporary lines end here

        try {
            FileInputStream inputFile = new FileInputStream(inputUrl);
            XSSFWorkbook workbook = new XSSFWorkbook(inputFile);
            XSSFSheet sheet = workbook.getSheetAt(2);   //take data from construct sheet

            Random rand1 = new Random();    //healthy parts
            Random rand2 = new Random();    //toxic parts

            ExpectationMaximization em = new ExpectationMaximization(mu, sigma, true);  //create 2 gaussian mixture with uniform priors

            for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {

                Row row = sheet.getRow(i);

                int size = (int) row.getCell(5).getNumericCellValue();
                //randomized growth rate
                data[i - 1][1] = rand1.nextGaussian() * healthy_sd + healthy_mean;     //find out how gaussian random works

                double flag = 1.0;  //for healthy

                for (int j = 0; j < size; j++) {

                    int part = (int) row.getCell(j + 6).getNumericCellValue();
                    data[i - 1][j + 2] = part;

                    partCount[part - 1]++;  //count if parts occured

                    if (tlist.contains(part)) {

                        data[i - 1][1] = rand2.nextGaussian() * toxic_sd + toxic_mean;

                        flag = 0.0; //for toxic
                    }
                }

                em_val[i - 1] = data[i - 1][1];
                String flagstring = (flag == 0.0) ? "toxic" : "healthy";
                System.out.println(data[i - 1][1] + "\t" + flagstring);
            }

            ///// EM Part ///
            //healthy_for_sure = GaussianMembership.test(value);
            int em_sim = -1;
            do {
                em_sim = em.iterate(em_val);    //loop until EM terminated with expected criteria
                //System.out.println(em_sim);
            } while (em_sim != 1);

            //Distribution[] em_dist = em.getDistribution();
            Distribution[] em_dist = em.getInitDistribution();
            
            for (int t = 0; t < em_dist.length; t++) {
                System.out.println("++ mu: " + em_dist[t].getMu() + " sigma: " + em_dist[t].getSigma());
            }
            int[] em_cluster = em.getFinalCluster();
            ///// EM Part ///
            
            ///// Parts inspection ///
            Set<Double> em_healthy = new HashSet<Double>();
            for (int x = 0; x < em_cluster.length; x++) {

                if (em_cluster[x] == 1) {
                    for (int y = 0; y < this.size; y++) {
                        em_healthy.add(this.data[x][y + 2]);
                    }
                }
            }
            System.out.println("List of parts that healthy for sure according to EM: ");
            for (double healthy : em_healthy) {
                System.out.println(healthy);
            }
            ///// Parts inspection ///

            //count total participated parts
            for (int i = 0; i < partCount.length; i++) {
                if (partCount[i] != 0) {
                    participant++;
                }
            }

            inputFile.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateToxic() {

        //generate ntlist of numbers from 1 to number of available parts
        ntlist = new ArrayList<>();
        for (int i = 0; i < pnum; i++) {
            ntlist.add(i + 1);
        }

        //randomly pick numbers from the ntlist as toxic genes
        tlist = new ArrayList<>();
        System.out.println("List of toxic genes: ");
        for (int i = 0; i < toxic; i++) {
            tlist.add(ntlist.remove((int) (Math.random() * ntlist.size())));    //is it okay to Math.random?
            System.out.print("- " + tlist.get(i) + " ");
        }
        System.out.println();

        //print remaining list as non-toxic
        System.out.println("List of non-toxic genes: ");
        for (int i = 0; i < ntlist.size(); i++) {
            System.out.print("+ " + ntlist.get(i) + " ");
        }
        System.out.println();

        System.out.println("Total " + toxic + " toxic genes, and " + ntlist.size() + " non-toxic genes");

    }

    //get data array as it is
    public double[][] getData() {

        double[][] datacopy = new double[cnum][size + 2];
        for (int i = 0; i < datacopy.length; i++) {
            for (int j = 0; j < datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j];
            }
        }
        return datacopy;
    }

    //get data array, parts only
    public double[][] getPart() {

        double[][] datacopy = new double[cnum][size];
        for (int i = 0; i < datacopy.length; i++) {
            for (int j = 0; j < datacopy[i].length; j++) {
                datacopy[i][j] = this.data[i][j + 2];
            }
        }
        return datacopy;
    }

    //get growth rate data as 2d array
    public double[][] getGrowth() {

        double[][] datacopy = new double[cnum][1];
        for (int i = 0; i < datacopy.length; i++) {
            datacopy[i][0] = this.data[i][1];
            //System.out.println("** " + datacopy[i][0]);
        }
        return datacopy;
    }

    //get matrix of parts-count
    public double[][] getCount() {

        double[][] datacopy = new double[cnum][pnum];
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 2; j < this.data[0].length; j++) {
                datacopy[i][((int) this.data[i][j] - 1)] = 1;  //should this be a counter instead of hardcoding with 1?
            }
        }
        return datacopy;
    }

    //get transpose matrix of parts (value will be growth rate)
    public double[][] transpose() {

        double[][] datacopy = new double[pnum][cnum];
        for (int i = 0; i < this.data.length; i++) {
            for (int j = 2; j < this.data[0].length; j++) {
                datacopy[(int) this.data[i][j] - 1][i] = this.data[i][1];
            }
        }
        return datacopy;
    }

    //clean the transpose matrix with only existed parts
    public double[][] transposeClean() {

        double[][] dataRaw = this.transpose();
        double[][] cleanData = new double[participant][cnum];

        int x = 0;
        for (int i = 0; i < dataRaw.length; i++) {
            int counter = 0;
            for (int j = 0; j < dataRaw[0].length; j++) {
                if (dataRaw[i][j] != 0.0) {
                    counter++;
                }
            }
            if (counter != 0) {
                //cleanData[x][1] = i+1;  //extra column to store id+1
                for (int j = 0; j < dataRaw[0].length; j++) {
                    cleanData[x][j] = dataRaw[i][j];
                }
                x++;
            }
        }
        return cleanData;
    }

    public int getParticipant() {
        return this.participant;
    }

    public List<Integer> getToxicList() {
        return this.tlist;
    }

    public List<Integer> getNonToxicList() {
        return this.ntlist;
    }

    public void getHealthyPart() {

        Set<Double> healthy_set = new HashSet<Double>();
        double[][] parts = getPart();

        if (healthy_for_sure!=null) {
            for (int i = 0; i < healthy_for_sure.size(); i++) {
                for (int j = 0; j < parts[0].length; j++) {
                    healthy_set.add(parts[healthy_for_sure.get(i)][j]);
                }
            }

            System.out.print("Absolute healthy parts: ");
            for (Double d : healthy_set) {
                System.out.print(d + ", ");
            }
            System.out.println();
        }
        else
            System.out.println("Absolute healthy parts list is null...!");
    }
}
