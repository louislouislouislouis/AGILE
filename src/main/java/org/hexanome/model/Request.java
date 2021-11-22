package org.hexanome.model;

public class Request {
    private PickupPoint pickupPoint;
    private DeliveryPoint deliveryPoint;

    public Request(PickupPoint pickupPoint, DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
        this.pickupPoint = pickupPoint;
    }
}
