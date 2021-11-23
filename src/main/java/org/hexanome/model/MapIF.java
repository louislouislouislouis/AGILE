package org.hexanome.model;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapIF {
    private HashMap<Long, Intersection> intersections;
    private HashMap<UUID, Segment> segments;

    public MapIF(HashMap<Long, Intersection> intersections, HashMap<UUID, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    public MapIF() {
        this.intersections = new HashMap<>();
        this.segments = new HashMap<>();
    }

    public void addSegment(UUID id, Segment segment){
        this.segments.put(id, segment);
    }

    public void addIntersection(Intersection intersection){
        this.intersections.put(intersection.getIdIntersection(), intersection);
    }

    public HashMap<Long, Intersection> getIntersections() {
        return intersections;
    }

    public HashMap<UUID, Segment> getSegments() {
        return segments;
    }

    @Override
    public String toString() {
        return "MapIF{" +
                "\nintersections=\n"+intersections+
                "\n,segments=\n"+segments+
                "\n}";
    }
}
