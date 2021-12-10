package org.hexanome.controller.tsp;

import javafx.collections.WeakListChangeListener;
import org.hexanome.model.Request;

import java.util.*;

public class SeqIter implements Iterator<Integer> {
	private Integer[] candidates;
	private int nbCandidates;

	/**
	 * Create an iterator to traverse the set of vertices in <code>unvisited</code> 
	 * which are successors of <code>currentVertex</code> in <code>g</code>
	 * Vertices are traversed in the same order as in <code>unvisited</code>
	 * @param unvisited
	 * @param currentVertex
	 * @param g
	 */
	public SeqIter(Set<Integer> unvisited, int currentVertex, Graph g, Map<Integer, Integer> requestsVertex) {
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			if (g.isArc(currentVertex, s) && isOkOrderPickupDelivery(unvisited, currentVertex, requestsVertex)) {
				candidates[nbCandidates++] = s;
			}
		}
	}
	
	@Override
	public boolean hasNext() {
		return nbCandidates > 0;
	}

	@Override
	public Integer next() {
		nbCandidates--;
		return candidates[nbCandidates];
	}

	@Override
	public void remove() {}

	public boolean isOkOrderPickupDelivery(Set<Integer> unvisited, int currentVertex, Map<Integer, Integer> requestsVertex) {
		if (requestsVertex.containsKey(currentVertex)) {
			if (unvisited.contains(requestsVertex.get(currentVertex))) {
				return false;
			}
		}
		return true;
	}
}
