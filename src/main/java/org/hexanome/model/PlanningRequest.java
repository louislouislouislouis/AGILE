package org.hexanome.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Object containing a planning of request that was generated from a xml file
 *
 * @author Gastronom'if
 */
public class PlanningRequest {
    public Warehouse warehouse;
    public LinkedList<Request> requests;

    /**
     * Create an empty planning
     */
    public PlanningRequest() {
        requests = new LinkedList<>();
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public LinkedList<Request> getRequests() {
        return requests;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public void removeRequest(Request request) {
        requests.remove(request);
    }

    /**
     * Clear the planning
     */
    public void clearPlanning() {
        requests.clear();
        warehouse = null;
    }

    @Override
    public String toString() {
        return "PlanningRequest{" +
                "\nwarehouse=\n" + warehouse +
                "\n, requests=\n" + requests +
                "\n}";
    }

    /**
     * @param idIntersection to check for
     * @return list with all pickup points followed by all delivery points with given ID
     */
    public List<Point> getPointByIdIntersection(long idIntersection) {
        List<Point> points = new ArrayList<>();
        for (Request r : requests) {
            if (idIntersection == r.getPickupPoint().getAddress().getIdIntersection()) {
                points.add(r.getPickupPoint());
            }
        }
        for (Request r2 : requests) {
            if (idIntersection == r2.getDeliveryPoint().getAddress().getIdIntersection()) {
                points.add(r2.getDeliveryPoint());
            }
        }
        return points;
    }

    public Point getPickupByDelivery(Point delivery) {
        if (!(delivery instanceof DeliveryPoint)) {
            return null;
        }
        for (Request r : requests) {
            if (r.getDeliveryPoint().equals(delivery)) {
                return r.getPickupPoint();
            }
        }
        return null;
    }
}
