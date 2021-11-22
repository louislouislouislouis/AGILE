package org.hexanome.model;

public class Request {
    private PickupPoint pickupPoint;
    private DeliveryPoint deliveryPoint;

    public Request(PickupPoint pickupPoint, DeliveryPoint deliveryPoint) {
        this.deliveryPoint = deliveryPoint;
        this.pickupPoint = pickupPoint;
    }

    public PickupPoint getPickupPoint() {
        return pickupPoint;
    }

    public DeliveryPoint getDeliveryPoint() {
        return deliveryPoint;
    }

    @Override
    public String toString() {
        return "\nRequest{" +
                "\npickupPoint=" + pickupPoint +
                "\n, deliveryPoint=" + deliveryPoint +
                "\n}";
    }
}
