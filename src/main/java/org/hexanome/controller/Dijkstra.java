package org.hexanome.controller;

import org.hexanome.model.Intersection;
import org.hexanome.model.Node;
import org.hexanome.model.Segment;

import java.util.*;

public class Dijkstra {

    private Map<Long,Double> dist; // idIntersection - currentDist
    private Map<Long,List<Intersection>> path; // idDestinationIntersection - shortestPathToIntersection
    private Set<Long> settled; // idIntersection already explored
    private PriorityQueue<Node> pq; // representing Intersections
    private int numberOfIntersections;
    private Map<Intersection,Map<Intersection,Segment>> adj;  // startIntersection - destinationIntersection - Segment between them

    Dijkstra(int numberOfIntersections) {
        this.numberOfIntersections = numberOfIntersections;
        this.dist = new HashMap<>();
        this.path = new HashMap<>();
        this.settled = new HashSet<>();
        this.pq = new PriorityQueue<>(numberOfIntersections, new Node());
    }

    /**
     * calculates the shortest paths from a given originIntersection to all given destinationIntersections
     * @param intersections amount of all intersections in the map
     * @param segments amount of all segments in the map
     * @param origin origin intersection
     * @param destinations list of destinations intersections
     * @return map with key: idDestinationIntersection,
     *         value: list of intersections from originIntersection to destinationIntersection
     */
    public Map<Long,List<Intersection>> dijkstra(Map<Long,Intersection> intersections, Map<String,Segment> segments,
                         Intersection origin, List<Intersection> destinations) {

        this.adj = getAdj(intersections, segments); // overview over intersections and their neighbours

        List<Intersection> initialPath = new ArrayList<>();
        for (Intersection i : intersections.values()) {
            if (i.getIdIntersection() == origin.getIdIntersection()) {
                this.dist.put(i.getIdIntersection(), (double) 0); // startIntersection has currentWeight 0
                initialPath.add(origin);
                this.path.put(i.getIdIntersection(),initialPath); // add startIntersection
                initialPath.remove(origin);
            }
            else {
                this.dist.put(i.getIdIntersection(),(double) Integer.MAX_VALUE); // other intersections (not start)
                this.path.put(i.getIdIntersection(), initialPath); // no path to go to intersections
            }
        }

        this.pq.add(new Node(origin.getIdIntersection(), (double) 0)); // add startIntersection to pq

        while (settled.size() != numberOfIntersections) {
            if (pq.isEmpty()) {
                return getResultList(destinations);
            }

            Long idNode = pq.remove().getId();
            if (settled.contains(idNode)) {
                continue;
            }
            settled.add(idNode);
            Intersection currentNode = intersections.get(idNode);
            exploreNeighbours(currentNode);
        }
        return getResultList(destinations);
    }

    /**
     *
     * @param intersections amount of all intersections in the map
     * @param segments amount of all segments in the map
     * @return map with key: startIntersection,
     *         value: map with key: neighbourIntersection,
     *                value: segment between startIntersection and neighbourIntersection
     */
    public Map<Intersection,Map<Intersection,Segment>> getAdj(Map<Long,Intersection> intersections, Map<String,Segment> segments) {
        Map<Intersection,Map<Intersection,Segment>> adj = new HashMap<>();
        for (Intersection i: intersections.values()) {
            adj.put(i,null);
        }
        for (Segment s: segments.values()) {
            adj.get(s.getOriginIntersection()).put(s.getDestinationIntersection(),s);
        }
        return adj;
    }

    /**
     *
     * @param currentNode intersection whose neighbours are going to be explored
     */
    public void exploreNeighbours(Intersection currentNode) {
        Double edgeDistance;
        Double newDistance;

        for (Intersection i: adj.get(currentNode).keySet()) {
            if (!settled.contains(i.getIdIntersection())) {
                edgeDistance = adj.get(currentNode).get(i).getLength();
                newDistance = dist.get(currentNode.getIdIntersection()) + edgeDistance;
                if (newDistance < dist.get(currentNode.getIdIntersection())) {
                    dist.replace(currentNode.getIdIntersection(),newDistance);
                    List<Intersection> newPath = path.get(currentNode.getIdIntersection());
                    newPath.add(i);
                    path.replace(i.getIdIntersection(),newPath);
                }
                pq.add(new Node(i.getIdIntersection(),dist.get(i.getIdIntersection())));
            }
        }
    }

    /**
     *
     * @param destinations list of given destinations
     * @return map with key: idDestinationIntersection,
     *         value: list of intersections from origin to destination
     */
    public Map<Long,List<Intersection>> getResultList(List<Intersection> destinations) {
        Map<Long, List<Intersection>> result = new HashMap<>();
        List<Intersection> list = new ArrayList<>();
        for (Intersection i : destinations) {
            // check if path ends at destination intersection i
            // otherwise path is not complete
            if (path.get(i.getIdIntersection()).contains(i)) {
                list = path.get(i.getIdIntersection());
            }
            result.put(i.getIdIntersection(), list);
        }
        return result;
    }
}
