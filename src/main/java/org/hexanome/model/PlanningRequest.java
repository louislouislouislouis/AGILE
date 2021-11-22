package org.hexanome.model;

import java.util.LinkedList;

public class PlanningRequest {
    public Warehouse warehouse;
    public LinkedList<Request> requests;

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

    public void addRequest(Request request){
        requests.add(request);
    }

    @Override
    public String toString() {
        return "PlanningRequest{" +
                "\nwarehouse=" + warehouse +
                "\n, requests=" + requests +
                "\n}";
    }
}
