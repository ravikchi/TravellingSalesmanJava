package com.ravi.TravellingSalesman.GeneticAlgorithm;

import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.BinaryIndividual;

import java.util.*;

/**
 * Created by ravik on 08/02/2017.
 */
public class Population {

    private Map<String, String> chroToPheno = new HashMap<String, String>();
    private Map<String, String> phenoToChron = new HashMap<String, String>();
    private BinaryGA ga;
    private List<Individual> population = new ArrayList<Individual>();
    private int size = 10;
    private int geneSize = 4;

    public Population(Map<String, String> chroToPheno, Map<String, String> phenoToChron, BinaryGA ga, int geneSize) {
        this.chroToPheno = chroToPheno;
        this.phenoToChron = phenoToChron;
        this.ga = ga;
        this.geneSize = geneSize;
    }

    public Population(Map<String, String> chroToPheno, Map<String, String> phenoToChron, BinaryGA ga, List<Individual> population, int geneSize) {
        this.chroToPheno = chroToPheno;
        this.phenoToChron = phenoToChron;
        this.ga = ga;
        this.population = population;
        this.geneSize = geneSize;
    }

    public Individual crossOver(){
        Individual child = null;
        while(true) {
            int first = randomWithRange(0, size-1);
            int second = randomWithRange(0, size-1);
            String offSpring = ga.crossover(population.get(first).getGenotype(), population.get(second).getGenotype());
            //System.out.println(offSpring);

            child = new BinaryIndividual(offSpring, geneSize, chroToPheno);
            if(child.isAlive()) {
                //System.out.println(child.fitness());
                break;
            }
        }

        return child;
    }

    public Individual mutate(){
        Individual child = null;
        while(true){
            int first = randomWithRange(0, size-1);
            String offSpring = ga.mutate(population.get(first).getGenotype());
            //System.out.println(offSpring);

            child = new BinaryIndividual(offSpring, geneSize, chroToPheno);
            if(child.isAlive()) {
                //System.out.println(child.fitness());
                break;
            }
        }

        return child;
    }

    public static List<Individual> initialPopulation(Individual seed, Map<String, String> phenoToChron, int size){
        List<Individual> initPopulation = new ArrayList<Individual>();
        List<String> pheno = null;
        Individual individual = null;
        for(int i=0; i<size; i++){
            List<String> phenotype = (List<String>) seed.getPhenotype();
            Collections.shuffle(phenotype);

            pheno = new ArrayList<String>();
            pheno.addAll(phenotype);

            individual = new BinaryIndividual(pheno, phenoToChron);

            initPopulation.add(individual);
        }

        return initPopulation;
    }

    public void sortPopulation(){
        Collections.sort(this.population, new Comparator<Individual>(){
            public int compare(Individual o1, Individual o2){
                if(o1.fitness() == o2.fitness())
                    return 0;
                return o1.fitness() > o2.fitness() ? -1 : 1;
            }
        });
    }

    public void printStatistics(){
        Individual best = population.get(0);
        Individual worst = population.get(0);
        double mean = 0.0;
        double totalFitness = 0.0;
        for(Individual individual : population){
            totalFitness = totalFitness + individual.fitness();
            if(individual.fitness() > best.fitness()){
                best = individual;
            }

            if(individual.fitness() < worst.fitness()){
                worst = individual;
            }
        }

        mean = totalFitness/size;

        System.out.println("The Best Individual has fitness :"+best.fitness());
        System.out.println("The Worst Individual has fitness :"+worst.fitness());
        System.out.println("The Average Fitness of population :"+mean);
    }

    public Population getNextGeneration(){
        this.sortPopulation();

        List<Individual> nextGenerationIndividuals = new ArrayList<Individual>();

        while(true){
            if(nextGenerationIndividuals.size() == size){
                break;
            }
            double random = Math.random();

            if(random < 0.5){
                nextGenerationIndividuals.add(crossOver());
            }else if(random < 0.75){
                nextGenerationIndividuals.add(mutate());
            }else{
                int index = 0;
                int first = randomWithRange(0, size-1);
                int second = randomWithRange(0, size-1);

                if(population.get(first).fitness() > population.get(second).fitness()) {
                    nextGenerationIndividuals.add(population.get(first));
                }else{
                    nextGenerationIndividuals.add(population.get(second));
                }
            }
        }

        Population nextGen = new Population(chroToPheno, phenoToChron, ga, nextGenerationIndividuals, geneSize);
        nextGen.sortPopulation();
        nextGen.replaceWorst(population.get(0));

        return nextGen;
    }

    public void replaceWorst(Individual individual){
        sortPopulation();
        population.add(population.size()-1,individual);
    }

    public void printPopulation(){
        for(Individual i : population){
            System.out.println(i.getGenotype());
            System.out.println(i.fitness());
        }
    }

    private int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }

    public Individual getBest(){
        sortPopulation();
        return population.get(0);
    }
}
