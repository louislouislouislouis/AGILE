package org.hexanome.model;

import org.hexanome.controller.MainsScreenController;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class Tour extends Observable {
    private List<Intersection> intersections = new ArrayList();
    private Double cost;

    public Tour() {

    }


    public Tour(List<Intersection> intersections, Double cost) {
        this.intersections = intersections;
        this.cost = cost;
    }

    public Tour(List<Intersection> intersections, Double cost, Observer o) {
        this.addObserver(o);
        this.intersections = intersections;
        this.cost = cost;
    }

    public void setIntersections(List<Intersection> intersections) {
        this.intersections = intersections;
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
}
