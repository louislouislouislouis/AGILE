package org.hexanome.model;

public class Point {
    private Intersection address;

    public Point(Intersection address) {
        this.address = address;
    }

    public Intersection getAddress() {
        return address;
    }

<<<<<<< Updated upstream
=======
    public Long getId() {
        return id;
    }

    public void setAddress(Intersection address) {
        //tomando address y al editarla cambiamos su id también (actualizando intersección y su ID)
        this.address = address;
        this.id = address.getIdIntersection();
    }

    public String getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

>>>>>>> Stashed changes
    @Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                "}";
    }
}
