package org.hexanome.controller.tsp;

import org.hexanome.model.*;

import java.util.*;

public abstract class TemplateTSP implements TSP {
    private Integer[] bestSol;
    protected Graph g;
    private Double bestSolCost;
    private int timeLimit;
    private long startTime;
    private Map<Long, Map<Long, List<Long>>> shortestPathsIntersections;
    private Map<Long, Map<Long, Double>> shortestPathsCost;
    public Map<Integer, Long> mapIdTSP;
    private MapIF map;
    private Set<Intersection> destinations;
    private PlanningRequest planning;

    public TemplateTSP(
            Map<Integer, Long> mapIdTSP,
            MapIF map,
            Set<Intersection> destinations,
            PlanningRequest planning) {
        this.mapIdTSP = mapIdTSP;
        this.shortestPathsIntersections = map.getShortestPathsIntersections();
        this.shortestPathsCost = map.getShortestPathsCost();
        this.map = map;
        this.destinations = destinations;
        this.planning = planning;
    }

    public void searchSolution(int timeLimit, Graph g, Tour tour) {
        // TODO: delete timeout when calculation can be stopped
        if (timeLimit <= 0) return;
        startTime = System.currentTimeMillis();
        this.timeLimit = timeLimit;
        this.g = g;
        bestSol = new Integer[g.getNbVertices()];
        Collection<Integer> unvisited = new ArrayList<Integer>(g.getNbVertices() - 1);
        for (int i = 1; i < g.getNbVertices(); i++) unvisited.add(i);
        Collection<Integer> visited = new ArrayList<Integer>(g.getNbVertices());
        visited.add(0); // The first visited vertex is 0
        bestSolCost = Double.MAX_VALUE;
        branchAndBound(0, unvisited, visited, Double.valueOf(0), tour);
    }

    public Integer getSolution(int i) {
        if (g != null && i >= 0 && i < g.getNbVertices())
            return bestSol[i];
        return -1;
    }

    public Double getSolutionCost() {
        if (g != null)
            return bestSolCost;
        return Double.valueOf(-1);
    }

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param currentVertex
     * @param unvisited
     * @return a lower bound of the cost of paths in <code>g</code> starting from <code>currentVertex</code>, visiting
     * every vertex in <code>unvisited</code> exactly once, and returning back to vertex <code>0</code>.
     */
    protected abstract Double bound(Integer currentVertex, Collection<Integer> unvisited);

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param currentVertex
     * @param unvisited
     * @param g
     * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
     */
    protected abstract Iterator<Integer> iterator(Integer currentVertex, Collection<Integer> unvisited, Graph g);

    /**
     * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     *
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @param visited       the sequence of vertices that have been already visited (including currentVertex)
     * @param currentCost   the cost of the path corresponding to <code>visited</code>
     */
    private void branchAndBound(int currentVertex, Collection<Integer> unvisited,
                                Collection<Integer> visited, Double currentCost, Tour tour) {
        if (System.currentTimeMillis() - startTime > timeLimit) return;
        if (unvisited.size() == 0) {
            if (g.isArc(currentVertex, 0)) {
                if (currentCost + g.getCost(currentVertex, 0) < bestSolCost) {
                    visited.toArray(bestSol);
                    this.updateTour(tour);
                    bestSolCost = currentCost + g.getCost(currentVertex, 0);
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()) {
                Integer nextVertex = it.next();
                visited.add(nextVertex);
                unvisited.remove(nextVertex);
                Intersection lastIntersection = tour.getLastIntersection();

                branchAndBound(nextVertex, unvisited, visited,
                        currentCost + g.getCost(currentVertex, nextVertex), tour);

                visited.remove(nextVertex);
                unvisited.add(nextVertex);
            }
        }
    }

    /**
     * updates tour if a shorter route is found while continuing the calculation
     *
     * @param tour displayed on map
     */
    public void updateTour(Tour tour) {
        Intersection[] LHSArray = new Intersection[destinations.size()];
        // Converting LinkedHashMap to Array
        LHSArray = destinations.toArray(LHSArray);

        //tour.setDestinations(LHSArray);
        //tour.computeCompleteTour(shortestPathsIntersections,map);

        List<Intersection> pathTSP = new ArrayList<>();
        for (int i = 0; i < bestSol.length; i++) {
            pathTSP.add(LHSArray[this.getSolution(i)]);
        }
        Intersection warehouse = pathTSP.get(0);
        pathTSP.add(warehouse);

        tour.setDestinations(pathTSP);
        tour.setPoints(planning, pathTSP);
        tour.computeCompleteTour(map);
        tour.setCost(this.getSolutionCost());
        tour.updateTiming(map, planning);

        tour.notifyChange("UPDATEMAP");
    }

}
