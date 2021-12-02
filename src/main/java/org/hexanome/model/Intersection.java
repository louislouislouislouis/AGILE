package org.hexanome.model;

public class Intersection {
    private double longitude;
    private double latitude;
    private long idIntersection;

    /**
     * create an intersection
     *
     * @param longitude      longitude of the intersection
     * @param latitude       latitude of the intersection
     * @param idIntersection unique id to recognize the intersection
     */
    public Intersection(double longitude, double latitude, long idIntersection) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.idIntersection = idIntersection;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public long getIdIntersection() {
        return idIntersection;
    }

    @Override
    public String toString() {
        return "Intersection{" +
                "longitude=" + longitude +
                ", latitude=" + latitude +
                ", idIntersection=" + idIntersection +
                "}";
    }

    @Override
    public boolean equals(Object o) {
        double delta = 0.00000001;
        if (o instanceof Intersection) {
            Intersection other = (Intersection) o;
            long deltaId = this.idIntersection - other.getIdIntersection();
            double deltaLat = this.latitude - other.getLatitude();
            double deltaLong = this.longitude - other.getLongitude();
            return deltaId == 0 && deltaLat > -delta && deltaLat < delta && deltaLong > -delta && deltaLong < delta;
        } else {
            return false;
        }
    }
}
