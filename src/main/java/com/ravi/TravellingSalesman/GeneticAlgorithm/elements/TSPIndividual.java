package com.ravi.TravellingSalesman.GeneticAlgorithm.elements;

import com.ravi.GenericGA.GeneticAlgorithm.Converter;
import com.ravi.GenericGA.GeneticAlgorithm.Individual;
import com.ravi.GenericGA.GeneticAlgorithm.Objective;
import com.ravi.TravellingSalesman.Utils.DistanceCalculator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class TSPIndividual implements Individual {
    private double fitness;
    private StringBuilder chromosome;
    private List<Object> phenoType = new ArrayList<Object>();
    private int geneSize;
    private Converter converter;

    public TSPIndividual(List<Object> phenoType, Converter converter) {
        for(Object obj : phenoType){
            this.phenoType.add((String) obj);
        }
        this.converter = converter;
    }

    public void setGeneSize(int geneSize) {
        this.geneSize = geneSize;
    }

    public TSPIndividual(String chromosome, Converter converter) {
        this.chromosome = new StringBuilder(chromosome);
        this.converter = converter;
    }

    public double getFitness() {
        String previousEle = null;
        double totalDistance = 0.0;
        for (Object obj : (List<Object>) getPhenoType()) {
            String ele = (String) obj;
            if (previousEle != null) {
                String[] ele1 = previousEle.split(",");
                String[] ele2 = ele.split(",");
                totalDistance = totalDistance + DistanceCalculator.distFrom(Float.parseFloat(ele1[0]), Float.parseFloat(ele1[1]), Float.parseFloat(ele2[0])
                        , Float.parseFloat(ele2[1])) / 1000;
            }
            previousEle = ele;
        }
        this.fitness = 1 - totalDistance;

        return this.fitness;
    }

    @Override
    public double getFitness(Objective objective) {
        return 0;
    }

    public String getChromosome() {
        if(chromosome == null){
            chromosome = new StringBuilder();
            for (Object pheno : phenoType) {
                chromosome.append(converter.getGene((String) pheno));
            }
        }

        return chromosome.toString();
    }

    public List<Object> getPhenoType() {
        if(phenoType.isEmpty()){
            int i=1;
            StringBuilder gene = new StringBuilder();
            for(char c: chromosome.toString().toCharArray()){
                gene.append(c);
                if(i%geneSize == 0){
                    String phenoEle = converter.getPheno(gene.toString());
                    phenoType.add(phenoEle);
                    gene.setLength(0);
                }
                i++;
            }
        }

        return phenoType;
    }

    public int geneSize() {
        return geneSize;
    }

    public Converter getConverter() {
        return converter;
    }
}
