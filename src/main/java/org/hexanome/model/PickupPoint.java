package org.hexanome.model;

import javafx.scene.paint.Color;

import java.time.LocalTime;

/**
 * Object containing a pickup Point
 *
 * @author Gastronom'if
 */
public class PickupPoint extends Point {

    /**
     * create a special point of pickup type
     *
     * @param address  address of the pickup
     * @param duration duration of the pickup
     * @param color    color of the point
     */
    public PickupPoint(Intersection address, int duration, Color color) {
        super(address, "pickup", color, LocalTime.MIDNIGHT, LocalTime.MIDNIGHT, duration);
    }

    @Override
    public String toString() {
        return "PickupPoint{" +
                "duration=" + super.getDuration() +
                "Point=" + super.toString() +
                "}";
    }
}
