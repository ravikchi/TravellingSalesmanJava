package com.ravi.TravellingSalesman.GeneticAlgorithm.impl;

import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.Utils.DistanceCalculator;
import com.ravi.TravellingSalesman.Utils.GoogleMapsAPI;

import java.util.*;

/**
 * Created by ravik on 08/02/2017.
 */
public class BinaryIndividual implements Individual {
    private StringBuilder chromosome;
    private Map<String, String> chromToPhenotype= new HashMap<String, String>();
    private List<String> phenotype = new ArrayList<String>();
    Map<String, String> phenoToChron = new HashMap<String, String>();
    private int geneSize = 4;
    GoogleMapsAPI api;
    private double fitnessVal = 0.0;

    public BinaryIndividual(String chromosome, int geneSize, Map<String, String> chromToPhenotype) {
        this.chromosome = new StringBuilder();
        this.geneSize = geneSize;
        this.chromToPhenotype = chromToPhenotype;
        this.chromosome.append(chromosome);
    }

    public BinaryIndividual(List<String> phenotype,Map<String, String> phenoToChron) {
        this.phenotype = phenotype;
        this.phenoToChron = phenoToChron;
    }

    public BinaryIndividual(List<String> phenotype, GoogleMapsAPI api) {
        this.phenotype = phenotype;
        this.api= api;
    }

    public String getGenotype() {
        if(chromosome == null) {
            chromosome = new StringBuilder();
            for (String gene : phenotype) {
                chromosome.append(phenoToChron.get(gene));
            }
        }
        return chromosome.toString();
    }

    public boolean isAlive(){
        Set<String> stringSet = new HashSet<String>();
        for(String ele : (List<String>) getPhenotype()){
            stringSet.add(ele);
        }

        if(stringSet.size() < ((List<String>) getPhenotype()).size()){
            return false;
        }else{
            return true;
        }
    }

    public Object getPhenotype() {
        if(phenotype.isEmpty()){
            int i=1;
            StringBuilder gene = new StringBuilder();
            for(char c: chromosome.toString().toCharArray()){
                gene.append(c);
                if(i%geneSize == 0){
                    String phenoEle = chromToPhenotype.get(gene.toString());
                    phenotype.add(phenoEle);
                    gene.setLength(0);
                }
                i++;
            }
        }
        return phenotype;
    }

    public double fitness() {
        String previousEle = null;
            double totalDistance = 0.0;
            for (String ele : (List<String>) getPhenotype()) {
                if (previousEle != null) {
                    String[] ele1 = previousEle.split(",");
                    String[] ele2 = ele.split(",");
                    totalDistance = totalDistance + DistanceCalculator.distFrom(Float.parseFloat(ele1[0]), Float.parseFloat(ele1[1]), Float.parseFloat(ele2[0])
                            , Float.parseFloat(ele2[1])) / 1000;
                }
                previousEle = ele;
            }
        this.fitnessVal = 1 - totalDistance;

        return this.fitnessVal;
    }
}
