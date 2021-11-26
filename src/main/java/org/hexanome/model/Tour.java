package org.hexanome.model;

import java.util.ArrayList;
import java.util.List;

public class Tour {
    private List<Intersection> intersections = new ArrayList();
    private double cost;

    public Tour(List<Intersection> intersections, float cost) {
        this.intersections = intersections;
        this.cost = cost;
    }

}
