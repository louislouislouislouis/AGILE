package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

public class DeliveryPoint extends Point {
    private int duration;
    private LocalTime arrivalTime;
    private LocalTime departureTime;

    public DeliveryPoint(Intersection address, int duration, Color color) {
        super(address, "delivery", color);
        this.duration = duration;
        departureTime = LocalTime.MIDNIGHT;
        arrivalTime = LocalTime.MIDNIGHT;
    }

    public int getDuration() {
        return duration;
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

    @Override
    public String toString() {
        return "DeliveryPoint{" +
                "duration=" + duration +
                "Point=" + super.toString() +
                "}";
    }
}
