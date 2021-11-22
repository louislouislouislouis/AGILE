package org.hexanome.controller;

import org.hexanome.model.Intersection;
import org.hexanome.model.Node;
import org.hexanome.model.Segment;
import org.hexanome.model.MapIF;

import java.util.*;

public class Dijkstra {

    private Map<Long,Double> dist; // idIntersection - currentDist
    private Map<Long,List<String>> path; // idIntersection - shortestPathToIntersection
    private Set<Long> settled; // idIntersection
    private PriorityQueue<Node> pq; // representing Intersections
    private int numberOfIntersections;
    private Map<Intersection,Map<Intersection,Segment>> adj;  // startIntersection - destinationIntersection - Segment between them

    Dijkstra(int numberOfIntersections) {
        this.numberOfIntersections = numberOfIntersections;
        dist = new HashMap<>();
        path = new HashMap<>();
        settled = new HashSet<>();
        pq = new PriorityQueue<>(numberOfIntersections, new Node());
    }

    public List<String> dijkstra(Map<Long,Intersection> intersections, Map<String,Segment> segments,
                         Intersection origin, Intersection destination) {
        this.adj = getAdj(intersections, segments);

        List<String> segmentNames = new ArrayList<>();

        for (Intersection i : intersections.values()) {
            if (i.getIdIntersection() == origin.getIdIntersection()) {
                dist.put(i.getIdIntersection(), (double) 0);
            }
            else {
                dist.put(i.getIdIntersection(),(double) Integer.MAX_VALUE);
            }
            path.put(i.getIdIntersection(), segmentNames);
        }

        pq.add(new Node(origin.getIdIntersection(), (double) 0));

        while (settled.size() != numberOfIntersections) {
            if (pq.isEmpty()) {
                return path.get(destination.getIdIntersection());
            }
            if (settled.contains(destination.getIdIntersection())) {
                boolean finished = true;
                for (Long l : dist.keySet()) {
                    if (dist.get(l) < dist.get(destination.getIdIntersection())) {
                        finished = false;
                    }
                }
                if (finished) {
                    return path.get(destination.getIdIntersection());
                }
            }
            Long idNode = pq.remove().getId();
            if (settled.contains(idNode)) {
                continue;
            }
            settled.add(idNode);
            Intersection currentNode = intersections.get(idNode);
            exploreNeighbours(currentNode);
        }
        return path.get(destination.getIdIntersection());
    }

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

    public void exploreNeighbours(Intersection currentNode) {
        Double edgeDistance;
        Double newDistance;

        for (Intersection i: adj.get(currentNode).keySet()) {
            if (!settled.contains(i.getIdIntersection())) {
                edgeDistance = adj.get(currentNode).get(i).getLength();
                newDistance = dist.get(currentNode.getIdIntersection()) + edgeDistance;
                if (newDistance < dist.get(currentNode.getIdIntersection())) {
                    dist.replace(currentNode.getIdIntersection(),newDistance);
                    List<String> newPath = path.get(currentNode.getIdIntersection());
                    newPath.add(adj.get(currentNode).get(i).getName());
                    path.replace(i.getIdIntersection(),newPath);
                }
                pq.add(new Node(currentNode.getIdIntersection(),dist.get(currentNode.getIdIntersection())));
            }
        }
    }
}
