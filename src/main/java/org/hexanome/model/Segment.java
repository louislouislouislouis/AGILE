package org.hexanome.model;

public class Segment {
    private Intersection originIntersection;
    private Intersection destinationIntersection;
    private String name;
    private double length;
    private int duration;

    /**
     * create a segment
     *
     * @param originIntersection      origin intersection of the segment
     * @param destinationIntersection destination intersection of the segment
     * @param name                    represent the name of the segment, it is usually the name of a street
     * @param length                  as the name imply it is the length of the segment
     */
    public Segment(Intersection originIntersection, Intersection destinationIntersection, String name, double length) {
        this.originIntersection = originIntersection;
        this.destinationIntersection = destinationIntersection;
        this.name = name;
        this.length = length;
        this.duration = this.calculateDuration();
    }

    /**
     * t = s/v
     * s = length
     * v = 15km/h : 3,6 --> converting km/h in m/s
     *
     * @return duration for passing the segment in seconds
     */
    private int calculateDuration() {
        int duration = (int) Math.ceil((this.length / (15 / 3.6)));
        return duration;
    }

    public Intersection getOriginIntersection() {
        return originIntersection;
    }

    public Intersection getDestinationIntersection() {
        return destinationIntersection;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }


    @Override
    public String toString() {
        return "\nSegment{" +
                "originIntersection=" + originIntersection +
                ", destinationIntersection=" + destinationIntersection +
                ", name='" + name + '\'' +
                ", length=" + length +
                "\n}";
    }

    public int getDuration() {
        return this.duration;
    }
}
