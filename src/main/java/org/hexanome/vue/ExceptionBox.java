package org.hexanome.vue;

import javafx.scene.control.Alert;

public class ExceptionBox {

    private Exception exception;
    private String type;

    public ExceptionBox(Exception e, String s){
        exception = e;
        type = s;
    }
    public ExceptionBox(String s){
        type = s;
    }

    public void display(){
        switch(this.type){
            case "Null":
                this.displayNullException();
                break;
            case "XML":
                this.displayExceptionXML();
                break;
            case "Other":
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
    public String getType(){return this.type;}
    //SETTER
    public void setException(Exception e){this.exception=e;}
    public void setType(String s){this.type=s;}

}

