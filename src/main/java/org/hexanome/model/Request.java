package org.hexanome.model;

public class Request {
    private PickupPoint pickupPoint;
    private DeliveryPoint deliveryPoint;

    /**
     * create a request, it is a couple of a pickup and a delivery point
     *
     * @param pickupPoint   pickup of the request
     * @param deliveryPoint delivery of the request
     */
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
                "pickupPoint= " + pickupPoint +
                ", deliveryPoint= " + deliveryPoint +
                "\n}";
    }
}
