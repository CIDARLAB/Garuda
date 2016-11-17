/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cidarlab.main.ml;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.cidarlab.main.dom.Data;

/**
 *
 * @author mardian
 */
public class Backpropagation {
    
    private int INPUT_NEURONS;
    private final int HIDDEN_NEURONS = 3;
    private int OUTPUT_NEURONS;

    private final double LEARN_RATE = 0.2;    // Rho.
    //private final double NOISE_FACTOR = 0.45;
    private final int TRAINING_REPS = 100000;

    // Input to Hidden Weights (with Biases).
    private double wih[][];

    // Hidden to Output Weights (with Biases).
    private double who[][];

    // Activations.
    private double inputs[];
    private double hidden[];
    private double target[];
    private double actual[];

    // Unit errors.
    private double erro[];
    private double errh[];

    private final int MAX_SAMPLES = 300;
    
    private List<Data> clusterData;
    
    private double[][] rawData;
    private int numOfTrain;
    private double expThreshold;
    
    public Backpropagation (double[][] rawData, int cluster) {
        
        INPUT_NEURONS = rawData[0].length;
        OUTPUT_NEURONS = cluster;
        
        clusterData = new ArrayList<Data>();
        
        wih = new double[INPUT_NEURONS + 1][HIDDEN_NEURONS];
        who = new double[HIDDEN_NEURONS + 1][OUTPUT_NEURONS];
        
        inputs = new double[INPUT_NEURONS];
        hidden = new double[HIDDEN_NEURONS];
        target = new double[OUTPUT_NEURONS];
        actual = new double[OUTPUT_NEURONS];
        
        erro = new double[OUTPUT_NEURONS];
        errh = new double[HIDDEN_NEURONS];
        
        this.rawData = rawData;
        this.numOfTrain = 300;
        this.expThreshold = 0.5;
        
        neuralNetwork();
    }

    public void neuralNetwork () {
        
        int sample = 0;
        
        double[][] trainData = makeTrainData (numOfTrain);
        double[][] trainOut = makeTrainOut (trainData);

        assignRandomWeights();

        // Train the network.
        for(int epoch=0; epoch<TRAINING_REPS; epoch++)
        {
            sample += 1;
            if(sample==MAX_SAMPLES){
                sample = 0;
            }

            for(int i=0; i<INPUT_NEURONS; i++)
            {
                inputs[i] = trainData[sample][i];
            } // i

            for(int i=0; i<OUTPUT_NEURONS; i++)
            {
                target[i] = trainOut[sample][i];
            } // i

            feedForward();

            backPropagate();

        } // epoch

        //getTrainingStats();
        
        testNetworkTraining();
		
        //System.out.println("\nTest network against noisy input:");
        //testNetworkWithNoise1();
    }

    private double[][] makeTrainData (int row) {
        
        int column = rawData[0].length;
        double[][] trainData = new double[row][column];
        
        List<Integer> list = new ArrayList<>();
        for (int i=0; i<rawData.length; i++) {
            list.add(i);
        }
        
        List<Integer> newlist = new ArrayList<>();
        for (int i=0; i<row; i++) {
            newlist.add (list.remove((int)(Math.random()*list.size())));
        }
        
        //System.out.println("List of training data:");
        for (int i=0; i<trainData.length; i++) {
            for (int j=0; j<trainData[0].length; j++) {
                trainData[i][j] = rawData[newlist.get(i)][j];
                //System.out.println((newlist.get(i)+1) + "     " + trainData[i][j]);
            }
        }
        
        return trainData;
    }
 
    private double[][] makeTrainOut (double[][] inputData) {
        
        int row = inputData.length;
        int column = 2;
        double[][] trainOut = new double[row][column];
        
        //System.out.println("List of training data:");
        for (int i=0; i<inputData.length; i++) {
            for (int j=0; j<inputData[0].length; j++) {
                if (inputData[i][j]<expThreshold) {
                    trainOut[i][0] = 1;
                }
                else {
                    trainOut[i][1] = 1;
                }
            }
            //System.out.println(inputData[i][0] + "  " + trainOut[i][0] + "  " + trainOut[i][1]);
        }
        
        return trainOut;
    }
     
/*    private void getTrainingStats()
    {
        double sum = 0.0;
        for(int i=0; i<MAX_SAMPLES; i++)
        {
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                inputs[j] = trainInputs[i][j];
            } // j

            for(int j = 0; j < OUTPUT_NEURONS; j++)
            {
                target[j] = trainOutput[i][j];
            } // j

            feedForward();

            if(maximum(actual) == maximum(target)){
                sum += 1;
            }else{
                System.out.println(inputs[0] + "\t" + inputs[1] + "\t" + inputs[2] + "\t" + inputs[3]);
              System.out.println(maximum(actual) + "\t" + maximum(target));
            }
        } // i

        System.out.println("Network is " + ((double)sum / (double)MAX_SAMPLES * 100.0) + "% correct.");

        return;
    }*/

