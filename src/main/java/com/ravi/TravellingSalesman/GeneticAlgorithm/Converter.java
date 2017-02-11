package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public interface Converter {
    public Individual getIndividual(String genoType);
    public Individual getIndividual(List<Object> phenoType);
    public String getGene(String pheno);
    public String getPheno(String gene);
}
