package org.hexanome.model;

public class Intersection {
    private double longitude;
    private double latitude;
    private long idIntersection;

    public Intersection(double longitude, double latitude, long idIntersection) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.idIntersection = idIntersection;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", idIntersection=" + idIntersection +
                '}';
    }
}
