package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

/**
 * Object containing a point representing a warehouse
 *
 * @author Gastronom'if
 */
public class Warehouse extends Point {

    /**
     * create a warehouse
     *
     * @param departureTime    time at which the delivery man start his journey
     * @param departureAddress address of the warehouse
     * @param color            color of the warehouse
     */
    public Warehouse(LocalTime departureTime, Intersection departureAddress, Color color) {
        super(departureAddress, "warehouse", color, departureTime, LocalTime.MIDNIGHT, 0);
    }

    @Override
    public String toString() {
        return "Warehouse{" +
                "departureTime=" + super.getDepartureTime() +
                "Point=" + super.toString() +
                "}";
    }

}
