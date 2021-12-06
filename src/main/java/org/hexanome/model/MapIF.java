package org.hexanome.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MapIF {
    private HashMap<Long, Intersection> intersections;
    private HashMap<UUID, Segment> segments;
    private Map<Intersection, Map<Intersection, Segment>> MatAdj;
    // shortestPathsIntersections Map with
    // key: idStartIntersection
    // value: List of Maps with key: idDestinationIntersection
    // value: List idIntersections to pass from start to destination
    Map<Long, Map<Long, List<Long>>> shortestPathsIntersections = new HashMap<>();

    // shortestPathsIntersections Map with
    // key: idStartIntersection
    // value: List of Maps with key: idDestinationIntersection
    // value: cost of complete shortest path between start and destination
    Map<Long, Map<Long, Double>> shortestPathsCost = new HashMap<>();


    public Map<Intersection, Map<Intersection, Segment>> getMatAdj() {
        return MatAdj;
    }

    /**
     * create a map
     *
     * @param intersections list of all intersections of the map
     * @param segments      list of all segments of the map
     */
    public MapIF(HashMap<Long, Intersection> intersections, HashMap<UUID, Segment> segments) {
        this.intersections = intersections;
        this.segments = segments;
    }

    /**
     * create an empty map
     */
    public MapIF() {
        this.intersections = new HashMap<>();
        this.segments = new HashMap<>();
    }

    /**
     * add a segment to the map
     *
     * @param segment segment to be added
     * @param id      id that will help recognize the segment
     */
    public void addSegment(UUID id, Segment segment) {
        this.segments.put(id, segment);
    }

    /**
     * add an intersection to the map
     *
     * @param intersection intersection to be added
     */
    public void addIntersection(Intersection intersection) {
        this.intersections.put(intersection.getIdIntersection(), intersection);
    }

    /**
     * clear all the segments and the intersections of the map
     */
    public void clearMap() {
        this.intersections.clear();
        ;
        this.segments.clear();
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
                "\nintersections=\n" + intersections +
                "\n,segments=\n" + segments +
                "\n}";
    }

    public void setAdj() {
        Map<Intersection, Map<Intersection, Segment>> adj = new HashMap<>();
        for (Intersection i : this.getIntersections().values()) {
            Map<Intersection, Segment> emptyMap = new HashMap<>();
            adj.put(i, emptyMap);
        }
        for (Segment s : this.getSegments().values()) {
            adj.get(s.getOriginIntersection()).put(s.getDestinationIntersection(), s);
        }
        this.MatAdj=adj;
    }

    public Map<Long, Map<Long, List<Long>>> getShortestPathsIntersections() {
        return shortestPathsIntersections;
    }

    public Map<Long, Map<Long, Double>> getShortestPathsCost() {
        return shortestPathsCost;
    }

    public void setShortestPathsIntersections(Map<Long, Map<Long, List<Long>>> shortestPathsIntersections) {
        this.shortestPathsIntersections = shortestPathsIntersections;
    }

    public void setShortestPathsCost(Map<Long, Map<Long, Double>> shortestPathsCost) {
        this.shortestPathsCost = shortestPathsCost;
    }
}