    private void testNetworkTraining()
    {
        // This function simply tests the training vectors against network.
        for(int i=0; i<rawData.length; i++)
        {
            for(int j=0; j<INPUT_NEURONS; j++)
            {
                inputs[j] = rawData[i][j];
            } // j
            
            feedForward();
            
        /*    for(int j=0; j<INPUT_NEURONS; j++)
            {
                System.out.print(inputs[j] + "\t");
            } // j
        */    
            /*for(int j = 0; j < OUTPUT_NEURONS; j++)
            {
                System.out.print("Output: " + actual[j] + "\t");
            }*/
        //    System.out.println(maximum(actual));
            
            clusterData.add(new Data(new double[]{inputs[0]}, i, maximum(actual)));
        } // i
        
        return;
    }

/*    private void testNetworkWithNoise1()
    {
        // This function adds a random fractional value to all the training
        // inputs greater than zero.
        DecimalFormat dfm = new java.text.DecimalFormat("###0.0");
        
        for(int i = 0; i < MAX_SAMPLES; i++)
        {
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                inputs[j] = trainInputs[i][j] + (new Random().nextDouble() * NOISE_FACTOR);
            } // j
            
            feedForward();
            
            for(int j = 0; j < INPUT_NEURONS; j++)
            {
                System.out.print(dfm.format(((inputs[j] * 1000.0) / 1000.0)) + "\t");
            } // j
            System.out.print("Output: " + maximum(actual) + "\n");
        } // i

        return;
    }*/

    private int maximum(final double[] vector)
    {
        // This function returns the index of the maximum of vector().
        int sel = 0;
        double max = vector[sel];

        for(int index = 0; index < OUTPUT_NEURONS; index++)
        {
            if(vector[index] > max){
                max = vector[index];
                sel = index;
            }
        }
        return sel;
    }

    private void feedForward()
    {
        double sum = 0.0;

        // Calculate input to hidden layer.
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            sum = 0.0;
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                sum += inputs[inp] * wih[inp][hid];
            } // inp

            sum += wih[INPUT_NEURONS][hid]; // Add in bias.
            hidden[hid] = sigmoid(sum);
        } // hid

        // Calculate the hidden to output layer.
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            sum = 0.0;
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                sum += hidden[hid] * who[hid][out];
            } // hid

            sum += who[HIDDEN_NEURONS][out]; // Add in bias.
            actual[out] = sigmoid(sum);
        } // out
        return;
    }

    private void backPropagate()
    {
        // Calculate the output layer error (step 3 for output cell).
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            erro[out] = (target[out] - actual[out]) * sigmoidDerivative(actual[out]);
        }

        // Calculate the hidden layer error (step 3 for hidden cell).
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            errh[hid] = 0.0;
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                errh[hid] += erro[out] * who[hid][out];
            }
            errh[hid] *= sigmoidDerivative(hidden[hid]);
        }

        // Update the weights for the output layer (step 4).
        for(int out = 0; out < OUTPUT_NEURONS; out++)
        {
            for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
            {
                who[hid][out] += (LEARN_RATE * erro[out] * hidden[hid]);
            } // hid
            who[HIDDEN_NEURONS][out] += (LEARN_RATE * erro[out]); // Update the bias.
        } // out

        // Update the weights for the hidden layer (step 4).
        for(int hid = 0; hid < HIDDEN_NEURONS; hid++)
        {
            for(int inp = 0; inp < INPUT_NEURONS; inp++)
            {
                wih[inp][hid] += (LEARN_RATE * errh[hid] * inputs[inp]);
            } // inp
            wih[INPUT_NEURONS][hid] += (LEARN_RATE * errh[hid]); // Update the bias.
        } // hid
        return;
    }

    private void assignRandomWeights()
    {
        for(int inp=0; inp<=INPUT_NEURONS; inp++) // Do not subtract 1 here.
        {
            for(int hid=0; hid<HIDDEN_NEURONS; hid++)
            {
                // Assign a random weight value between -0.5 and 0.5
                wih[inp][hid] = new Random().nextDouble() - 0.5;
            } // hid
        } // inp

        for(int hid=0; hid<=HIDDEN_NEURONS; hid++) // Do not subtract 1 here.
        {
            for(int out = 0; out < OUTPUT_NEURONS; out++)
            {
                // Assign a random weight value between -0.5 and 0.5
                who[hid][out] = new Random().nextDouble() - 0.5;
            } // out
        } // hid
    }

    private static double sigmoid(final double val)
    {
        return (1.0 / (1.0 + Math.exp(-val)));
    }

    private static double sigmoidDerivative(final double val)
    {
        return (val * (1.0 - val));
    }
    
    public List<Data> getClusterData () {
        return this.clusterData;
    }

}
