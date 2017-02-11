package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.NextGenSelectors;

import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.NextGenSelector;
import com.ravi.TravellingSalesman.Utils.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class HeuristicNextGenSelector implements NextGenSelector {
    private int populationSize;

    public HeuristicNextGenSelector(int populationSize) {
        this.populationSize = populationSize;
    }

    @Override
    public List<Individual> getNextGenPopulation(List<Individual> parentGen, List<Individual> childGen) {
        List<Individual> population = new ArrayList<Individual>(parentGen);
        population.addAll(childGen);
        sortPopulation(population);

        List<Individual> newPopulation = population.subList(0, populationSize);

        while(true){
            if(newPopulation.size() == populationSize){
                break;
            }else if(newPopulation.size() > populationSize){
                newPopulation.remove(newPopulation.size()-1);
            }else {
                int randon = RandomUtils.randomIntWithRange(0, populationSize - 1);
                newPopulation.add(population.get(randon));
            }
        }

        return newPopulation;
    }

    private void sortPopulation(List<Individual> population){
        Collections.sort(population, new Comparator<Individual>(){
            public int compare(Individual o1, Individual o2){
                if(o1.getFitness() == o2.getFitness())
                    return 0;
                return o1.getFitness() > o2.getFitness() ? -1 : 1;
            }
        });
    }
}
