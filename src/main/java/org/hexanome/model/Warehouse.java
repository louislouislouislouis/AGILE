package org.hexanome.model;

public class Warehouse extends Point{
    private int departureHour;
    private int departureMinute;
    private int departureSecond;

    public Warehouse(int departureHour, int departureMinute, int departureSecond, Intersection departureAddress) {
        super(departureAddress);
        this.departureHour = departureHour;
        this.departureMinute = departureMinute;
        this.departureSecond = departureSecond;
    }
}
