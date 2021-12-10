package org.hexanome.controller.tsp;

import org.hexanome.controller.MainsScreenController;
import org.hexanome.model.*;

import java.util.*;

public abstract class TemplateTSP implements TSP {
    private Integer[] bestSol;
    protected Graph g;
    private Double bestSolCost;
    public Map<Integer, Long> mapIdTSP;
    public Map<Long, Integer> mapVertexTSP;
    private MapIF map;
    private Set<Intersection> destinations;
    private PlanningRequest planning;

    // key: vertex delivery, value: vertex pickup
    public Map<Integer, Integer> requestsVertex;

    public TemplateTSP(
            Map<Integer, Long> mapIdTSP,
            MapIF map,
            Set<Intersection> destinations,
            PlanningRequest planning) {
        this.mapIdTSP = mapIdTSP;
        this.mapVertexTSP = new HashMap<>();
        for (Integer i : mapIdTSP.keySet()) {
            mapVertexTSP.put(mapIdTSP.get(i), i);
        }
        this.map = map;
        this.destinations = destinations;
        this.planning = planning;
        this.requestsVertex = new HashMap<>();
        for (Request r : this.planning.requests) {
            requestsVertex.put(mapVertexTSP.get(r.getDeliveryPoint().getId()), mapVertexTSP.get(r.getPickupPoint().getId()));
        }
    }

    /**
     * @param g
     * @param tour
     * @param allowCalculation
     */
    public void searchSolution(Graph g, Tour tour, MainsScreenController allowCalculation) {
        this.g = g;
        bestSol = new Integer[g.getNbVertices()];
        bestSolCost = Double.MAX_VALUE;

        Set<Integer> unvisited = new HashSet<>();
        for (int i = 1; i < g.getNbVertices(); i++) unvisited.add(i);

        LinkedList<Integer> visited = new LinkedList<>();
        visited.add(0); // The first visited vertex is 0

        branchAndBound(0, unvisited, visited, Double.valueOf(0), tour, allowCalculation);

        for (Integer i : bestSol) {
            System.out.print(i + " ");
        }
        System.out.println(bestSolCost);
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
    protected abstract Double bound(Integer currentVertex, Set<Integer> unvisited);

    /**
     * Method that must be defined in TemplateTSP subclasses
     *
     * @param currentVertex
     * @param unvisited
     * @param g
     * @return an iterator for visiting all vertices in <code>unvisited</code> which are successors of <code>currentVertex</code>
     */
    protected abstract Iterator<Integer> iterator(Integer currentVertex, Set<Integer> unvisited, Graph g);

    /**
     * Template method of a branch and bound algorithm for solving the TSP in <code>g</code>.
     *
     * @param currentVertex the last visited vertex
     * @param unvisited     the set of vertex that have not yet been visited
     * @param visited       the sequence of vertices that have been already visited (including currentVertex)
     * @param currentCost   the cost of the path corresponding to <code>visited</code>
     */
    private void branchAndBound(int currentVertex,
                                Set<Integer> unvisited,
                                LinkedList<Integer> visited,
                                Double currentCost,
                                Tour tour,
                                MainsScreenController allowCalculation) {
        if (!allowCalculation.isAllowcalculation() && tour.getIntersections().size() > 1) {
            return;
        }
        if (unvisited.isEmpty()) {
            if (g.isArc(currentVertex, 0)) {
                if (currentCost + g.getCost(currentVertex, 0) < bestSolCost) {
                    visited.toArray(bestSol);
                    bestSolCost = currentCost + g.getCost(currentVertex, 0);
                    this.updateTour(tour);
                }
            }
        } else if (currentCost + bound(currentVertex, unvisited) < bestSolCost) {
            Iterator<Integer> it = iterator(currentVertex, unvisited, g);
            while (it.hasNext()) {
                Integer nextVertex = it.next();
                visited.add(nextVertex);
                unvisited.remove(nextVertex);

                branchAndBound(nextVertex, unvisited, visited,
                        currentCost + g.getCost(currentVertex, nextVertex), tour, allowCalculation);

                visited.removeLast();
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
