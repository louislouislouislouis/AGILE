package org.hexanome.vue;

import javafx.scene.control.Alert;

public class ExceptionBox {

    private String msg;
    private String type;

    public ExceptionBox(Exception e, String s) {
        msg = e.getMessage();
        type = s;
    }

    public ExceptionBox(String e, String s) {
        msg = e;
        type = s;
    }

    public void display() {
        switch (this.type) {
            case "Null":
                this.displayNullException();
                break;
            case "XML":
                this.displayExceptionXML();
                break;
            case "Other":
                this.displayOtherException();
                break;
            default:
                break;
        }
    }

    private void displayNullException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("Null Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void displayOtherException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    private void displayExceptionXML() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("XML Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }
}

