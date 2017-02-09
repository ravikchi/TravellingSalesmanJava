package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 08/02/2017.
 */
public interface BinaryGA {
    public String crossover(Individual parent1, Individual parent2);
    public String mutate(Individual parent);
}
