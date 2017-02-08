package com.ravi.TravellingSalesman.GeneticAlgorithm;

/**
 * Created by ravik on 08/02/2017.
 */
public interface Individual {
    public String getGenotype();
    public Object getPhenotype();
    public double fitness();
    public boolean isAlive();
}
