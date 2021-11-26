package org.hexanome.controller.tsp;

import org.hexanome.controller.Dijkstra;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class GraphAPI {
    public void GraphAPI() {

    }

    public Tour V1_TSP(PlanningRequest planning, MapIF map) {
        Map<Intersection, Map<Intersection, Segment>> adj = this.getAdj(map);
        Set<Intersection> destinations = new HashSet<>();
        destinations.add(planning.getWarehouse().getAddress());
        for (Request r : planning.getRequests()) {
            destinations.add(r.getPickupPoint().getAddress());
            destinations.add(r.getDeliveryPoint().getAddress());
        }

        int nbVerticesDijkstra = map.getIntersections().size();
        int nbVerticesTSP = destinations.size();

        Double[][] costTSP = new Double[nbVerticesTSP][nbVerticesTSP];
        int iOrigin = 0;
        for (Intersection origin : destinations) {
            System.out.println("Calculating shortest paths for Origin: " + origin.getIdIntersection());
            Dijkstra dijkstra = new Dijkstra(nbVerticesDijkstra);
            dijkstra.dijkstra(map.getIntersections(), adj, origin, destinations);
            System.out.println(dijkstra.getPath());
            System.out.println(dijkstra.getDist());

            Map<Long, Double> distTSP = dijkstra.getResultDistForTSP(destinations);
            int j = 0;
            for (Double c : distTSP.values()) {
                costTSP[iOrigin][j++] = c;
            }
            iOrigin++;
        }

        for (int i = 0; i < nbVerticesTSP; i++) {
            for (int j = 0; j < nbVerticesTSP; j++) {
                System.out.print(costTSP[i][j] + " ");
            }
            System.out.println();
        }
        return null;
    }

    private Map<Intersection, Map<Intersection, Segment>> getAdj(MapIF myMap) {
        Map<Intersection, Map<Intersection, Segment>> adj = new HashMap<>();
        for (Intersection i : myMap.getIntersections().values()) {
            Map<Intersection, Segment> emptyMap = new HashMap<>();
            adj.put(i, emptyMap);
        }
        for (Segment s : myMap.getSegments().values()) {
            adj.get(s.getOriginIntersection()).put(s.getDestinationIntersection(), s);
        }
        return adj;

    }

}
