package com.ravi.TravellingSalesman.Utils;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.*;
import org.joda.time.Instant;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ravik on 08/02/2017.
 */
public class GoogleMapsAPI {

    private GeoApiContext context;

    public GoogleMapsAPI(GeoApiContext context) {
        this.context = context;
    }

    public double getDistance(DirectionsResult result){
        DirectionsRoute[] routes = result.routes;

        double totalDistance = 0.0;
        double distanceInKm = 0.0;

        for(DirectionsRoute route : routes){
            DirectionsLeg legs[] = route.legs;
            for(DirectionsLeg leg : legs){
                System.out.println(leg.startAddress);
                System.out.println(leg.endAddress);
                Distance distance = leg.distance;
                distanceInKm = distance.inMeters/1000;
                totalDistance = totalDistance + distanceInKm;
            }
        }

        return totalDistance;
    }

    public List<LatLng> getWaypoints(List<String> latlonglist){
        List<LatLng> waypoints = new ArrayList<LatLng>();
        for(String latlong: latlonglist){
            String[] arr = latlong.split(",");
            LatLng lng = new LatLng(Double.parseDouble(arr[0]), Double.parseDouble(arr[1]));
            waypoints.add(lng);
        }
        return waypoints;
    }

    public DirectionsResult directionsFromWaypoints(List<LatLng> waypoints){
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(waypoints.get(0))
                .destination(waypoints.get(waypoints.size()-1))
                .departureTime(Instant.now())
                .waypoints(waypoints.subList(1, waypoints.size()-1).toArray(new LatLng[0]));

        return request.awaitIgnoreError();
    }
}
