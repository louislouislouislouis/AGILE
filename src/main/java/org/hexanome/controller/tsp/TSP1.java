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
            Map<Integer, Long> mapIdTSP, LinkedList<Request> requests,
            MapIF map,
            Set<Intersection> destinations,
            PlanningRequest planning) {
        super(mapIdTSP, map, destinations, planning);
        this.costTSP = costTSP;
        this.requests = requests;
    }

    @Override
    protected Double bound(Integer currentVertex, Collection<Integer> unvisited) {
        return 0.0;
		/*
		Double minCostTSP = Double.MAX_VALUE;
		Integer minUnvisited = 0;
		for (Integer i : unvisited) {
			if(costTSP[currentVertex][i] < minCostTSP) {
				minUnvisited = i;
				minCostTSP = costTSP[currentVertex][i];
			}
		}
		return costTSP[currentVertex][minUnvisited];
		 */
    }

    @Override
    protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
        return new SeqIter(unvisited, currentVertex, g, super.mapIdTSP, requests);
    }

}
