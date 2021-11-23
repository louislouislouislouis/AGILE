package org.hexanome.model;

public class DeliveryPoint extends Point{
    private int deliveryDuration;

    public DeliveryPoint(Intersection address, int deliveryDuration) {
        super(address);
        this.deliveryDuration = deliveryDuration;
    }

    public int getDeliveryDuration() {
        return deliveryDuration;
    }

    @Override
    public String toString() {
        return "DeliveryPoint{" +
                "deliveryDuration=" + deliveryDuration +
                "Point=" + super.toString() +
                "}";
    }
}
