package org.hexanome.model;

public class Warehouse {
    private int departureHour;
    private int departureMinute;
    private int departureSecond;
    private Intersection departureAddress;

    public Warehouse(int departureHour, int departureMinute, int departureSecond, Intersection departureAddress) {
        this.departureHour = departureHour;
        this.departureMinute = departureMinute;
        this.departureSecond = departureSecond;
        this.departureAddress = departureAddress;
    }
}
