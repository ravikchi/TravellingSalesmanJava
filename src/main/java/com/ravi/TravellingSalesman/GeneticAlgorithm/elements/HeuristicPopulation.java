package com.ravi.TravellingSalesman.GeneticAlgorithm.elements;

import com.ravi.GenericGA.GeneticAlgorithm.GAOperators;
import com.ravi.GenericGA.GeneticAlgorithm.Individual;
import com.ravi.GenericGA.GeneticAlgorithm.NextGenSelector;
import com.ravi.GenericGA.GeneticAlgorithm.Population;
/**
 * Created by ravik on 11/02/2017.
 */
public class HeuristicPopulation extends Population {

    public HeuristicPopulation(GAOperators operators, NextGenSelector nextGenOperator) {
        super(operators, nextGenOperator);
    }

    @Override
    public void printStatistics() {
        Individual best = super.getPopulation().get(0);
        Individual worst = super.getPopulation().get(0);
        double mean = 0.0;
        double totalFitness = 0.0;
        for(Individual individual : super.getPopulation()){
            totalFitness = totalFitness + individual.getFitness();
            if(individual.getFitness() > best.getFitness()){
                best = individual;
            }

            if(individual.getFitness() < worst.getFitness()){
                worst = individual;
            }
        }

        mean = totalFitness/super.getN();

        System.out.println("The Best Individual has fitness :"+best.getFitness());
        System.out.println("The Worst Individual has fitness :"+worst.getFitness());
        System.out.println("The Average Fitness of population :"+mean);
    }
}
