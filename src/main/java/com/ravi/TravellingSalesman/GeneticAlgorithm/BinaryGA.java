package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 08/02/2017.
 */
public interface BinaryGA {
    public String crossover(String parent1, String parent2);
    public String mutate(String parent);
}
