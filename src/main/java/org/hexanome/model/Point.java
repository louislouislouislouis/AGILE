package org.hexanome.model;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;

public class Point {
    private Intersection address;
    private Long id;
    private String type;
    private Color color;
    private Button btnModify;
    private Button btnDelete;

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
        this.btnModify = new Button("Modify");
        this.btnDelete = new Button("Delete");

        btnModify.setOnAction((ActionEvent e)->{
            System.out.println("Modifying Point");
        });

        btnDelete.setOnAction((ActionEvent e)->{
            System.out.println("Deleting Point");
        });
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

    public Button getBtnModify() {
        return btnModify;
    }

    public void setBtnModify(Button btnModify) {
        this.btnModify = btnModify;
    }

    /*@Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                "}";
    }*/

    @Override
    public String toString() {
        return "Point{" +
                "address=" + address +
                ", id=" + id +
                ", type='" + type + '\'' +
                ", color=" + color +
                '}';
    }
}
