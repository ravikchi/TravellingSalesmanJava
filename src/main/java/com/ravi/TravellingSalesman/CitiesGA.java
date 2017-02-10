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
    private String json;
    private int size;

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

    public static void main(String[] args){
        //Get file from resources folder
        CitiesGA salesman = new CitiesGA();
        String json = salesman.getJson("cities.json");
        int size = 50;

        salesman.setJson(json);
        salesman.setSize(size);
        String binary = Integer.toBinaryString(size);
        int geneSize = binary.length();
        //List<String> wayPoints = salesman.getWaypoints(json, size);
        List<String> wayPoints = new ArrayList<String>();
        wayPoints.add("35.2270869,-80.8431267");
        wayPoints.add("35.7795897,-78.6381787");
        wayPoints.add("36.8529263,-75.9779849");
        wayPoints.add("42.3600825,-71.0588801");
        wayPoints.add("40.7127837,-74.0059413");
        wayPoints.add("39.9525839,-75.1652215");
        wayPoints.add("35.1495343,-90.0489801");
        wayPoints.add("29.7604267,-95.3698028");
        wayPoints.add("30.267153,-97.7430608 ");
        wayPoints.add("29.4241219,-98.4936281");
        wayPoints.add("38.8338816,-104.821363");
        wayPoints.add("39.7392358,-104.990251");
        wayPoints.add("32.7554883,-97.3307658");
        wayPoints.add("32.735687,-97.10806559");
        wayPoints.add("32.7766642,-96.7969878");
        wayPoints.add("35.4675602,-97.5164276");
        wayPoints.add("36.1539816,-95.9927750");
        wayPoints.add("37.688889,-97.336111");
        wayPoints.add("47.6062095,-122.332070");
        wayPoints.add("45.5230622,-122.676481");
        wayPoints.add("38.5815719,-121.494399");
        wayPoints.add("37.8043637,-122.271113");
        wayPoints.add("37.7749295,-122.419415");
        wayPoints.add("37.3382082,-121.886328");
        wayPoints.add("36.7468422,-119.772586");
        wayPoints.add("36.1699412,-115.139829");
        wayPoints.add("33.4483771,-112.074037");
        wayPoints.add("33.4151843,-111.831472");
        wayPoints.add("31.7775757,-106.442455");
        wayPoints.add("32.2217429,-110.926479");
        wayPoints.add("32.715738,-117.1610838");
        wayPoints.add("33.7700504,-118.193739");
        wayPoints.add("34.0522342,-118.243684");
        wayPoints.add("35.0853336,-106.605553");
        wayPoints.add("39.0997265,-94.5785667");
        wayPoints.add("41.2523634,-95.9979882");
        wayPoints.add("44.977753,-93.2650108 ");
        wayPoints.add("43.0389025,-87.9064736");
        wayPoints.add("41.8781136,-87.6297982");
        wayPoints.add("42.331427,-83.0457538 ");
        wayPoints.add("41.49932,-81.6943605");
        wayPoints.add("39.2903848,-76.6121893");
        wayPoints.add("38.9071923,-77.0368707");
        wayPoints.add("39.9611755,-82.9987941");
        wayPoints.add("39.768403,-86.158068");
        wayPoints.add("38.2526647,-85.7584557");
        wayPoints.add("36.1626638,-86.7816016");
        wayPoints.add("33.7489954,-84.3879824");
        wayPoints.add("30.3321838,-81.6556509");
        wayPoints.add("25.7616798,-80.1917902");

        Map<String, String> chroToPheno = salesman.getChromoToPhenoMap(wayPoints, geneSize);
        Map<String, String> phenoToChron = salesman.getPhenoToChromo(wayPoints, geneSize);

        BinaryGA ga = new HeuristicCrossOver(0.05);

        salesman.runGA(wayPoints, chroToPheno, phenoToChron, ga,geneSize, 100);

    }

    public void runGA(List<String> origWaypoints, Map<String, String> chroToPheno, Map<String, String> phenoToChron , BinaryGA ga, int geneSize, int populationSize){
        List<String> waypoints = new ArrayList<String>();
        waypoints.addAll(origWaypoints);

        Individual individual = new BinaryIndividual(waypoints, phenoToChron, geneSize);
        List<Individual> initPopulation = Population.initialPopulation(individual, phenoToChron, populationSize);

        Population population = new Population(chroToPheno, phenoToChron, ga, initPopulation, geneSize);
        population.sortPopulation();
        individual = new BinaryIndividual(origWaypoints, phenoToChron,geneSize);
        population.replaceWorst(individual);

        for(int i=0; i<2000; i++){
            System.out.println("Generation :"+i);
            population = population.getNextGeneration();
            //population.printPopulation();
            population.printStatistics();

            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println();
        }


        Map<String, JSONObject> wayPointsMap = this.getWaypointsMap(json, size);
        Individual bestIn = population.getBest();
        for(String str : (List<String>) bestIn.getPhenotype()){
            JSONObject obj = wayPointsMap.get(str);
            System.out.println("lat,long : "+str+ " ; city :"+obj.getString("city")+" ; state :"+obj.getString("state"));
        }
        System.out.println(bestIn.fitness());




        /*GeoApiContext context = new GeoApiContext().setApiKey(Constants.googleDirectionsAPIKey);
        GoogleMapsAPI api = new GoogleMapsAPI(context);

        DirectionsResult result = api.directionsFromWaypoints(api.getWaypoints((List<String>) bestIn.getPhenotype()));
        System.out.println(api.getDistance(result));*/
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
