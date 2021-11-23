package org.hexanome.model;

public class Point {
    private Intersection address;

    public Point(Intersection address) {
        this.address = address;
    }

    public Intersection getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                "}";
    }
}
