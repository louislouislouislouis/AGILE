package org.hexanome.model;

public class Point {
    private Intersection address;
    private Long id;
    private String type;

    public Point(Intersection address, String type) {
        this.address = address;
        this.id = address.getIdIntersection();
        this.type = type;
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

    @Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                "}";
    }
}
