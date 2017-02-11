package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.MutationOpe;

import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.MutationOperator;
import com.ravi.TravellingSalesman.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class HeuristicMutation implements MutationOperator {
    public Individual mutate(Individual parent) {
        List<String> chromosome = getChromosome(parent.getChromosome(), parent.geneSize());

        int first = RandomUtils.randomIntWithRange(0, chromosome.size() - 1);
        int second = RandomUtils.randomIntWithRange(0, chromosome.size() - 1);

        String gene1 = chromosome.get(first);
        String gene2 = chromosome.get(second);

        chromosome.remove(first);
        chromosome.add(first, gene2);
        chromosome.remove(second);
        chromosome.add(second, gene1);

        StringBuilder result = new StringBuilder();
        for (String ele : chromosome) {
            result.append(ele);
        }


        return parent.getConverter().getIndividual(result.toString());
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
