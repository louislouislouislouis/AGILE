package org.hexanome.model;

import org.hexanome.controller.MainsScreenController;

import java.time.LocalTime;
import java.util.*;

public class Tour extends Observable {


    private List<Intersection> intersections = new ArrayList();
    private Double cost;
    private LocalTime departureTime;
    private Map<Intersection, Map<Intersection, Segment>> MatAdj;
    private Intersection warehouse;
    private List<Intersection> destinations = new ArrayList<>();

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
    public Tour(List<Intersection> intersections, Double cost, Observer o, LocalTime departureTime,Map<Intersection, Map<Intersection, Segment>> MatAdj  ) {
        this.addObserver(o);
        this.intersections = intersections;
        this.cost = cost;
        this.MatAdj = MatAdj;
        this.departureTime= departureTime;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
    }

    public void addIntersection(Intersection intersection) {
        //this.cost += this.intersections.get(this.intersections.size()-1).
        this.intersections.add(intersection);

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

    public Intersection getLastIntersection() {
        if (intersections.size() == 0) {
            return null;
        }
        return this.intersections.get(intersections.size() - 1);
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
     * @param map contains all Intersections and Segments and shortestPathsCost
     */
    public void calculateCost(MapIF map) {
        Map<Long, Map<Long, Double>> shortestPathsCost = map.getShortestPathsCost();
        double cost = 0;
        for (int i = 0; i < intersections.size()-1; i++) {
            Intersection startIntersection = intersections.get(i);
            Intersection destinationIntersection = intersections.get(i+1);
            cost += shortestPathsCost.get(startIntersection.getIdIntersection()).get(destinationIntersection.getIdIntersection());
        }
        this.cost = cost;
    }

    public Intersection getWarehouse() {
        return this.warehouse;
    }

    public void removeLastDestination() {
        this.destinations.remove(this.destinations.size()-1);
    }
}
