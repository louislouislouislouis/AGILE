package org.hexanome.Model;

public class Segment {
    private Intersection originIntersection;
    private Intersection destinationIntersection;
    private String name;
    private double length;

    public Segment(Intersection originIntersection, Intersection destinationIntersection, String name, double length){
        this.originIntersection = originIntersection;
        this.destinationIntersection = destinationIntersection;
        this.name = name;
        this.length = length;
    }

    @Override
    public String toString() {
        return "Segment{" +
                "originIntersection=" + originIntersection +
                ", destinationIntersection=" + destinationIntersection +
                ", name='" + name + '\'' +
                ", length=" + length +
                '}';
    }
}
