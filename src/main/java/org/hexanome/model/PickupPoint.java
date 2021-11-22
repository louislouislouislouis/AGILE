package org.hexanome.model;

public class PickupPoint extends Point{
    private int pickupDuration;

    public PickupPoint(Intersection address, int pickupDuration) {
        super(address);
        this.pickupDuration = pickupDuration;
    }

    public int getPickupDuration() {
        return pickupDuration;
    }

    @Override
    public String toString() {
        return "\nPickupPoint{" +
                "\npickupDuration=" + pickupDuration +
                "\nPoint=" + super.toString() +
                "\n}";
    }
}
