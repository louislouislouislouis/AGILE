package org.hexanome.controller;

import org.hexanome.model.Intersection;
import org.hexanome.model.Node;
import org.hexanome.model.Segment;

import java.util.*;

public class Dijkstra {

    private Map<Long,Double> dist; // idIntersection - currentDist
    private Map<Long,List<Long>> path; // idDestinationIntersection - shortestPathToIntersection
    private Set<Long> settled; // idIntersection already explored
    private PriorityQueue<Node> pq; // representing Intersections
    private int numberOfIntersections;
    private Map<Intersection,Map<Intersection,Segment>> adj;  // startIntersection - destinationIntersection - Segment between them

    public Dijkstra(int numberOfIntersections) {
        this.numberOfIntersections = numberOfIntersections;
        this.dist = new HashMap<>();
        this.path = new HashMap<>();
        this.settled = new HashSet<>();
        this.pq = new PriorityQueue<>(numberOfIntersections, new Node());
    }

    /**
     * calculates the shortest paths from a given originIntersection to all given destinationIntersections
     * @param intersections amount of all intersections in the map
     * @param adj Adjacency matrix
     * @param origin origin intersection
     * @param destinations set of destinations intersections
     * @return map with key: idDestinationIntersection,
     *         value: list of intersections from originIntersection to destinationIntersection
     */
    public Map<Long,List<Long>> dijkstra(
            Map<Long,Intersection> intersections,
            Map<Intersection,Map<Intersection,Segment>> adj,
            Intersection origin,
            Set<Intersection> destinations) {

        this.adj = adj;

        for (Intersection i : intersections.values()) {
            List<Long> initialPath = new ArrayList<>();
            // no path to go to intersections
            if (i.getIdIntersection() == origin.getIdIntersection()) {
                this.dist.put(i.getIdIntersection(), (double) 0); // startIntersection has currentWeight 0
                initialPath.add(origin.getIdIntersection());
            }
            else {
                this.dist.put(i.getIdIntersection(),(double) Integer.MAX_VALUE); // other intersections (not start)
            }
            this.path.put(i.getIdIntersection(),initialPath); // add startIntersection
        }

        this.pq.add(new Node(origin.getIdIntersection(), (double) 0)); // add startIntersection to pq

        while (settled.size() != numberOfIntersections) {
            if (pq.isEmpty()) {
                return getResult(destinations);
            }

            Long idNode = pq.remove().getId();
            if (settled.contains(idNode)) {
                continue;
            }
            settled.add(idNode);
            Intersection currentNode = intersections.get(idNode);
            exploreNeighbours(currentNode);
        }
        return getResult(destinations);
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
                if (newDistance < dist.get(i.getIdIntersection())) {
                    dist.replace(i.getIdIntersection(),newDistance);
                    List<Long> newPath = new ArrayList<>();
                    for (Long l : path.get(currentNode.getIdIntersection())) {
                        newPath.add(l);
                    }
                    newPath.add(i.getIdIntersection());
                    path.replace(i.getIdIntersection(),newPath);
                }
                pq.add(new Node(i.getIdIntersection(),dist.get(i.getIdIntersection())));
            }
        }
    }

    /**
     *
     * @param destinations set of given destinations
     * @return map with key: idDestinationIntersection,
     *         value: list of intersections from origin to destination
     */
    public Map<Long,List<Long>> getResult(Set<Intersection> destinations) {
        Map<Long, List<Long>> result = new HashMap<>();
        List<Long> list = new ArrayList<>();
        for (Intersection i : destinations) {
            // check if path ends at destination intersection i
            // otherwise path is not complete
            if (path.get(i.getIdIntersection()).contains(i.getIdIntersection())) {
                list = path.get(i.getIdIntersection());
            }
            result.put(i.getIdIntersection(), list);
        }
        return result;
    }

    public Map<Long, Double> getResultDistForTSP(Set<Intersection> destinations) {
        Map<Long, Double> result = new HashMap<>();

        for (Intersection i : destinations) {
            result.put(i.getIdIntersection(), dist.get(i.getIdIntersection()));
        }
        return result;
    }

    public Map<Long, Double> getDist() {
        return dist;
    }

    public Map<Long, List<Long>> getPath() {
        return path;
    }
}
