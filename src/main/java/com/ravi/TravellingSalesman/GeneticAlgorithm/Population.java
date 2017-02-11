package com.ravi.TravellingSalesman.GeneticAlgorithm;

import com.ravi.TravellingSalesman.GeneticAlgorithm.Exceptions.GAException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class Population {
    private GAOperators operators;
    private NextGenSelector nextGenSelector;
    private List<Individual> population = new ArrayList<Individual>();
    private int n;

    public Population(GAOperators operators, NextGenSelector nextGenOperator) {
        this.operators = operators;
        this.nextGenSelector = nextGenOperator;
    }

    public List<Individual> getPopulation() {
        return population;
    }

    public void setPopulation(List<Individual> population) {
        this.population = population;
        Collections.sort(this.population, new Comparator<Individual>(){
            public int compare(Individual o1, Individual o2){
                if(o1.getFitness() == o2.getFitness())
                    return 0;
                return o1.getFitness() > o2.getFitness() ? -1 : 1;
            }
        });
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<Individual> nextGeneration() throws GAException {
        if(population.isEmpty()){
            throw new GAException("Population is Empty");
        }
        List<Individual> nextGen = new ArrayList<Individual>();
        while(nextGen.size() < n){
            nextGen.add(operators.produceChild(population));
        }

        return nextGenSelector.getNextGenPopulation(population, nextGen);
    }

    public void printStatistics(){
        Individual best = population.get(0);
        Individual worst = population.get(0);
        double mean = 0.0;
        double totalFitness = 0.0;
        for(Individual individual : population){
            totalFitness = totalFitness + individual.getFitness();
            if(individual.getFitness() > best.getFitness()){
                best = individual;
            }

            if(individual.getFitness() < worst.getFitness()){
                worst = individual;
            }
        }

        mean = totalFitness/n;

        System.out.println("The Best Individual has fitness :"+best.getFitness());
        System.out.println("The Worst Individual has fitness :"+worst.getFitness());
        System.out.println("The Average Fitness of population :"+mean);
    }
}
