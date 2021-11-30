package org.hexanome.controller.tsp;

import org.hexanome.model.Intersection;

import java.util.Collection;
import java.util.Iterator;

public class TSP1 extends TemplateTSP {

	private Double[][] costTSP;

	public  TSP1(Double[][] costTSP) {
		this.costTSP = costTSP;
	}

	@Override
	protected Double bound(Integer currentVertex, Collection<Integer> unvisited) {
		Double minCostTSP = Double.MAX_VALUE;
		Integer minUnvisited = 0;
		for (Integer i : unvisited) {
			if(costTSP[currentVertex][i] < minCostTSP) {
				minUnvisited = i;
				minCostTSP = costTSP[currentVertex][i];
			}
		}
		return costTSP[currentVertex][minUnvisited];
	}

	@Override
	protected Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g) {
		return new SeqIter(unvisited, currentVertex, g);
	}

}
