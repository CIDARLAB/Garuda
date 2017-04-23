package org.cidarlab.main.garuda.CelloAnalysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Models a Mathematical Boolean Function implemented in a living organism
 * Created by Manuel Gimenez on 4/23/17.
 */
public class BioBooleanFunction {
    int domain_dimension;
    int codomain_dimension;
    private HashMap<String, List<Float>> input_values_map;
    private HashMap<String, List<Float>> expected_output_values_map;
    private HashMap<String,List<Float>> real_output_values_map;

    /**
     *
     * @param input_values_map Map specifying theoretical input values of the boolean function (keys are names of a
     *                         input proteins, values should be a list of 1s and 0s).
     * @param expected_output_values_map Map specifying theoretical output values of the boolean function (keys are
     *                                   names of output proteins, values should be a list of 1s and 0s).
     * @param real_output_values_map Map specifying empirical (wetlab) output expression levels of the biocircuit (keys
     *                               are names of output proteins, values should be expression).
     */
    public BioBooleanFunction(HashMap<String, List<Float>> input_values_map, HashMap<String, List<Float>> expected_output_values_map, HashMap<String,List<Float>> real_output_values_map) {
        // TODO: Sanity check to verify everything is what should be.
        // All maps should have the same keySet.
        // When looking at all the i-th position of the lists in the maps, that describes an input or output
        this.domain_dimension = input_values_map.keySet().size();
        this.codomain_dimension = expected_output_values_map.keySet().size();

        this.input_values_map = input_values_map;
        this.expected_output_values_map = expected_output_values_map;
        this.real_output_values_map = real_output_values_map;
    }

    /**
     * Calculates the value of the correctness metric for this bioBooleanFunction
     * @return Value of the correctness metric
     */
    public Float get_correctness(){
        // Build vectors (as lists).
        List<Float> expected_list = new ArrayList<Float>();
        List<Float> real_list = new ArrayList<Float>();
        for(String aKey : this.expected_output_values_map.keySet()){
            // I'm assuming all the maps have the same Keyset.
            expected_list.addAll(this.expected_output_values_map.get(aKey));
            real_list.addAll(this.real_output_values_map.get(aKey));
        }
        // Create float vectors
        float[] expected_array = new float[expected_list.size()];
        expected_array = expected_list.toArray(expected_array);
        float[] real_array = new float[real_list.size()];
        real_array = real_list.toArray(real_array);
        // TODO: Normalize vectors before calculation?
        // Compute angle
        double angle = BioBooleanFunction.cosineSimilarity(expected_array, real_array);
        return (Float) angle;

    }

    /**
     * Computes the cosine similarity between two vectors
     * @param vectorA
     * @param vectorB
     * @return Cosine similarity value
     */
    private static double cosineSimilarity(float[] vectorA, float[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
