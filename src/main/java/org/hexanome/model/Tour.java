package org.hexanome.model;

import java.time.LocalTime;
import java.util.*;

/**
 * Object containing a computed list of request
 *
 * @author Gastronom'if
 */
public class Tour extends Observable {


    private List<Intersection> intersections = new ArrayList();
    private Double cost;
    private LocalTime departureTime;
    private Map<Intersection, Map<Intersection, Segment>> MatAdj;
    private Intersection warehouse;
    private List<Intersection> destinations = new ArrayList<>();
    private List<Point> points = new ArrayList<>();

    @Override
    public String toString() {
        return "Tour{" +
                "intersections=" + intersections +
                ", cost=" + cost +
                ", departureTime=" + departureTime +
                ", MatAdj=" + MatAdj +
                '}';
    }

    /**
     * create an empty tour
     */
    public Tour() {
    }

    /**
     * create a tour
     *
     * @param intersections list of all intersections of the tour
     * @param cost          calculated cost of the tour
     */
    public Tour(List<Intersection> intersections, Double cost) {
        this.intersections = intersections;
        this.cost = cost;
    }

    /**
     * create a tour
     *
     * @param o             Observer from the design pattern
     * @param intersections list of all intersections of the tour
     * @param cost          calculated cost of the tour
     */
    public Tour(List<Intersection> intersections, Double cost, Observer o, LocalTime departureTime, Map<Intersection, Map<Intersection, Segment>> MatAdj) {
        this.addObserver(o);
        this.intersections = intersections;
        this.cost = cost;
        this.MatAdj = MatAdj;
        this.departureTime = departureTime;
    }

