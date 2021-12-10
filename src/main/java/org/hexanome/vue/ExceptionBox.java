package org.hexanome.vue;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionBox {

    private String msg;
    private String type;
    private Exception ex = null;

    public ExceptionBox(Exception e, String s) {
        msg = e.getMessage();
        type = s;
        ex = e;
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
        this.addExpandable(alert);

        alert.showAndWait();
    }

    private void addExpandable(Alert alert){
        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        this.ex.printStackTrace(pw);
        String exceptionText = sw.toString();



        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);

        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);
    }
}

