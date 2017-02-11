package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public interface Individual {
    public double getFitness();
    public String getChromosome();
    public List<Object> getPhenoType();
    public int geneSize();
    public Converter getConverter();
}
