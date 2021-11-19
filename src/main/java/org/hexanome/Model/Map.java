package org.hexanome.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class Map {
    private HashMap<Long, Intersection> intersections;
    private HashMap<String, Segment> segments;

    public Map(HashMap<Long, Intersection> intersections, HashMap<String, Segment> segments){
        this.intersections = intersections;
        this.segments = segments;
    }
}
