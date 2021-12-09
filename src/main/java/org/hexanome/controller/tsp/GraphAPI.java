package org.hexanome.controller.tsp;

import javafx.util.Pair;
import org.hexanome.controller.MainsScreenController;
import org.hexanome.model.*;

import java.time.Duration;
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

        tsp.searchSolution(g, tour, allowCalculation);
        System.out.print("End of method");
        //       + (System.currentTimeMillis() - startTime) + "ms : ");

    }

    public void ADD_REQUEST(PlanningRequest planning, MapIF map, Tour tour, Boolean isUndo) {
        Map<Intersection, Map<Intersection, Segment>> adj = map.getMatAdj();
        Set<Intersection> destinations = this.getDestinations(planning);

        int nbVerticesDijkstra = map.getIntersections().size();

        Request newRequest = planning.getRequests().getLast();
        Intersection newPickUpPoint = newRequest.getPickupPoint().getAddress();
        Intersection newDeliveryPoint = newRequest.getDeliveryPoint().getAddress();

        if (isUndo) {
            tour.addPoint(tour.getPoints().get(0)); // add Warehouse
            Boolean isSet = false;
            int i = 1;
            while (!isSet && i < tour.getPoints().size()) {
                if (tour.getPoints().get(i).getArrivalTime().isAfter(newRequest.getPickupPoint().getArrivalTime())) {
                    tour.addPointBetween(tour.getPoints().get(i - 1), tour.getPoints().get(i), newRequest.getPickupPoint());
                    isSet = true;
                }
                i++;
            }
            isSet = false;
            i = 1;
            while (!isSet && i < tour.getPoints().size()) {
                if (tour.getPoints().get(i).getArrivalTime().isAfter(newRequest.getDeliveryPoint().getArrivalTime())) {
                    tour.addPointBetween(tour.getPoints().get(i - 1), tour.getPoints().get(i), newRequest.getDeliveryPoint());
                    isSet = true;
                }
                i++;
            }
            tour.deleteLastPoint();
            tour.updateDestinationsByPoints();
        } else {
            Dijkstra dijkstra1 = new Dijkstra(nbVerticesDijkstra);
            dijkstra1.dijkstra(map.getIntersections(), adj, newPickUpPoint, destinations);
            this.updateShortestPathIntersections(map, dijkstra1, newPickUpPoint);
            this.updateShortestPathsCost(map, dijkstra1, newPickUpPoint);

            Dijkstra dijkstra2 = new Dijkstra(nbVerticesDijkstra);
            dijkstra2.dijkstra(map.getIntersections(), adj, newDeliveryPoint, destinations);
            this.updateShortestPathIntersections(map, dijkstra2, newDeliveryPoint);
            this.updateShortestPathsCost(map, dijkstra2, newDeliveryPoint);

            // pairs of points where new request could potentially be added
            List<Pair<Point, Point>> haveWaitingTimes = new ArrayList<>();

            // add newPickup in tour if possible
            if (!this.addRequestDuringTour(tour, planning, map, haveWaitingTimes)) {
                // if not possible add newPickup and newDelivery in the end of the tour
                tour.removeLastDestination(); // remove Warehouse in the end of the tour
                tour.addDestination(newPickUpPoint); // add new PickupPoint
                tour.addDestination(newDeliveryPoint); // add new DeliveryPoint
                tour.addDestination(planning.getWarehouse().getAddress()); // add Warehouse in the end of the tour
                tour.updateTimingForNewRequest(map, newRequest.getPickupPoint(), newRequest.getDeliveryPoint());
            }
        }
        tour.computeCompleteTour(map);
        tour.calculateCost(map);
        tour.notifyChange("UPDATEMAP");
    }

    /**
     * @param tour      contains tour data
     * @param planning  contains requests
     * @param map       contains Intersections and Segments
     * @param emptyList where to store pairs with the potential to add new request between
     * @return true if newPickup could have been added during the tour, false otherwise
     */
    public boolean addRequestDuringTour(Tour tour, PlanningRequest planning, MapIF map, List<Pair<Point, Point>> emptyList) {
        boolean addRequestSuccessfully = false;

        List<Pair<Point, Point>> haveWaitingTimes = isTimeLeftDuringDeliveries(tour, planning, map, emptyList);
        if (haveWaitingTimes.isEmpty()) {
            return false;
        }

        Point newPickup = planning.getRequests().getLast().getPickupPoint();
        Point newDelivery = planning.getRequests().getLast().getDeliveryPoint();

        // compute all possibilities to add request in tour
        Tour newTour = new Tour(tour.getIntersections(), tour.getCost(), tour.getDepartureTime(), tour.getMatAdj(), tour.getWarehouse(), tour.getDestinations(), tour.getPoints());
        Tour newTour2 = new Tour(tour.getIntersections(), tour.getCost(), tour.getDepartureTime(), tour.getMatAdj(), tour.getWarehouse(), tour.getDestinations(), tour.getPoints());


        // check Pickup Points
        for (int i = 0; i < haveWaitingTimes.size(); i++) {
            Pair<Point, Point> p = haveWaitingTimes.get(i);
            Point start = p.getKey();
            Point destination = p.getValue();

            if (this.checkIfAddableBetween(map, newTour2, start, destination, newPickup)) {
                addRequestSuccessfully = true;

                //check if time left between newPickup and next Point and if so at to havingWaitingTimes
                LocalTime departureTime = newPickup.getDepartureTime();
                LocalTime arrivalTime = destination.getArrivalTime();
                Duration difference = Duration.between(departureTime, arrivalTime);
                int restTimeToTravel = (int) difference.getSeconds();
                int currentTimeToTravel = tour.getSecondsForPath(map, newPickup.getAddress(), destination.getAddress());

                List<Pair<Point, Point>> updatedHaveWaitingTimes = new ArrayList<>();
                if (currentTimeToTravel < restTimeToTravel) {
                    Pair<Point, Point> newPair = new Pair<>(newPickup, destination);
                    updatedHaveWaitingTimes.add(newPair);
                }
                for (int j = 0; j < haveWaitingTimes.size(); j++) {
                    if (j > i) {
                        updatedHaveWaitingTimes.add(haveWaitingTimes.get(i));
                    }
                }

                // check DeliveryPoints
                for (int j = 0; j < updatedHaveWaitingTimes.size(); j++) {
                    Pair<Point, Point> p2 = updatedHaveWaitingTimes.get(j);
                    Point start2 = p2.getKey();
                    Point destination2 = p2.getValue();

                    if (!this.checkIfAddableBetween(map, newTour2, start2, destination2, newDelivery)) {
                        // if not possible add newDelivery in the end of the tour
                        newTour2.removeLastDestination(); // remove Warehouse in the end of the tour
                        newTour2.addDestination(newDelivery.getAddress()); // add new DeliveryPoint
                        newTour2.addDestination(planning.getWarehouse().getAddress()); // add Warehouse in the end of the tour
                        newTour2.updateTimingForNewDestination(map, planning.getRequests().getLast().getDeliveryPoint());
                    }

                    newTour2.computeCompleteTour(map);
                    newTour2.calculateCost(map);

                    if (newTour.getPoints().size() <= tour.getPoints().size()) {
                        updateBestTour(newTour, newTour2);
                    } else {
                        if (newTour2.getPoints().get(0).getArrivalTime().isBefore(newTour.getPoints().get(0).getArrivalTime())) {
                            updateBestTour(newTour, newTour2);
                        } else if (!newTour2.getPoints().get(0).getArrivalTime().isAfter(newTour.getPoints().get(0).getArrivalTime())) {
                            if (newTour2.getCost() < newTour.getCost()) {
                                updateBestTour(newTour, newTour2);
                            }
                        }
                    }

                }
            }
        }
        updateBestTour(tour, newTour);
        return addRequestSuccessfully;
    }

    private void updateBestTour(Tour newTour, Tour newTour2) {
        newTour.setIntersections(newTour2.getIntersections());
        newTour.setCost(newTour2.getCost());
        newTour.setDepartureTime(newTour2.getDepartureTime());
        newTour.setMatAdj(newTour2.getMatAdj());
        newTour.setWarehouse(newTour2.getWarehouse());
        newTour.setDestinations(newTour2.getDestinations());
        newTour.setPoints(newTour2.getPoints());
    }

    public boolean addDeliveryPointDuringTour(Tour tour, PlanningRequest planning, MapIF map, List<Pair<Point, Point>> haveWaitingTimes) {
        if (haveWaitingTimes.isEmpty()) {
            return false;
        }

        Point newDelivery = planning.getRequests().getLast().getDeliveryPoint();

        for (Pair<Point, Point> p : haveWaitingTimes) {
            Point start = p.getKey();
            Point destination = p.getValue();

            if (this.checkIfAddableBetween(map, tour, start, destination, newDelivery)) {
                return true;
            }

            // delete pair from list with potential spaces for adding request
            haveWaitingTimes.remove(p);
        }
        return false;
    }

    /**
     * @param map         contains Intersections and Segments
     * @param tour        contains current tour data
     * @param start       first point
     * @param destination second point
     * @param newPoint    to be added between start and destination
     * @return true if there is enough time to add newPoint between start and destination, false otherwise
     */
    private boolean checkIfAddableBetween(MapIF map, Tour tour, Point start, Point destination, Point newPoint) {
        LocalTime departureTime = start.getDepartureTime();
        LocalTime arrivalTime = destination.getArrivalTime();

        Duration difference = Duration.between(departureTime, arrivalTime);
        int timeToTravel = (int) difference.getSeconds();

        int timeStartToNewPickup = tour.getSecondsForPath(map, start.getAddress(), newPoint.getAddress());
        int timeNewpickupToDestination = tour.getSecondsForPath(map, newPoint.getAddress(), destination.getAddress());

        int newTimeToTravel = timeStartToNewPickup + newPoint.getDuration() + timeNewpickupToDestination;

        if (newTimeToTravel <= timeToTravel) {
            tour.addPointBetween(start, destination, newPoint);
            tour.updateDestinationsByPoints();

            // update timetable
            newPoint.setArrivalTime(start.getDepartureTime().plusSeconds(timeStartToNewPickup));
            newPoint.setDepartureTime(newPoint.getArrivalTime().plusSeconds(newPoint.getDuration()));
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param tour     contains planned tour
     * @param planning contains requests
     * @param map      contains Intersections and Segments
     * @return list with pairs of points between them driver has waiting times
     * (= potential spaces for adding request)
     */
    private List<Pair<Point, Point>> isTimeLeftDuringDeliveries(Tour tour, PlanningRequest planning, MapIF map, List<Pair<Point, Point>> haveWaitingTimes) {
        for (int i = 0; i < tour.getPoints().size() - 1; i++) {
            Point start = tour.getPoints().get(i);
            Point destination = tour.getPoints().get(i + 1);

            LocalTime departureTime = start.getDepartureTime();
            LocalTime arrivalTime = destination.getArrivalTime();

            Duration difference = Duration.between(departureTime, arrivalTime);
            int maxSecondsForPath = (int) difference.getSeconds();

            int actualSecondsForPath = tour.getSecondsForPath(map, start.getAddress(), destination.getAddress());

            if (maxSecondsForPath - actualSecondsForPath > 0) {
                Pair<Point, Point> pair = new Pair<>(start, destination);
                haveWaitingTimes.add(pair);
            }
        }
        return haveWaitingTimes;
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
