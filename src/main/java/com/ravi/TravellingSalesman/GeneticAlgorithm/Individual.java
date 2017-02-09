package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.Map;

/**
 * Created by ravik on 08/02/2017.
 */
public interface Individual {
    public String getGenotype();
    public Object getPhenotype();
    public int getGeneSize();
    public double fitness();
    public boolean isAlive();
    public Map<String, String> chronToPheno();
    public Map<String, String> phenoToChron();

}
