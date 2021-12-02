package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

public class PickupPoint extends Point {
    private int duration;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    /**
     * create a special point of pickup type
     *
     * @param address  address of the pickup
     * @param duration duration of the pickup
     * @param color    color of the point
     */
    public PickupPoint(Intersection address, int duration, Color color) {
        super(address, "pickup", color);
        this.duration = duration;
        departureTime = LocalTime.MIDNIGHT;
        arrivalTime = LocalTime.MIDNIGHT;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "PickupPoint{" +
                "duration=" + duration +
                "Point=" + super.toString() +
                "}";
    }
}
