package org.hexanome.model;

import java.time.LocalTime;

public class Warehouse extends Point {
    private LocalTime departureTime;

    public Warehouse(LocalTime departureTime, Intersection departureAddress) {
        super(departureAddress);
        this.departureTime = departureTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public Intersection getAddress() {
        return super.getAddress();
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "departureTime=" + departureTime +
                "Point=" + super.toString() +
                "}";
    }
}