    public Tour(List<Intersection> intersections, Double cost, LocalTime departureTime, Map<Intersection, Map<Intersection, Segment>> matAdj, Intersection warehouse, List<Intersection> destinations, List<Point> points) {
        this.intersections = intersections;
        this.cost = cost;
        this.departureTime = departureTime;
        MatAdj = matAdj;
        this.warehouse = warehouse;
        this.destinations = destinations;
        this.points = points;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void addIntersection(Intersection intersection) {
        this.intersections.add(intersection);

    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public Map<Intersection, Map<Intersection, Segment>> getMatAdj() {
        return MatAdj;
    }

    public void setMatAdj(Map<Intersection, Map<Intersection, Segment>> matAdj) {
        MatAdj = matAdj;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public void notifyChange(String msg) {
        setChanged();
        notifyObservers(msg);
    }

    public List<Intersection> getIntersections() {
        return intersections;
    }

    public Double getCost() {
        return cost;
    }

    public void setWarehouse(Intersection warehouse) {
        this.warehouse = warehouse;
    }

    public void addDestination(Intersection destination) {
        this.destinations.add(destination);
    }

    public void setDestinations(List<Intersection> path) {
        List<Intersection> newDestinations = new ArrayList<>();
        for (Intersection i : path) {
            newDestinations.add(i);
        }
        this.destinations = newDestinations;
    }

    public List<Intersection> getDestinations() {
        return this.destinations;
    }

    /**
     * completes list of all intersections to visit during the tour
     *
     * @param map contains all Intersections and Segments and shortestPathsIntersections
     */
    public void computeCompleteTour(MapIF map) {
        Map<Long, Map<Long, List<Long>>> shortestPathsIntersections = map.getShortestPathsIntersections();
        List<Intersection> completeTour = new ArrayList<>();
        completeTour.add(warehouse);
        for (int i = 0; i < destinations.size() - 1; i++) {
            Intersection startIntersection = destinations.get(i);
            Intersection destinationIntersection = destinations.get(i + 1);
            for (Long l : shortestPathsIntersections.get(startIntersection.getIdIntersection()).get(destinationIntersection.getIdIntersection())) {
                Intersection intersection = map.getIntersections().get(l);
                if (!intersection.equals(destinations.get(i))) {
                    completeTour.add(intersection);
                }
            }
        }
        this.setIntersections(completeTour);
    }

    /**
     * sum of cost of all visited Segments
     *
     * @param map contains all Intersections and Segments and shortestPathsCost
     */
    public void calculateCost(MapIF map) {
        Map<Long, Map<Long, Double>> shortestPathsCost = map.getShortestPathsCost();
        double cost = 0;
        for (int i = 0; i < destinations.size() - 1; i++) {
            Intersection startIntersection = destinations.get(i);
            Intersection destinationIntersection = destinations.get(i + 1);
            System.out.println("startIntersection: " + startIntersection.getIdIntersection());
            System.out.println("destinationIntersection: " + destinationIntersection.getIdIntersection());
            System.out.println("OK");
            cost += shortestPathsCost.get(startIntersection.getIdIntersection()).get(destinationIntersection.getIdIntersection());
        }
        this.cost = cost;
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public void removeLastDestination() {
        this.destinations.remove(this.destinations.size() - 1);
    }


    /**
     * remove Request from Tour
     *
     * @param request to remove
     * @param map     contains Intersections and Segments
     */
    public void removeRequest(Request request, MapIF map) {
        Intersection pickUpPoint = request.getPickupPoint().getAddress();
        destinations.remove(pickUpPoint);
        Intersection deliveryPoint = request.getDeliveryPoint().getAddress();
        destinations.remove(deliveryPoint);

        points.remove(request.getPickupPoint());
        points.remove(request.getDeliveryPoint());

        this.computeCompleteTour(map);
        this.calculateCost(map);
    }

    /**
     * @param map         contains all Intersections and Segments
     * @param start       startIntersection
     * @param destination destinationIntersection
     * @return time for going from a given startIntersection to a given destinationIntersection in seconds
     */
    public int getSecondsForPath(MapIF map, Intersection start, Intersection destination) {
        List<Long> intersectionsFromStartToDestination = map.getShortestPathsIntersections().get(start.getIdIntersection()).get(destination.getIdIntersection());
        List<Segment> segmentsFromStartToDestination = new ArrayList<>();
        for (int i = 0; i < intersectionsFromStartToDestination.size() - 1; i++) {
            Long idCurrentStart = intersectionsFromStartToDestination.get(i);
            Intersection currentStart = map.getIntersections().get(idCurrentStart);
            Long idCurrentDestination = intersectionsFromStartToDestination.get(i + 1);
            Intersection currentDestination = map.getIntersections().get(idCurrentDestination);

            segmentsFromStartToDestination.add(map.getMatAdj().get(currentStart).get(currentDestination));
        }
        int time = 0;
        for (Segment s : segmentsFromStartToDestination) {
            time += s.getDuration();
        }
        return time;
    }

    public void addPoint(Point point) {
        this.points.add(point);
    }

    public void setPoints(PlanningRequest planning, List<Intersection> path) {
        List<Point> newPoints = new ArrayList<>();
        newPoints.add(planning.getWarehouse());
        for (Intersection i : path) {
            this.addNewPoint(i, planning, newPoints);
        }
        this.points = newPoints;
    }

    /**
     * helper method for setPoints for the case that there are multiple points at the same intersection
     * it will first add the pickup points, then the delivery points
     * for the delivery points the corresponding pickupPoint must already be in the list
     *
     * @param i         current Intersection
     * @param planning  contains all requests
     * @param newPoints list of points in tour
     */
    private void addNewPoint(Intersection i, PlanningRequest planning, List<Point> newPoints) {
        for (Point p : planning.getPointByIdIntersection(i.getIdIntersection())) {
            if (!newPoints.contains(p)) {
                if (!(p instanceof DeliveryPoint)) {
                    newPoints.add(p);
                    return;
                } else {
                    Point correspondingPickUpPoint = planning.getPickupByDelivery(p);
                    if (newPoints.contains(correspondingPickUpPoint)) {
                        newPoints.add(p);
                        return;
                    }
                }
            }
        }
    }

    /**
     * updating arrival and departure times for all points
     *
     * @param map      contains Intersections and Segments
     * @param planning contains requests
     */
    public void updateTiming(MapIF map, PlanningRequest planning) {
        Point warehouse = points.get(0);
        warehouse.setDepartureTime(this.departureTime);
        points.add(warehouse);
        for (int i = 0; i < points.size() - 1; i++) {
            this.setTimings(map, i);
        }
        int i = points.size();
        points.remove(i - 1);
    }

    /**
     * calculate arrival and departure time for next point i+1
     *
     * @param map contains Intersections and Segments
     * @param i   current point
     */
    private void setTimings(MapIF map, int i) {
        Point start = points.get(i);
        Point destination = points.get(i + 1);
        int secondsForPath = this.getSecondsForPath(map, start.getAddress(), destination.getAddress());
        LocalTime newArrivalTime = start.getDepartureTime().plusSeconds(secondsForPath);
        destination.setArrivalTime(newArrivalTime);
        // we don't want to overwrite the departure time from the warehouse
        if (!(destination instanceof Warehouse)) {
            LocalTime newDepartureTime = destination.getArrivalTime().plusSeconds(destination.getDuration());
            destination.setDepartureTime(newDepartureTime);
        }
    }

    /**
     * updates arrival and departure times for added request in the tour,
     * update arrival time warehouse
     * all others points keep their arrival and departure times that have already been calculated
     *
     * @param map      contains Intersections and Segments
     * @param pickUp   of new request
     * @param delivery of new request
     */
    public void updateTimingForNewRequest(MapIF map, Point pickUp, Point delivery) {
        int indexPickup = points.indexOf(pickUp);
        int indexDelivery = points.indexOf(delivery);
        this.setTimings(map, indexPickup - 1);
        this.setTimings(map, indexDelivery - 1);
        this.updateTimingWarehouse(map);
    }

    /**
     * updates timing only for new request and warehouse
     *
     * @param map           contains Intersections and Segments
     * @param deliveryPoint point to update timing
     */
    public void updateTimingForNewDestination(MapIF map, DeliveryPoint deliveryPoint) {
        points.add(deliveryPoint);
        points.add(points.get(0)); //add warehouse as last point
        for (int i = points.size() - 3; i < points.size() - 1; i++) {
            this.setTimings(map, i);
        }
        points.remove(points.size() - 1); // remove last warehouse
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * @param start       where to come from
     * @param destination where to go
     * @param newPoint    to add between start and destination
     */
    public void addPointBetween(Point start, Point destination, Point newPoint) {
        List<Point> newPoints = new ArrayList<>();
        for (int i = 0; i < points.size() - 1; i++) {
            if (points.get(i).equals(start) && points.get(i + 1).equals(destination)) {
                for (int j = 0; j <= i; j++) {
                    newPoints.add(points.get(j));
                }
                newPoints.add(newPoint);
                for (int j = i + 1; j < points.size(); j++) {
                    newPoints.add(points.get(j));
                }
                this.points = newPoints;
                return;
            }
        }
    }

    /**
     * updating list destinations by list points
     */
    public void updateDestinationsByPoints() {
        List<Intersection> newDestinations = new ArrayList<>();
        for (Point p : points) {
            newDestinations.add(p.getAddress());
        }
        newDestinations.add(points.get(0).getAddress()); //adding warehouse as last intersection
        this.destinations = newDestinations;
    }

    public void deleteLastPoint() {
        points.remove(points.size() - 1);
    }

    /**
     * updates arrivalTime for Warehouse
     *
     * @param map contains Intersections and Segments
     */
    public void updateTimingWarehouse(MapIF map) {
        points.add(points.get(0)); //add warehouse as last point
        this.setTimings(map, points.size() - 2);
        points.remove(points.size() - 1); // remove last warehouse
    }
}
