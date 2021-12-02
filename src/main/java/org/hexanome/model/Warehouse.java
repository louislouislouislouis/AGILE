package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

public class Warehouse extends Point {
    private LocalTime departureTime;

    /**
     * create a warehouse
     *
     * @param departureTime    time at which the delivery man start his journey
     * @param departureAddress address of the warehouse
     * @param color            color of the warehouse
     */
    public Warehouse(LocalTime departureTime, Intersection departureAddress, Color color) {
        super(departureAddress, "warehouse", color);
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
