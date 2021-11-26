package org.hexanome.controller.tsp;

public class CompleteGraph implements Graph {
	private static final int MAX_COST = 40;
	private static final int MIN_COST = 10;
	int nbVertices;
	Double[][] cost;

	public CompleteGraph(int nbVertices, Double[][] cost){
		this.nbVertices = nbVertices;
		this.cost = cost;
	}

	@Override
	public int getNbVertices() {
		return nbVertices;
	}

	@Override
	public Double getCost(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return Double.valueOf(-1);
		return cost[i][j];
	}

	@Override
	public boolean isArc(int i, int j) {
		if (i<0 || i>=nbVertices || j<0 || j>=nbVertices)
			return false;
		return i != j;
	}

}
