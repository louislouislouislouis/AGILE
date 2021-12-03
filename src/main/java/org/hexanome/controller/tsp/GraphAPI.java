package org.hexanome.controller.tsp;

import org.hexanome.model.*;

import java.util.*;

public class GraphAPI {

    public GraphAPI() {
    }

    public void V1_TSP(PlanningRequest planning, MapIF map, Tour tour) {

        // shortestPathsIntersections Map with
        // key: idStartIntersection
        // value: List of Maps with key: idDestinationIntersection
        // value: List idIntersections to pass from start to destination
        Map<Long, Map<Long, List<Long>>> shortestPathsIntersections = new HashMap<>();

        // shortestPathsIntersections Map with
        // key: idStartIntersection
        // value: List of Maps with key: idDestinationIntersection
        // value: cost of complete shortest path between start and destination
        Map<Long, Map<Long, Double>> shortestPathsCost = new HashMap<>();

        Map<Intersection, Map<Intersection, Segment>> adj = map.getMatAdj();
        Set<Intersection> destinations = this.getDestinations(planning);

        int nbVerticesDijkstra = map.getIntersections().size();
        int nbVerticesTSP = destinations.size();

        Map<Long, Map<Long, Double>> adjTSP = new HashMap<>();
        long idZero = 0L;
        // calculate the shortest path from every destination to every other destination + update maps
        for (Intersection origin : destinations) {
            //System.out.println("Calculating shortest paths for Origin: " + origin.getIdIntersection());
            Dijkstra dijkstra = new Dijkstra(nbVerticesDijkstra);
            dijkstra.dijkstra(map.getIntersections(), adj, origin, destinations);

            this.updateShortestPathIntersections(shortestPathsIntersections, dijkstra, origin);
            this.updateShortestPathsCost(shortestPathsCost, dijkstra, origin);
            this.updateAdjTSP(adjTSP, dijkstra, destinations, idZero++);
        }

        Map<Integer,Long> mapIdTSP = new HashMap<>();
        Double[][] costTSP = new Double[nbVerticesTSP][nbVerticesTSP];

        this.calculateMapIdTSPAndCostTSP(adjTSP, mapIdTSP, costTSP, nbVerticesTSP);

        Graph g = new CompleteGraph(nbVerticesTSP, costTSP);
        long startTime = System.currentTimeMillis();
        TSP tsp = new TSP1(costTSP, mapIdTSP, planning.getRequests(), shortestPathsIntersections, shortestPathsCost, map);
        tour.addIntersection(planning.getWarehouse().getAddress()); // add Warehouse to tour
        tsp.searchSolution(20000, g, tour);
        // System.out.print("Solution of cost " + tsp.getSolutionCost() + " found in "
        //       + (System.currentTimeMillis() - startTime) + "ms : ");

        // Converting LinkedHashMap to Array
        Intersection[] LHSArray = new Intersection[destinations.size()];
        LHSArray = destinations.toArray(LHSArray);

        List<Intersection> pathTSP = new ArrayList<>();
        for (int i = 0; i < nbVerticesTSP; i++) {
            pathTSP.add(LHSArray[tsp.getSolution(i)]);
        }
        pathTSP.add(planning.getWarehouse().getAddress());

        for (Intersection i : pathTSP) {
            //System.out.println(i);
        }

/*        List<Intersection> completeTour = new ArrayList<>();
        completeTour.add(pathTSP.get(0));
        for (int i = 0; i < pathTSP.size() - 1; i++) {
            Intersection startIntersection = pathTSP.get(i);
            Intersection destinationIntersection = pathTSP.get(i + 1);
            for (Long l : shortestPathsIntersections.get(startIntersection.getIdIntersection()).get(destinationIntersection.getIdIntersection())) {
                Intersection intersection = map.getIntersections().get(l);
                if (!intersection.equals(pathTSP.get(i))) {
                    completeTour.add(intersection);
                }
            }
        }
 */
        //System.out.println(tour.getIntersections().size());
//        tour.setIntersections(completeTour);
//        tour.setCost(tsp.getSolutionCost());
        System.out.println(tour.getIntersections().size());

    }

    /**
     *
     * @param adjTSP contains cost between Intersections
     * @param mapIdTSP maps idTSP to idIntersection
     * @param costTSP array 2 dimensions index idTSP
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
                    mapIdTSP.put(i.getKey().intValue(),j.getKey());
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
     *
     * @param adjTSP adjacency matrix for calculating TSP
     * @param dijkstra instance for calculating the shortest paths
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
     * @param shortestPathsCost map startIntersection - destinationIntersection - cost
     * @param dijkstra instance for calculating the shortest paths
     * @param origin startIntersection
     */
    private void  updateShortestPathsCost(Map<Long, Map<Long, Double>> shortestPathsCost, Dijkstra dijkstra, Intersection origin) {
        if (!shortestPathsCost.containsKey(origin.getIdIntersection())) {
            Map<Long, Double> emptyCosts = new HashMap<>();
            shortestPathsCost.put(origin.getIdIntersection(), emptyCosts);
        }
        Map<Long, Double> currentCosts = dijkstra.getDist();
        for (Long l : currentCosts.keySet()) {
            shortestPathsCost.get(origin.getIdIntersection()).put(l, currentCosts.get(l));
        }
    }

    /**
     * add the sequence of intersections for the shortest paths from origin to every other destination to shortestPathsIntersections
     * @param shortestPathsIntersections map startIntersection - destinationIntersection - sequenceIntersectionsBetween
     * @param dijkstra instance for calculating the shortest paths
     * @param origin startIntersection
     */
    private void updateShortestPathIntersections(Map<Long, Map<Long, List<Long>>> shortestPathsIntersections, Dijkstra dijkstra, Intersection origin) {
        if (!shortestPathsIntersections.containsKey(origin.getIdIntersection())) {
            Map<Long, List<Long>> emptyMap = new HashMap<>();
            shortestPathsIntersections.put(origin.getIdIntersection(), emptyMap);
        }
        Map<Long, List<Long>> currentPaths = dijkstra.getPath();
        for (Long l : currentPaths.keySet()) {
            shortestPathsIntersections.get(origin.getIdIntersection()).put(l, currentPaths.get(l));
        }
    }



    /**
     *
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
