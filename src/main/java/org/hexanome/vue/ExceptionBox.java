package org.hexanome.vue;

import javafx.scene.control.Alert;

public class ExceptionBox {

    private Exception exception;

    public ExceptionBox(Exception e){
        exception = e;

    }

    public void display(){
        switch(this.exception.getMessage()){
            case "null":
                this.displayNullException();
                break;
            case "Wrong format":
                this.displayExceptionXML();
                break;
            default:
                break;
        }
    }

    private void displayNullException(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText("XML Error");
        alert.setContentText(this.exception.getMessage());

        alert.showAndWait();
    }
    private void displayExceptionXML(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error message");
        alert.setHeaderText(this.exception.getMessage());
        alert.setContentText("truck");

        alert.showAndWait();
    }

    //GETTER
    public Exception getException(){return this.exception;}
    //SETTER
    public void setException(Exception e){this.exception=e;}

}

