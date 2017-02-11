package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.CrossOver;

import com.ravi.TravellingSalesman.GeneticAlgorithm.CrossOverOperator;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.Utils.RandomUtils;

/**
 * Created by ravik on 11/02/2017.
 */
public class TwoPointCrossOver implements CrossOverOperator {

    public Individual crossOver(Individual parent1, Individual parent2) {
        String chromosome1 = parent1.getChromosome();
        String chromosome2 = parent2.getChromosome();


        int crossOverPoint1 = RandomUtils.randomIntWithRange(0, chromosome1.length());
        int crossOverPoint2 = RandomUtils.randomIntWithRange(crossOverPoint1, chromosome1.length());

        StringBuilder offspring = new StringBuilder();
        offspring.append(chromosome1.substring(0, crossOverPoint1));
        offspring.append(chromosome2.substring(crossOverPoint1, crossOverPoint2));
        offspring.append(chromosome1.substring(crossOverPoint2, chromosome1.length()));

        return parent1.getConverter().getIndividual(offspring.toString());
    }
}
