package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

public class DeliveryPoint extends Point {

    /**
     * create a special point of delivery type
     *
     * @param address  address of the delivery
     * @param duration duration of the delivery
     * @param color    color of the point
     */
    public DeliveryPoint(Intersection address, int duration, Color color) {
        super(address, "delivery", color, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT, duration);
    }

    @Override
    public String toString() {
        return "DeliveryPoint{" +
                "duration=" + super.getDuration() +
                "Point=" + super.toString() +
                "}";
    }
}
