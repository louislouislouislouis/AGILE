package org.hexanome.model;

import java.time.LocalTime;

public class PickupPoint extends Point {
    private int duration;
    private LocalTime departureTime;
    private LocalTime arrivalTime;

    public PickupPoint(Intersection address, int duration) {
        super(address, "pickup");
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
