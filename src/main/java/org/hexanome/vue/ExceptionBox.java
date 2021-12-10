package org.hexanome.vue;

import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * this class is used to show what type of box for what type of exception
 */

public class ExceptionBox {

    private String msg;
    private String type;
    private Exception ex = null;

    /**
     * create ExceptionBox who contain info for the box
     *
     * @param e the exception
     * @param s the type of the exception
     */
    public ExceptionBox(Exception e, String s) {
        msg = e.getMessage();
        type = s;
        ex = e;
    }

    public ExceptionBox(String e, String s) {
        msg = e;
        type = s;
    }

    /**
     * method to call to display the ExceptionBox, is a switch which call private display method for exception
     * type
     *
     * @return void
     */
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

    /**
     * show a box for null exception
     *
     * @return void
     */
    private void displayNullException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("Null Error");
        alert.setContentText(msg);

        alert.showAndWait();
    }

    /**
     * show a box for other exception like IOS, SAX etc
     */
    private void displayOtherException() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("Error");
        alert.setContentText(msg);
        this.addExpandable(alert);


        alert.showAndWait();
    }

    /**
     * show a box for XML exception, it is exception when the xml file have some problems like wrong format
     */
    private void displayExceptionXML() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("XML Error");
        alert.setContentText(msg);
        this.addExpandable(alert);

        alert.showAndWait();
    }

    /**
     * to add a grid which display the track of the exception
     *
     * @param alert
     */
    private void addExpandable(Alert alert) {
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

