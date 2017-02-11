package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public interface SelectionOperator {
    public Individual selection(List<Individual> matingPool);
}
