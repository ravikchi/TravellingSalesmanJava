package com.ravi.TravellingSalesman.GeneticAlgorithm.impl.Selectors;

import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.SelectionOperator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class RouletteWheelSelection implements SelectionOperator{
    private List<Individual> population;
    private List<Double> rouletteWheel = new ArrayList<Double>();
    private double totalFitness = -1.0;

    private double totalFitness(){
        for (Individual individual : population) {
            totalFitness = totalFitness + individual.getFitness();
        }

        return totalFitness;
    }

    private int roulettWheelAlgo(){
        if(rouletteWheel.isEmpty()){
            double totalFit = totalFitness();
            double curFitness = 0.0;
            for(int i=0; i<population.size();i++) {
                double fitness = population.get(i).getFitness()/totalFit;
                curFitness = curFitness + fitness;
                rouletteWheel.add(curFitness);
            }
        }

        int index = 0;

        double random = Math.random();
        for(int i=0; i<rouletteWheel.size();i++){
            if(random < rouletteWheel.get(i)){
                index = i;
                break;
            }
        }

        return index;
    }

    public Individual selection(List<Individual> matingPool) {
        population = matingPool;

        int first = roulettWheelAlgo();

        return population.get(first);
    }
}
