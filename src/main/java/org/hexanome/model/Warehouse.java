package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

public class Warehouse extends Point {
    private LocalTime departureTime;
    private LocalTime arrivalTime;

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
        this.arrivalTime = LocalTime.MIDNIGHT;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "departureTime=" + departureTime +
                "Point=" + super.toString() +
                "}";
    }
}
