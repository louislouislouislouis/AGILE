package org.hexanome.model;

public class Request {
    private Intersection pickupPoint;
    private Intersection deliveryPoint;
    private int pickupDuration;
    private int deliveryDuration;

    public Request(Intersection pickupPoint, Intersection deliveryPoint, int pickupDuration, int deliveryDuration) {
        this.deliveryPoint = deliveryPoint;
        this.pickupPoint = pickupPoint;
        this.pickupDuration = pickupDuration;
        this.deliveryDuration = deliveryDuration;
    }
}
