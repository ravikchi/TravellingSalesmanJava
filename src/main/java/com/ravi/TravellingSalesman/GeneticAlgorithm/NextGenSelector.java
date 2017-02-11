package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public interface NextGenSelector {
    public List<Individual> getNextGenPopulation(List<Individual> parentGen, List<Individual> childGen);
}
