package org.hexanome.controller.tsp;

import org.hexanome.controller.MainsScreenController;
import org.hexanome.model.*;

import java.time.LocalTime;
import java.util.*;

public class GraphAPI {

    public GraphAPI() {
    }

    public void V1_TSP(PlanningRequest planning, MapIF map, Tour tour, MainsScreenController allowCalculation) {
        Map<Intersection, Map<Intersection, Segment>> adj = map.getMatAdj();
        Set<Intersection> destinations = this.getDestinations(planning);

        int nbVerticesDijkstra = map.getIntersections().size();
        int nbVerticesTSP = destinations.size();

        Map<Long, Map<Long, Double>> adjTSP = new HashMap<>();
        long idZero = 0L;
        // calculate the shortest path from every destination to every other destination + update maps
        for (Intersection origin : destinations) {
            Dijkstra dijkstra = new Dijkstra(nbVerticesDijkstra);
            dijkstra.dijkstra(map.getIntersections(), adj, origin, destinations);

            this.updateShortestPathIntersections(map, dijkstra, origin);
            this.updateShortestPathsCost(map, dijkstra, origin);
            this.updateAdjTSP(adjTSP, dijkstra, destinations, idZero++);
        }

        Map<Integer, Long> mapIdTSP = new HashMap<>();
        Double[][] costTSP = new Double[nbVerticesTSP][nbVerticesTSP];

        this.calculateMapIdTSPAndCostTSP(adjTSP, mapIdTSP, costTSP, nbVerticesTSP);

        Graph g = new CompleteGraph(nbVerticesTSP, costTSP);
        long startTime = System.currentTimeMillis();
        TSP tsp = new TSP1(costTSP, mapIdTSP, planning.getRequests(), map, destinations, planning);

        // add Warehouse to tour
        tour.addIntersection(planning.getWarehouse().getAddress());
        tour.setWarehouse(planning.getWarehouse().getAddress());
        tour.addDestination(planning.getWarehouse().getAddress());
        tour.addPoint(planning.getWarehouse());

        tsp.searchSolution(60000, g, tour, allowCalculation);
        System.out.print("End of method");
        //       + (System.currentTimeMillis() - startTime) + "ms : ");

    }

    public void ADD_REQUEST(PlanningRequest planning, MapIF map, Tour tour) {
        Map<Intersection, Map<Intersection, Segment>> adj = map.getMatAdj();
        Set<Intersection> destinations = this.getDestinations(planning);

        int nbVerticesDijkstra = map.getIntersections().size();

        Request newRequest = planning.getRequests().getLast();
        Intersection newPickUpPoint = newRequest.getPickupPoint().getAddress();
        Intersection newDeliveryPoint = newRequest.getDeliveryPoint().getAddress();

        Dijkstra dijkstra1 = new Dijkstra(nbVerticesDijkstra);
        dijkstra1.dijkstra(map.getIntersections(), adj, newPickUpPoint, destinations);
        this.updateShortestPathIntersections(map, dijkstra1, newPickUpPoint);
        this.updateShortestPathsCost(map, dijkstra1, newPickUpPoint);

        Dijkstra dijkstra2 = new Dijkstra(nbVerticesDijkstra);
        dijkstra2.dijkstra(map.getIntersections(), adj, newDeliveryPoint, destinations);
        this.updateShortestPathIntersections(map, dijkstra2, newDeliveryPoint);
        this.updateShortestPathsCost(map, dijkstra2, newDeliveryPoint);

//        if (!this.addRequestDuringTour(tour, planning, map)) {
        tour.removeLastDestination(); // remove Warehouse in the end of the tour
        tour.addDestination(newPickUpPoint); // add new PickupPoint
        tour.addDestination(newDeliveryPoint); // add new DeliveryPoint
        tour.addDestination(planning.getWarehouse().getAddress()); // add Warehouse in the end of the tour
        tour.updateTimingForNewRequest(map, newRequest.getPickupPoint(), newRequest.getDeliveryPoint());
        tour.computeCompleteTour(map);
        tour.calculateCost(map);
        tour.notifyChange("UPDATEMAP");
        //       }
    }

 /*   private boolean addRequestDuringTour(Tour tour, PlanningRequest planning, MapIF map) {
        if (!isTimeLeftDuringDeliveries(tour, planning, map)) {
            return false;
        }
    }
  */

    /**
     * @param tour     contains planned tour
     * @param planning contains requests
     * @param map      contains Intersections and Segments
     * @return true if driver has waiting between two Points, otherwise false
     */
    private boolean isTimeLeftDuringDeliveries(Tour tour, PlanningRequest planning, MapIF map) {
        for (Request r : planning.getRequests()) {
            LocalTime departureTime = r.getPickupPoint().getDepartureTime();
            LocalTime arrivalTime = r.getDeliveryPoint().getArrivalTime();
            int maxSecondsForPath = arrivalTime.getSecond() - departureTime.getSecond();
            int actualSecondsForPath = tour.getSecondsForPath(map, r.getPickupPoint().getAddress(), r.getDeliveryPoint().getAddress());
            if (maxSecondsForPath - actualSecondsForPath > 0) {
                return true;
            }
        }
        return false;
    }


