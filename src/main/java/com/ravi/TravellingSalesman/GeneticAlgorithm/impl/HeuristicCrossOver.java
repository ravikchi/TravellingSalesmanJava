package com.ravi.TravellingSalesman.GeneticAlgorithm.impl;

import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.Utils.DistanceCalculator;

import java.util.*;

/**
 * Created by ravik on 09/02/2017.
 */
public class HeuristicCrossOver implements BinaryGA {
    private double mutationRate;

    public HeuristicCrossOver(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    @Override
    public String crossover(Individual parent1, Individual parent2) {

        List<String> chromosome1 = getChromosome(parent1.getGenotype(), parent1.getGeneSize());
        List<String> chromosome2 = getChromosome(parent2.getGenotype(), parent2.getGeneSize());

        Map<String, String> chronoToPheno = parent1.chronToPheno();

        Set<String> geneSet = new HashSet<>();

        List<String> geneList = new ArrayList<String>();
        geneList.addAll(chromosome1);

        List<String> child = new ArrayList<String>();

        double random = Math.random();
        String gene;
        if(random<0.5){
            gene = chromosome1.get(0);
        }else{
            gene = chromosome2.get(0);
        }

        child.add(gene);
        geneSet.add(gene);
        geneList.remove(gene);

        for(int i=1; i<chromosome1.size(); i++){
            String gene1 = chromosome1.get(i);
            String gene2 = chromosome2.get(i);

            if(geneSet.contains(gene1) || geneSet.contains(gene2)){
                int index = randomWithRange(0, geneList.size()-1);
                String nextGene = geneList.get(index);
                child.add(nextGene);
                geneSet.add(nextGene);
                geneList.remove(index);

            }else {
                String previousGene = child.get(i - 1);
                String nextGene;
                if (getDistance(previousGene, gene1, chronoToPheno) < getDistance(previousGene, gene2, chronoToPheno)) {
                    nextGene = gene1;
                }else{
                    nextGene = gene2;
                }
                child.add(nextGene);
                geneSet.add(nextGene);
                geneList.remove(nextGene);
            }
        }

        StringBuilder result = new StringBuilder();
        for(String ele : child){
            result.append(ele);
        }

        return result.toString();
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    private double getDistance(String gene1, String gene2, Map<String, String> chronoToPheno){
        String[] ele1 = chronoToPheno.get(gene1).split(",");
        String[] ele2 = chronoToPheno.get(gene2).split(",");
        return DistanceCalculator.distFrom(Float.parseFloat(ele1[0]), Float.parseFloat(ele1[1]), Float.parseFloat(ele2[0])
                , Float.parseFloat(ele2[1])) / 1000;
    }

    private List<String> getChromosome(String genotype, int geneSize){
        StringBuilder gene = new StringBuilder();
        List<String> chromosome = new ArrayList<String>();
        int i=1;
        for(char c: genotype.toString().toCharArray()){
            gene.append(c);
            if(i%geneSize == 0){
                chromosome.add(gene.toString());
                gene.setLength(0);
            }
            i++;
        }
        return chromosome;
    }

    @Override
    public String mutate(Individual parent) {
        List<String> chromosome = getChromosome(parent.getGenotype(), parent.getGeneSize());

        String genotype = parent.getGenotype();
        /*for(int i=0; i<chromosome.size();i++) {
            if (Math.random() < mutationRate) {*/
                int first = randomWithRange(0, chromosome.size() - 1);
                int second = randomWithRange(0, chromosome.size() - 1);

                String gene1 = chromosome.get(first);
                String gene2 = chromosome.get(second);

                chromosome.remove(first);
                chromosome.add(first, gene2);
                chromosome.remove(second);
                chromosome.add(second, gene1);
/*
            }
        }*/

        StringBuilder result = new StringBuilder();
        for (String ele : chromosome) {
            result.append(ele);
        }

        genotype = result.toString();

        return genotype;
    }
}
