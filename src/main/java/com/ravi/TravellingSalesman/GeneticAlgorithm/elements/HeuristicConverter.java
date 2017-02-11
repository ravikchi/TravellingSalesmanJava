package com.ravi.TravellingSalesman.GeneticAlgorithm.elements;

import com.ravi.GenericGA.GeneticAlgorithm.Converter;
import com.ravi.GenericGA.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.Utils.DistanceCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ravik on 11/02/2017.
 */
public class HeuristicConverter implements Converter {
    private Map<String, String> chromToPhenotype= new HashMap<String, String>();
    private Map<String, String> phenoToChron = new HashMap<String, String>();
    private int geneSize;

    public HeuristicConverter(Map<String, String> chromToPhenotype, Map<String, String> phenoToChron, int geneSize) {
        this.chromToPhenotype = chromToPhenotype;
        this.phenoToChron = phenoToChron;
        this.geneSize = geneSize;
    }

    public Individual getIndividual(String genoType) {
        TSPIndividual individual = new TSPIndividual(genoType, this);
        individual.setGeneSize(geneSize);

        return individual;
    }

    public Individual getIndividual(List<Object> phenoType) {
        TSPIndividual individual = new TSPIndividual(phenoType, this);
        individual.setGeneSize(geneSize);

        return individual;
    }

    public String getGene(String pheno) {
        return phenoToChron.get(pheno);
    }

    public String getPheno(String gene) {
        return chromToPhenotype.get(gene);
    }

    public double getDistance(String gene1, String gene2){
        String[] ele1 = chromToPhenotype.get(gene1).split(",");
        String[] ele2 = chromToPhenotype.get(gene2).split(",");
        return DistanceCalculator.distFrom(Float.parseFloat(ele1[0]), Float.parseFloat(ele1[1]), Float.parseFloat(ele2[0])
                , Float.parseFloat(ele2[1])) / 1000;
    }
}
