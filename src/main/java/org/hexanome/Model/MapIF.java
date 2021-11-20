package org.hexanome.model;

import java.util.HashMap;
import java.util.Map;

public class MapIF {
    private HashMap<Long, Intersection> intersections;
    private HashMap<String, Segment> segments;

    public MapIF(HashMap<Long, Intersection> intersections, HashMap<String, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    @Override
    public String toString() {
        String toReturn = "MapIF{" +
                "intersections=\n";
        for (Map.Entry<Long, Intersection> pair : intersections.entrySet()) {
            toReturn += pair.getValue().toString() + "\n";
        }
        toReturn += ",\nsegments=\n";
        for (Map.Entry<String, Segment> pair : segments.entrySet()) {
            toReturn += pair.getValue().toString() + "\n";
        }
        toReturn += '}';
        return toReturn;
    }
}
