package org.hexanome.model;

import java.time.LocalTime;

public class Warehouse extends Point{
    private LocalTime departureTime;

    public Warehouse(LocalTime departureTime, Intersection departureAddress) {
        super(departureAddress);
        this.departureTime = departureTime;
    }
}
