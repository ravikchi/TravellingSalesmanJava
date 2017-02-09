package com.ravi.TravellingSalesman.GeneticAlgorithm.impl;

import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;

/**
 * Created by ravik on 08/02/2017.
 */
public class TwoPointCrossOverGA implements BinaryGA {

    private double mutationRate = 0.15;

    public TwoPointCrossOverGA(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public String crossover(String parent1, String parent2) {
        int crossOverPoint1 = randomWithRange(0, parent1.length());
        int crossOverPoint2 = randomWithRange(crossOverPoint1, parent1.length());

        StringBuilder offspring = new StringBuilder();
        offspring.append(parent1.substring(0, crossOverPoint1));
        offspring.append(parent2.substring(crossOverPoint1, crossOverPoint2));
        offspring.append(parent1.substring(crossOverPoint2, parent1.length()));
        return offspring.toString();
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public String mutate(String parent) {
        double random = 0.0;
        char[] offspring = new char[parent.length()];

        for(int i=0; i<parent.length(); i++) {
            random = Math.random();
            if (random < mutationRate) {

                if (parent.charAt(i) == '0')
                    offspring[i] = '1';
                else
                    offspring[i] = '0';

            } else {
                offspring[i] = parent.charAt(i);
            }
        }

        return String.valueOf(offspring);
    }
}
