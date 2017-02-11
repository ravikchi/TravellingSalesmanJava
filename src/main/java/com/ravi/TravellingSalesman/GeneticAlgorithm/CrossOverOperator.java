package com.ravi.TravellingSalesman.GeneticAlgorithm;

/**
 * Created by ravik on 11/02/2017.
 */
public interface CrossOverOperator {
    public Individual crossOver(Individual parent1, Individual parent2);
}
