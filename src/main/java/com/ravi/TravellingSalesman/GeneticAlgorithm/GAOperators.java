package com.ravi.TravellingSalesman.GeneticAlgorithm;

import java.util.List;

/**
 * Created by ravik on 11/02/2017.
 */
public class GAOperators {
    private CrossOverOperator crossOverOperator;
    private MutationOperator mutationOperator;
    private SelectionOperator selectionOperator;
    private double crossOverRate=0.6;
    private double mutationRate=0.25;

    public GAOperators(CrossOverOperator crossOverOperator, MutationOperator mutationOperator, SelectionOperator selectionOperator) {
        this.crossOverOperator = crossOverOperator;
        this.mutationOperator = mutationOperator;
        this.selectionOperator = selectionOperator;
    }

    public double getCrossOverRate() {
        return crossOverRate;
    }

    public void setCrossOverRate(double crossOverRate) {
        this.crossOverRate = crossOverRate;
    }

    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public Individual produceChild(List<Individual> matingPool){
        double random = Math.random();

        Individual parent1 = selectionOperator.selection(matingPool);
        Individual parent2 = selectionOperator.selection(matingPool);

        Individual offSpring = null;
        if(random <= crossOverRate){
            offSpring = crossOverOperator.crossOver(parent1, parent2);
        }else if(random <= mutationRate+crossOverRate){
            offSpring = mutationOperator.mutate(fitterParent(parent1, parent2));
        }else{
            offSpring = fitterParent(parent1, parent2);
        }

        return offSpring;
    }

    private Individual fitterParent(Individual parent1, Individual parent2){
        if(parent1.getFitness() < parent2.getFitness()){
            return parent2;
        }else{
            return parent1;
        }
    }
}
