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
    private boolean mutate = true;
    private List<Double> rouletteWheel = new ArrayList<Double>();

    public void setMutate(boolean mutate) {
        this.mutate = mutate;
    }

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
        this.size = population.size();
        this.geneSize = geneSize;
    }

    public Individual crossOver(){
        Individual child = null;
        while(true) {
            Individual[] individuals = selection();
            String offSpring = ga.crossover(individuals[0], individuals[1]);
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
            Individual[] individuals = selection();
            String offSpring = ga.mutate(individuals[0]);
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

            individual = new BinaryIndividual(pheno, phenoToChron, seed.getGeneSize());

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

    private double totalFitness(){
        double totalFitness = 0.0;
        for(Individual individual : population){
            totalFitness = totalFitness + individual.fitness();
        }
        return totalFitness;
    }

    private int roulettWheelAlgo(){
        if(rouletteWheel.isEmpty()){
            double totalFit = totalFitness();
            double curFitness = 0.0;
            for(int i=0; i<population.size();i++) {
                double fitness = population.get(i).fitness()/totalFit;
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

    public Individual[] selection(){

        int first = roulettWheelAlgo();
        int second = roulettWheelAlgo();

        Individual[] individuals = new Individual[2];
        individuals[0] = population.get(first);
        individuals[1] = population.get(second);

        return individuals;
    }

    public Population getNextGeneration(){
        this.sortPopulation();

        List<Individual> nextGenerationIndividuals = new ArrayList<Individual>();

        while(true){
            if(nextGenerationIndividuals.size() == size){
                break;
            }
            double random = Math.random();

            if(random < 0.7){
                nextGenerationIndividuals.add(crossOver());
            }else if(random < 0.95 && mutate){
                nextGenerationIndividuals.add(mutate());
            }else{
                int index = 0;
                Individual[] individuals = selection();
                nextGenerationIndividuals.add(individuals[0]);
            }
        }

        Population nextGen = new Population(chroToPheno, phenoToChron, ga, nextGenerationIndividuals, geneSize);
        nextGen.sortPopulation();
        nextGen.getBestOf2Generations(population);

        return nextGen;
    }

    public void getBestOf2Generations(List<Individual> gen1){
       population.addAll(gen1);
       sortPopulation();
        List<Individual> newPopulation = population.subList(0, size);
        while(true){
            if(newPopulation.size() == size){
                break;
            }else if(newPopulation.size() > size){
                newPopulation.remove(newPopulation.size()-1);
            }else {
                int randon = randomWithRange(0, size - 1);
                newPopulation.add(population.get(randon));
            }
        }

        population = newPopulation;
    }

    public void getBestOf2Generationsv2(List<Individual> gen1){
        List<Individual> oldChild = new ArrayList<>();
        oldChild.addAll(population);
        population.addAll(gen1);
        sortPopulation();
        int newSize = new Double(size*0.1).intValue();
        List<Individual> newPopulation = population.subList(0, newSize);
        newPopulation.addAll(oldChild.subList(0, size-newSize-1));
        while(true){
            if(newPopulation.size() == size){
                break;
            }else if(newPopulation.size() > size){
                newPopulation.remove(newPopulation.size()-1);
            }

            int randon = randomWithRange(0, size-1);
            newPopulation.add(oldChild.get(randon));
        }

        population = newPopulation;
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
