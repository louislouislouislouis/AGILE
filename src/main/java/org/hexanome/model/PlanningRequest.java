package org.hexanome.model;

import java.util.LinkedList;

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

    /**
     * Clear the planning
     */
    public void clearPlanning() {
        requests.clear();
    }

    @Override
    public String toString() {
        return "PlanningRequest{" +
                "\nwarehouse=\n" + warehouse +
                "\n, requests=\n" + requests +
                "\n}";
    }
}
