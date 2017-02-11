package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.MutationOpe;

import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.MutationOperator;

/**
 * Created by ravik on 11/02/2017.
 */
public class BitWiseMutate implements MutationOperator {
    private double mutationRate;

    public BitWiseMutate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Individual mutate(Individual parent) {
        double random = 0.0;
        char[] offspring = new char[parent.getChromosome().length()];

        for(int i=0; i<parent.getChromosome().length(); i++) {
            random = Math.random();
            if (random < mutationRate) {

                if (parent.getChromosome().charAt(i) == '0')
                    offspring[i] = '1';
                else
                    offspring[i] = '0';

            } else {
                offspring[i] = parent.getChromosome().charAt(i);
            }
        }

        return parent.getConverter().getIndividual(String.valueOf(offspring));
    }
}