    public void DELETE_REQUEST(Request request, MapIF map, Tour tour) {
        tour.removeRequest(request, map);
        tour.notifyChange("UPDATEMAP");
    }

    /**
     * @param adjTSP   contains cost between Intersections
     * @param mapIdTSP maps idTSP to idIntersection
     * @param costTSP  array 2 dimensions index idTSP
     */
    private void calculateMapIdTSPAndCostTSP(Map<Long, Map<Long, Double>> adjTSP,
                                             Map<Integer, Long> mapIdTSP,
                                             Double[][] costTSP,
                                             int nbVerticesTSP) {
        for (Map.Entry<Long, Map<Long, Double>> i : adjTSP.entrySet()) {
            System.out.println(i.getKey() + " " + i.getValue());
            int jj = 0;
            for (Map.Entry<Long, Double> j : i.getValue().entrySet()) {
                if (j.getValue() == 0) {
                    mapIdTSP.put(i.getKey().intValue(), j.getKey());
                }
                costTSP[i.getKey().intValue()][jj++] = j.getValue();
            }
        }
        System.out.println("MapIdTSP: " + mapIdTSP);

        for (int i = 0; i < nbVerticesTSP; i++) {
            for (int j = 0; j < nbVerticesTSP; j++) {
                if (costTSP[i][j] == 0) {
                    costTSP[i][j] = (double) -1;
                }
                System.out.print(costTSP[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * @param adjTSP       adjacency matrix for calculating TSP
     * @param dijkstra     instance for calculating the shortest paths
     * @param destinations
     * @param idZero
     */
    private void updateAdjTSP(Map<Long, Map<Long, Double>> adjTSP, Dijkstra dijkstra, Set<Intersection> destinations, Long idZero) {
        Map<Long, Double> distTSP = dijkstra.getResultDistForTSP(destinations);
        Map<Long, Double> distDestinationsTSP = new LinkedHashMap<>();
        for (Intersection i : destinations) {
            if (distTSP.containsKey(i.getIdIntersection())) {
                distDestinationsTSP.put(i.getIdIntersection(), distTSP.get(i.getIdIntersection()));
            }
        }
        adjTSP.put(idZero, distDestinationsTSP);
    }

    /**
     * add the cost of the shortest paths from origin to every other destination to shortestPathsCost
     *
     * @param map      contains shortestPathsCost
     * @param dijkstra instance for calculating the shortest paths
     * @param origin   startIntersection
     */
    private void updateShortestPathsCost(MapIF map, Dijkstra dijkstra, Intersection origin) {
        Map<Long, Map<Long, Double>> shortestPathsCost = map.getShortestPathsCost();
        if (!shortestPathsCost.containsKey(origin.getIdIntersection())) {
            Map<Long, Double> emptyCosts = new HashMap<>();
            shortestPathsCost.put(origin.getIdIntersection(), emptyCosts);
        }
        Map<Long, Double> currentCosts = dijkstra.getDist();
        for (Long l : currentCosts.keySet()) {
            shortestPathsCost.get(origin.getIdIntersection()).put(l, currentCosts.get(l));
        }
        map.setShortestPathsCost(shortestPathsCost);
    }

    /**
     * add the sequence of intersections for the shortest paths from origin to every other destination to shortestPathsIntersections
     *
     * @param map      contains shortestPathIntersections
     * @param dijkstra instance for calculating the shortest paths
     * @param origin   startIntersection
     */
    private void updateShortestPathIntersections(MapIF map, Dijkstra dijkstra, Intersection origin) {
        Map<Long, Map<Long, List<Long>>> shortestPathsIntersections = map.getShortestPathsIntersections();
        if (!shortestPathsIntersections.containsKey(origin.getIdIntersection())) {
            Map<Long, List<Long>> emptyMap = new HashMap<>();
            shortestPathsIntersections.put(origin.getIdIntersection(), emptyMap);
        }
        Map<Long, List<Long>> currentPaths = dijkstra.getPath();
        for (Long l : currentPaths.keySet()) {
            shortestPathsIntersections.get(origin.getIdIntersection()).put(l, currentPaths.get(l));
        }
        map.setShortestPathsIntersections(shortestPathsIntersections);
    }


    /**
     * @param planning contains all requests
     * @return Set of Warehouse and all Pickup and Delivery Points
     */
    private Set<Intersection> getDestinations(PlanningRequest planning) {
        Set<Intersection> destinations = new LinkedHashSet<>();
        destinations.add(planning.getWarehouse().getAddress());
        for (Request r : planning.getRequests()) {
            destinations.add(r.getPickupPoint().getAddress());
            destinations.add(r.getDeliveryPoint().getAddress());
        }
        return destinations;
    }

}
