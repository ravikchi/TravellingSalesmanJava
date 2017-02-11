package com.ravi.TravellingSalesman;

import com.ravi.GenericGA.GeneticAlgorithm.*;
import com.ravi.GenericGA.GeneticAlgorithm.Exceptions.GAException;
import com.ravi.GenericGA.GeneticAlgorithm.impl.MutationOpe.HeuristicMutation;
import com.ravi.GenericGA.GeneticAlgorithm.impl.NextGenSelectors.ElitistNextGen;
import com.ravi.GenericGA.GeneticAlgorithm.impl.Selectors.RouletteWheelSelection;
import com.ravi.TravellingSalesman.GeneticAlgorithm.elements.HeuristicConverter;
import com.ravi.TravellingSalesman.GeneticAlgorithm.elements.HeuristicCrossOver;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by ravik on 11/02/2017.
 */
public class CitiesGA {
    private String json;
    private int size;

    public static void main( String[] args )
    {
        CrossOverOperator crossOverOperator = new HeuristicCrossOver();
        MutationOperator mutationOperator = new HeuristicMutation();
        SelectionOperator selectionOperator = new RouletteWheelSelection();

        GAOperators gaOperators = new GAOperators(crossOverOperator, mutationOperator, selectionOperator);

        CitiesGA salesman = new CitiesGA();
        String json = salesman.getJson("cities.json");
        int size = 50;

        salesman.setJson(json);
        salesman.setSize(size);

        String binary = Integer.toBinaryString(size);
        int geneSize = binary.length();
        List<String> wayPoints = salesman.getWaypoints(json, size);

        Map<String, String> chroToPheno = salesman.getChromoToPhenoMap(wayPoints, geneSize);
        Map<String, String> phenoToChron = salesman.getPhenoToChromo(wayPoints, geneSize);

        Converter converter = new HeuristicConverter(chroToPheno, phenoToChron, geneSize);

        Individual individual = converter.getIndividual(new ArrayList<Object>(wayPoints));

        int generationSize = 10;

        //NextGenSelector nextGenSelector = new HeuristicNextGenSelector(generationSize);
        NextGenSelector nextGenSelector = new ElitistNextGen();

        Population population = new Population(gaOperators, nextGenSelector);
        population.setN(generationSize);
        population.setPopulation(salesman.initialPopulation(individual, generationSize));

        for(int i=0; i<1000; i++){
            try {
                System.out.println("Generation Number : "+i);
                List<Individual> nextGen = population.nextGeneration();
                population.setPopulation(nextGen);

                population.printStatistics();

                System.out.println();
                System.out.println();
                System.out.println();
                System.out.println();
            } catch (GAException e) {
                e.printStackTrace();
                System.out.println(e.getMessage());
                break;
            }
        }

        wayPoints.clear();

        List<Object> phenoType = population.getPopulation().get(0).getPhenoType();
        for(Object obj : phenoType){
            wayPoints.add((String) obj);
        }

        Map<String, JSONObject> wayPointsMap = salesman.getWaypointsMap(json, size);
        for(String str : wayPoints){
            JSONObject obj = wayPointsMap.get(str);
            System.out.println("lat,long : "+str+ " ; city :"+obj.getString("city")+" ; state :"+obj.getString("state"));
        }
        System.out.println(population.getPopulation().get(0).getFitness());

        /*GeoApiContext context = new GeoApiContext().setApiKey(Constants.googleDirectionsAPIKey);
        GoogleMapsAPI api = new GoogleMapsAPI(context);

        DirectionsResult result = api.directionsFromWaypoints(api.getWaypoints(wayPoints));
        System.out.println(api.getDistance(result));*/

    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<Individual> initialPopulation(Individual seed,int populationSize){
        List<Individual> initPopulation = new ArrayList<Individual>();
        while(initPopulation.size() < populationSize){
            List<Object> phenoType = new ArrayList<Object>();
            phenoType.addAll(seed.getPhenoType());

            Collections.shuffle(phenoType);

            initPopulation.add(seed.getConverter().getIndividual(phenoType));
        }

        return initPopulation;
    }

    public Map<String, JSONObject> getWaypointsMap(String json, int size){
        JSONArray arr = new JSONArray(json);
        Map<String, JSONObject> wayPointsMap = new HashMap<>();
        int count =1;
        for(Object obj : arr){
            if(count > size){
                break;
            }
            JSONObject jsonObject = (JSONObject) obj;
            String lattitude = jsonObject.get("latitude")+"";
            String longitude = jsonObject.get("longitude")+"";

            wayPointsMap.put(lattitude+","+longitude, jsonObject);

            count++;
        }

        return wayPointsMap;
    }

    public Map<String, String> getPhenoToChromo(List<String> wayPoints, int geneSize){
        Map<String, String> phenoToChron = new HashMap<String, String>();
        for(int i=0; i<wayPoints.size(); i++){
            int key = Integer.parseInt(Integer.toBinaryString(i));

            phenoToChron.put(wayPoints.get(i),String.format("%0"+geneSize+"d", key));
        }
        return phenoToChron;
    }

    public Map<String, String> getChromoToPhenoMap(List<String> wayPoints, int geneSize){
        Map<String, String> chroToPheno = new HashMap<String, String>();
        for(int i=0; i<wayPoints.size(); i++){
            int key = Integer.parseInt(Integer.toBinaryString(i));
            chroToPheno.put(String.format("%0"+geneSize+"d", key), wayPoints.get(i));
        }

        return chroToPheno;
    }

    public List<String> getWaypoints(String json, int size){
        JSONArray arr = new JSONArray(json);
        List<String> wayPoints = new ArrayList<String>();
        int count =1;
        for(Object obj : arr){
            if(count > size){
                break;
            }
            JSONObject jsonObject = (JSONObject) obj;
            String lattitude = jsonObject.get("latitude")+"";
            String longitude = jsonObject.get("longitude")+"";

            wayPoints.add(lattitude+","+longitude);

            count++;
        }

        return wayPoints;
    }

    public String getJson(String fileName){
        StringBuilder result = new StringBuilder("");

        //Get file from resources folder
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());

        try (Scanner scanner = new Scanner(file)) {

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }

            scanner.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return result.toString();

    }
}
