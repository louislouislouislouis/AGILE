package org.hexanome.model;

import org.hexanome.controller.MainsScreenController;

import java.time.LocalTime;
import java.util.*;

public class Tour extends Observable {


    private List<Intersection> intersections = new ArrayList();
    private Double cost;
    private LocalTime departureTime;
    private Map<Intersection, Map<Intersection, Segment>> MatAdj;

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

    public void deleteIntersectionsAfter(Intersection lastIntersection) {
        System.out.println("HERE IN DELETE INTERSECTIONS AFTER");
        System.out.println("Aktuelle Tour: ");
        for (Intersection i : intersections) {
            System.out.print(" " + i.getIdIntersection());
        }
        int index = intersections.lastIndexOf(lastIntersection);
        System.out.println("Index last intersection: " + index);
        System.out.println("Anzahl Intersections: " + intersections.size());
        int size = intersections.size();
        for (int i = index + 1; i < size; i++) {
            intersections.remove(index + 1);
            System.out.println("Index gelöscht: " + i);
        }
        System.out.println("delete ist fertig");
    }
}
