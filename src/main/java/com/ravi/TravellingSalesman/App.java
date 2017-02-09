package com.ravi.TravellingSalesman;

import com.google.maps.*;
import com.google.maps.model.*;
import com.ravi.TravellingSalesman.GeneticAlgorithm.BinaryGA;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Individual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.Population;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.BinaryIndividual;
import com.ravi.TravellingSalesman.GeneticAlgorithm.impl.SinglePointCrossOverGA;
import com.ravi.TravellingSalesman.Utils.Constants;
import com.ravi.TravellingSalesman.Utils.GoogleMapsAPI;

import org.joda.time.Instant;

import java.util.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        BinaryGA ga = new SinglePointCrossOverGA(0.15);

        List<String> origWaypoints = new ArrayList<String>();
        origWaypoints.add("51.508530,-0.076132");
        origWaypoints.add("52.489471,-1.898575");
        origWaypoints.add("53.397495,-2.974092");
        origWaypoints.add("53.483959,-2.244644");
        origWaypoints.add("53.383055,-1.464795");
        origWaypoints.add("53.801277,-1.548567");
        origWaypoints.add("54.993168,-1.603431");
        origWaypoints.add("55.865101,-4.433177");

        Map<String, String> chroToPheno = new HashMap<String, String>();
        chroToPheno.put("000","51.508530,-0.076132");
        chroToPheno.put("001","52.489471,-1.898575");
        chroToPheno.put("010","53.483959,-2.244644");
        chroToPheno.put("011","55.865101,-4.433177");
        chroToPheno.put("100","54.993168,-1.603431");
        chroToPheno.put("101","53.383055,-1.464795");
        chroToPheno.put("110","53.397495,-2.974092");
        chroToPheno.put("111","53.801277,-1.548567");

        Map<String, String> phenoToChron = new HashMap<String, String>();
        phenoToChron.put("51.508530,-0.076132","000");
        phenoToChron.put("52.489471,-1.898575","001");
        phenoToChron.put("53.483959,-2.244644","010");
        phenoToChron.put("55.865101,-4.433177","011");
        phenoToChron.put("54.993168,-1.603431","100");
        phenoToChron.put("53.383055,-1.464795","101");
        phenoToChron.put("53.397495,-2.974092","110");
        phenoToChron.put("53.801277,-1.548567","111");

        List<String> waypoints = new ArrayList<String>();
        waypoints.addAll(origWaypoints);
        Individual individual = new BinaryIndividual(waypoints, phenoToChron, 3);
        List<Individual> initPopulation = Population.initialPopulation(individual, phenoToChron, 10);

        Population population = new Population(chroToPheno, phenoToChron, ga, initPopulation, 4);
        population.sortPopulation();
        individual = new BinaryIndividual(origWaypoints, phenoToChron,3);
        population.replaceWorst(individual);

        for(int i=0; i<1000; i++){
            System.out.println("Generation :"+i);
            population = population.getNextGeneration();
            population.printPopulation();
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

        waypoints = new ArrayList<String>();
        waypoints.add("51.508530,-0.076132");
        waypoints.add("52.489471,-1.898575");
        waypoints.add("53.397495,-2.974092");
        waypoints.add("53.483959,-2.244644");
        waypoints.add("53.383055,-1.464795");
        waypoints.add("53.801277,-1.548567");
        waypoints.add("54.993168,-1.603431");
        waypoints.add("55.865101,-4.433177");
        individual = new BinaryIndividual(waypoints, phenoToChron,3);

        System.out.println(individual.fitness());



    }

    public void test(){
        GeoApiContext context = new GeoApiContext().setApiKey(Constants.googleDirectionsAPIKey);
        GoogleMapsAPI api = new GoogleMapsAPI(context);

        try {

            // DirectionsResult result = DirectionsApi.getDirections(context, "Sydney, AU","Melbourne, AU").await();

            GoogleMapsAPI gapi = new GoogleMapsAPI(context);

            //gapi.getDistance(result);

            List<LatLng> waypoints = new ArrayList<LatLng>();
            waypoints.add(new LatLng(51.508530,-0.076132));
            waypoints.add(new LatLng(52.489471,-1.898575));
            waypoints.add(new LatLng(53.483959,-2.244644));
            waypoints.add(new LatLng(55.865101,-4.433177));
            waypoints.add(new LatLng(54.993168,-1.603431));
            waypoints.add(new LatLng(53.383055,-1.464795));
            waypoints.add(new LatLng(53.397495,-2.974092));
            waypoints.add(new LatLng(53.801277,-1.548567));

            List<LatLng> origins = new ArrayList<LatLng>();
            List<LatLng> destinations = new ArrayList<LatLng>();

            for(LatLng lng : waypoints){
                for(LatLng lngd : waypoints){
                    if(lng.equals(lngd)){
                        continue;
                    }
                    origins.add(lng);
                    destinations.add(lngd);

                    System.out.println("From "+lng.toString() + " To : "+lngd.toString());
                }
            }

            DistanceMatrix matrix = DistanceMatrixApi.newRequest(context)
                    .origins(origins.subList(0,10).toArray(new LatLng[0]))
                    .destinations(destinations.subList(0,10).toArray(new LatLng[0]))
                    .await();


            DistanceMatrixRow[] rows = matrix.rows;
            for(DistanceMatrixRow row : rows){
                DistanceMatrixElement[] elements = row.elements;
                System.out.println(row.toString());
                for(DistanceMatrixElement element : elements){
                    System.out.println(element.distance);
                }
            }

           /* DirectionsApiRequest request = DirectionsApi.newRequest(context)
                    .origin(waypoints.get(0))
                    .destination(waypoints.get(waypoints.size()-1))
                    .departureTime(Instant.now())
                    .waypoints(waypoints.subList(1, waypoints.size()-1).toArray(new LatLng[0]));

            result = request.awaitIgnoreError();
            double distance = gapi.getDistance(result);

            System.out.println(distance);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
