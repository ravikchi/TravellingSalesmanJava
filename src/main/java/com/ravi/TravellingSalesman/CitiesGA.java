package com.ravi.TravellingSalesman;

import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Population;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.BinaryIndividual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.HeuristicCrossOver;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.SinglePointCrossOverGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.TwoPointCrossOverGA;
import com.ravi.TravellingSalesman.Utils.Constants;
import com.ravi.TravellingSalesman.Utils.GoogleMapsAPI;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by ravik on 08/02/2017.
 */
public class CitiesGA {
    public static void main(String[] args){
        //Get file from resources folder
        CitiesGA salesman = new CitiesGA();
        String json = salesman.getJson("cities.json");
        int size = 50;
        String binary = Integer.toBinaryString(size);
        int geneSize = binary.length();
        List<String> wayPoints = salesman.getWaypoints(json, size);
        /*List<String> wayPoints = new ArrayList<String>();
        wayPoints.add("30.3321838,-81.65565099999999");
        wayPoints.add("35.2270869,-80.8431267");
        wayPoints.add("39.768403,-86.158068");
        wayPoints.add("39.9611755,-82.99879419999999");
        wayPoints.add("39.9525839,-75.1652215");
        wayPoints.add("40.7127837,-74.0059413");
        wayPoints.add("41.8781136,-87.6297982");
        wayPoints.add("32.7766642,-96.79698789999999");
        wayPoints.add("29.7604267,-95.3698028");
        wayPoints.add("30.267153,-97.7430608");
        wayPoints.add("29.4241219,-98.49362819999999");
        wayPoints.add("33.4483771,-112.0740373");
        wayPoints.add("32.715738,-117.1610838");
        wayPoints.add("34.0522342,-118.2436849");
        wayPoints.add("37.3382082,-121.8863286");
        wayPoints.add("37.7749295,-122.4194155");*/

        Map<String, String> chroToPheno = salesman.getChromoToPhenoMap(wayPoints, geneSize);
        Map<String, String> phenoToChron = salesman.getPhenoToChromo(wayPoints, geneSize);

        BinaryGA ga = new HeuristicCrossOver(0.15);

        salesman.runGA(wayPoints, chroToPheno, phenoToChron, ga,geneSize);

    }

    public void runGA(List<String> origWaypoints, Map<String, String> chroToPheno, Map<String, String> phenoToChron , BinaryGA ga, int geneSize){
        List<String> waypoints = new ArrayList<String>();
        waypoints.addAll(origWaypoints);

        Individual individual = new BinaryIndividual(waypoints, phenoToChron, geneSize);
        List<Individual> initPopulation = Population.initialPopulation(individual, phenoToChron, 20);

        Population population = new Population(chroToPheno, phenoToChron, ga, initPopulation, geneSize);
        population.sortPopulation();
        individual = new BinaryIndividual(origWaypoints, phenoToChron,geneSize);
        population.replaceWorst(individual);

        for(int i=0; i<1000; i++){
            System.out.println("Generation :"+i);
            population = population.getNextGeneration();
            //population.printPopulation();
            population.printStatistics();

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }

        Individual bestIn = population.getBest();
        for(String str : (List<String>) bestIn.getPhenotype()){
            System.out.println(str);
        }
        System.out.println(bestIn.fitness());


        GeoApiContext context = new GeoApiContext().setApiKey(Constants.googleDirectionsAPIKey);
        GoogleMapsAPI api = new GoogleMapsAPI(context);

        DirectionsResult result = api.directionsFromWaypoints(api.getWaypoints((List<String>) bestIn.getPhenotype()));
        System.out.println(api.getDistance(result));
    }

    public Map<String, String> getPhenoToChromo(List<String> wayPoints, int geneSize){
        Map<String, String> phenoToChron = new HashMap<String, String>();
        for(int i=0; i<wayPoints.size(); i++){
            int key = Integer.parseInt(Integer.toBinaryString(i));
            String value = wayPoints.get(i);

            //System.out.println(value + ", " + String.format("%04d", key));

            String format = "%0"+geneSize+"d";

            phenoToChron.put(value,String.format(format, key));
        }
        return phenoToChron;
    }

    public Map<String, String> getChromoToPhenoMap(List<String> wayPoints, int geneSize){
        Map<String, String> chroToPheno = new HashMap<String, String>();
        for(int i=0; i<wayPoints.size(); i++){
            int key = Integer.parseInt(Integer.toBinaryString(i));
            String value = wayPoints.get(i);

            //System.out.println(String.format("%04d", key) + ", " + value);
            String format = "%0"+geneSize+"d";

            chroToPheno.put(String.format(format, key), value);
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
