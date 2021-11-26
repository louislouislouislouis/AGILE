package org.hexanome.controller.tsp;

import javafx.beans.binding.ListExpression;
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

    // shortestPathsIntersections Map with
    // key: idStartIntersection
    // value: List of Maps with key: idDestinationIntersection
    //                          value: List idIntersections to pass from start to destination
    private Map<Long, Map<Long,List<Long>>> shortestPathsIntersections;

    // shortestPathsIntersections Map with
    // key: idStartIntersection
    // value: List of Maps with key: idDestinationIntersection
    //                          value: cost of complete shortest path between start and destination
    private Map<Long, Map<Long,Double>> shortestPathsCost;

    public void GraphAPI() {
        this.shortestPathsIntersections = new HashMap<>();
        this.shortestPathsCost = new HashMap<>();
    }

    public Tour V1_TSP(PlanningRequest planning, MapIF map) {
        //this.calculateDijkstra(planning,map);
        Map<Intersection, Map<Intersection, Segment>> adj = this.getAdj(map);
        Set<Intersection> destinations = new LinkedHashSet<>();
        destinations.add(planning.getWarehouse().getAddress());
        for (Request r : planning.getRequests()) {
            destinations.add(r.getPickupPoint().getAddress());
            destinations.add(r.getDeliveryPoint().getAddress());
        }

        int nbVerticesDijkstra = map.getIntersections().size();
        int nbVerticesTSP = destinations.size();

        Double[][] costTSP = new Double[nbVerticesTSP][nbVerticesTSP];
        Map <Long, Map<Long,Double>> adjTSP = new HashMap<>();
        Long idZero = Long.valueOf(0);

        for (Intersection origin : destinations) {
            System.out.println("Calculating shortest paths for Origin: " + origin.getIdIntersection());
            Dijkstra dijkstra = new Dijkstra(nbVerticesDijkstra);
            dijkstra.dijkstra(map.getIntersections(), adj, origin, destinations);


            if (!this.getShortestPathsIntersections().containsKey(origin.getIdIntersection())) {
                Map<Long, List<Long>> emptyMap = new HashMap<>();
                this.getShortestPathsIntersections().put(origin.getIdIntersection(), emptyMap);
            }
            Map<Long, List<Long>> currentPaths = dijkstra.getPath();
            for (Long l : currentPaths.keySet()) {
                this.getShortestPathsIntersections().get(origin.getIdIntersection()).put(l, currentPaths.get(l));
            }

            if (!this.getShortestPathsCost().containsKey(origin.getIdIntersection())) {
                Map<Long, Double> emptyCosts = new HashMap<>();
                this.getShortestPathsCost().put(origin.getIdIntersection(), emptyCosts);
            }
            Map<Long, Double> currentCosts = new HashMap<>();
            for (Long l : currentCosts.keySet()) {
                this.getShortestPathsCost().get(origin.getIdIntersection()).put(l, currentCosts.get(l));
            }

            Map<Long, Double> distTSP = dijkstra.getResultDistForTSP(destinations);

            Map<Long, Double> distDestinationsTSP = new LinkedHashMap<>();
            for (Intersection i : destinations) {
                if (distTSP.containsKey(i.getIdIntersection())) {
                    distDestinationsTSP.put(i.getIdIntersection(), distTSP.get(i.getIdIntersection()));
                }
            }

            adjTSP.put(idZero++, distDestinationsTSP);
        }
            for (Map.Entry<Long, Map<Long, Double>> i : adjTSP.entrySet()) {
                System.out.println(i.getKey() + " " + i.getValue());
                int jj = 0;
                for (Map.Entry<Long, Double> j : i.getValue().entrySet()) {
                    costTSP[i.getKey().intValue()][jj++] = j.getValue();
                }
            }

            for (int i = 0; i < nbVerticesTSP; i++) {
                for (int j = 0; j < nbVerticesTSP; j++) {
                    if (costTSP[i][j] == 0) {
                        costTSP[i][j] = Double.valueOf(-1);
                    }
                    System.out.print(costTSP[i][j] + " ");
                }
                System.out.println();
            }


            Graph g = new CompleteGraph(nbVerticesTSP, costTSP);

            long startTime = System.currentTimeMillis();
            TSP tsp = new TSP1();
            tsp.searchSolution(20000, g);
            System.out.print("Solution of cost " + tsp.getSolutionCost() + " found in "
                    + (System.currentTimeMillis() - startTime) + "ms : ");


            // Converting LinkedHashMap to Array
            Intersection[] LHSArray = new Intersection[destinations.size()];
            LHSArray = destinations.toArray(LHSArray);

            List<Intersection> pathTSP = new ArrayList<>();
            for (int i = 0; i < nbVerticesTSP; i++) {
                pathTSP.add(LHSArray[tsp.getSolution(i)]);
            }

            for (Intersection i : pathTSP) {
                System.out.println(i);
            }

            List<Intersection> completeTour = new ArrayList<>();
            completeTour.add(pathTSP.get(0));
            for (int i = 0; i < pathTSP.size()-1; i++){
                Intersection startIntersection = pathTSP.get(i);
                Intersection destinationIntersection = pathTSP.get(i+1);
                List<Intersection> currentPath = new ArrayList<>();
                for ( Long l : this.getShortestPathsIntersections().get(startIntersection.getIdIntersection()).get(destinationIntersection.getIdIntersection())) {
                    Intersection intersection = map.getIntersections().get(l);
                    if (!intersection.equals(pathTSP.get(i))) {
                        currentPath.add(intersection);
                    }
                }
                for (Intersection intersection : currentPath) {
                    completeTour.add(intersection);
                }
            }

            Tour tour = new Tour(pathTSP,tsp.getSolutionCost());

        return tour;
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

    public Map<Long, Map<Long, List<Long>>> getShortestPathsIntersections() {
        return shortestPathsIntersections;
    }

    public Map<Long, Map<Long, Double>> getShortestPathsCost() {
        return shortestPathsCost;
    }

    private void calculateDijkstra(PlanningRequest planning, MapIF map) {

    }

}
