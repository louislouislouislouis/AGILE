package org.hexanome.model;

import java.time.LocalTime;

public class Warehouse extends Point {
    private LocalTime departureTime;

    public Warehouse(LocalTime departureTime, Intersection departureAddress) {
        super(departureAddress, "warehouse");
        this.departureTime = departureTime;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "departureTime=" + departureTime +
                "Point=" + super.toString() +
                "}";
    }
}
