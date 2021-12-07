package org.hexanome.model;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

import java.time.LocalTime;

public class Point {
    private Intersection address;
    private Long id;
    private String type;
    private Color color;
    private Button btnModify;
    private Button btnDelete;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private int duration;

    /**
     * Clear a point
     *
     * @param address Intersection that represent the address of the point
     * @param type    type of the point
     * @param color   color of the point
     */
    public Point(Intersection address, String type, Color color, LocalTime departureTime, LocalTime arrivalTime, int duration) {
        this.address = address;
        this.id = address.getIdIntersection();
        this.type = type;
        this.color = color;

        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
    }

    public Intersection getAddress() {
        return address;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                "departure-time=" + departureTime +
                "arrival-time=" + arrivalTime +
                "}";
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

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
