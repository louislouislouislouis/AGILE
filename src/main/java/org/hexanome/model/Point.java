package org.hexanome.model;

import javafx.scene.paint.Color;

public class Point {
    private Intersection address;
    private Long id;
    private String type;
    private Color color;

    /**
     * Clear a point
     *
     * @param address Intersection that represent the address of the point
     * @param type    type of the point
     * @param color   color of the point
     */
    public Point(Intersection address, String type, Color color) {
        this.address = address;
        this.id = address.getIdIntersection();
        this.type = type;
        this.color = color;
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
                "}";
    }
}
