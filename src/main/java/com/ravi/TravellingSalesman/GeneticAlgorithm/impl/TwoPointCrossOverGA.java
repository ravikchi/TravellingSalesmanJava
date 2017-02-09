package com.ravi.TravellingSalesman.GeneticAlgorithm.impl;

import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;

/**
 * Created by ravik on 08/02/2017.
 */
public class TwoPointCrossOverGA implements BinaryGA {

    private double mutationRate = 0.15;

    public TwoPointCrossOverGA(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public String crossover(Individual parent1, Individual parent2) {
        int crossOverPoint1 = randomWithRange(0, parent1.getGenotype().length());
        int crossOverPoint2 = randomWithRange(crossOverPoint1, parent1.getGenotype().length());

        StringBuilder offspring = new StringBuilder();
        offspring.append(parent1.getGenotype().substring(0, crossOverPoint1));
        offspring.append(parent2.getGenotype().substring(crossOverPoint1, crossOverPoint2));
        offspring.append(parent1.getGenotype().substring(crossOverPoint2, parent1.getGenotype().length()));
        return offspring.toString();
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public String mutate(Individual parent) {
        double random = 0.0;
        char[] offspring = new char[parent.getGenotype().length()];

        for(int i=0; i<parent.getGenotype().length(); i++) {
            random = Math.random();
            if (random < mutationRate) {

                if (parent.getGenotype().charAt(i) == '0')
                    offspring[i] = '1';
                else
                    offspring[i] = '0';

            } else {
                offspring[i] = parent.getGenotype().charAt(i);
            }
        }

        return String.valueOf(offspring);
    }
}
