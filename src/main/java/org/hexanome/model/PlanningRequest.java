package org.hexanome.model;

import java.util.LinkedList;

public class PlanningRequest {
    public Warehouse warehouse;
    public LinkedList<Request> requests;

    public PlanningRequest(Warehouse warehouse, LinkedList<Request> requests) {
        this.warehouse = warehouse;
        this.requests = requests;
    }
}
