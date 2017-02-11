package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.CrossOver;

import com.ravi.TravellingSalesman.GeneticAlgorithm.CrossOverOperator;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.Converters.HeuristicConverter;
import com.ravi.TravellingSalesman.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by ravik on 11/02/2017.
 */
public class HeuristicCrossOver implements CrossOverOperator {
    public Individual crossOver(Individual parent1, Individual parent2) {
        List<String> chromosome1 = getChromosome(parent1.getChromosome(), parent1.geneSize());
        List<String> chromosome2 = getChromosome(parent2.getChromosome(), parent2.geneSize());

        HeuristicConverter converter = null;
        if(parent1.getConverter() instanceof HeuristicConverter) {
            converter = (HeuristicConverter) parent1.getConverter();
        }

        Set<String> geneSet = new HashSet<String>();

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
                int index = RandomUtils.randomIntWithRange(0, geneList.size()-1);
                String nextGene = geneList.get(index);
                child.add(nextGene);
                geneSet.add(nextGene);
                geneList.remove(index);

            }else {
                String previousGene = child.get(i - 1);
                String nextGene;
                if(converter == null){
                    if(RandomUtils.randomDouble() < 0.5){
                        nextGene = gene1;
                    }else{
                        nextGene = gene2;
                    }
                }else {
                    if (converter.getDistance(previousGene, gene1) < converter.getDistance(previousGene, gene2)){
                        nextGene = gene1;
                    }else{
                        nextGene = gene2;
                    }
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

        return parent1.getConverter().getIndividual(result.toString());
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
}
