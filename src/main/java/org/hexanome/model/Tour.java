package org.hexanome.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private List<Intersection> intersections = new ArrayList();
    private Double cost;

    public Tour(){

    }
    
    public Tour(List<Intersection> intersections, Double cost) {
        this.intersections = intersections;
        this.cost = cost;
    }

}
