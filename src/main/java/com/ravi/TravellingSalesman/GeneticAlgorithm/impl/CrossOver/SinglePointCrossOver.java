package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.CrossOver;

import com.ravi.TravellingSalesman.GeneticAlgorithm.CrossOverOperator;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.Utils.RandomUtils;

/**
 * Created by ravik on 11/02/2017.
 */
public class SinglePointCrossOver implements CrossOverOperator{

    public Individual crossOver(Individual parent1, Individual parent2) {
        int crossOverPoint = RandomUtils.randomIntWithRange(0, parent1.getChromosome().length());

        String offspring = parent1.getChromosome().substring(0, crossOverPoint) + parent2.getChromosome().substring(crossOverPoint, parent2.getChromosome().length());
        return parent1.getConverter().getIndividual(offspring);
    }
}
