package org.hexanome.controller.tsp;

import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Request;

import java.util.*;

public class TSP1 extends TemplateTSP {

    private Double[][] costTSP;
    private LinkedList<Request> requests;


    public TSP1(
            Double[][] costTSP,
            Map<Integer, Long> mapIdTSP,
            LinkedList<Request> requests,
            MapIF map,
            Set<Intersection> destinations,
            PlanningRequest planning) {
        super(mapIdTSP, map, destinations, planning);
        this.costTSP = costTSP;
        this.requests = requests;
    }

    @Override
    protected Double bound(Integer currentVertex, Set<Integer> unvisited) {
        Set<Integer> unvisitedTemp = new HashSet<>(unvisited);
        Integer currentVertexTemp = currentVertex;
        Double minCostCurrent = Double.MAX_VALUE;

        for (Integer i : unvisitedTemp) {
            if (costTSP[currentVertexTemp][i] < minCostCurrent) {
                minCostCurrent = costTSP[currentVertex][i];
            }
        }

        return minCostCurrent;
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Set<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g, super.requestsVertex);
    }

}
