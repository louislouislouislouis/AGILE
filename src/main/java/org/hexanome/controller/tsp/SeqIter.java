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
	public SeqIter(Collection<Integer> unvisited, int currentVertex, Graph g, Map<Integer,Long> mapIdTSP, LinkedList<Request> requests){
		this.candidates = new Integer[unvisited.size()];
		for (Integer s : unvisited){
			if (g.isArc(currentVertex, s) && isOkOrderPickupDelivery(unvisited, currentVertex, g, mapIdTSP, requests))
				candidates[nbCandidates++] = s;
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

	public boolean isOkOrderPickupDelivery(Collection<Integer> unvisited, int currentVertex, Graph g, Map<Integer, Long> mapIdTSP, LinkedList<Request> requests) {
		if (isDelivery(currentVertex, mapIdTSP, requests)) {
			for (Integer i : getPickupByDelivery(currentVertex, mapIdTSP, requests)) {
				if (unvisited.contains(i)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isDelivery(int currentVertex, Map<Integer,Long> mapIdTSP, LinkedList<Request> requests) {
		for (Request r : requests) {
			if (r.getDeliveryPoint().getAddress().getIdIntersection() == mapIdTSP.get(currentVertex)) {
				return true;
			}
		}
		return false;
	}

	public LinkedList<Integer> getPickupByDelivery(Integer deliveryVertex, Map<Integer,Long> mapIdTSP, LinkedList<Request> requests) {
		Long idDelivery = mapIdTSP.get(deliveryVertex);
		LinkedList<Long> idPickupPoints = new LinkedList<>();
		for (Request r : requests) {
			if (r.getDeliveryPoint().getAddress().getIdIntersection() == idDelivery) {
				idPickupPoints.add(r.getDeliveryPoint().getAddress().getIdIntersection());
			}
		}
		LinkedList<Integer> pickupVertexes = new LinkedList<>();
		Map<Long,Integer> mapVertexTSP = new HashMap<>();
		for (Integer i : mapIdTSP.keySet()) {
			mapVertexTSP.put(mapIdTSP.get(i), i);
		}
		for (Long l : idPickupPoints) {
			pickupVertexes.add(mapVertexTSP.get(l));
		}
		return  pickupVertexes;
	}



}
