package org.hexanome.vue;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox{

    /*---------------------------VARIABLES------------------------------------------------------------*/
    //Declaration of the buttons in the alertBox.fxml

    @FXML
    private Button btnClose;

    @FXML
    private AnchorPane layout;

    @FXML
    private Label lbMessageAlert;
    Stage window = new Stage();

    //This class will create a popup for the user displaying any malfunction in the program
    public void displayAlert(String title){

        if(title == null || lbMessageAlert == null){
            return;
        }
        System.out.println("Title : " + title + " Message : " + lbMessageAlert);
        //Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL); //blocks any action until the window is dealt with
        window.setTitle(title);
        btnClose.setOnAction(e -> window.close());
        /*layout.getChildren().addAll(lbMessageAlert*//*, btnClose*//*);*/
        /*Scene scene = new Scene(layout);
        window.setScene(scene);*/
        window.showAndWait(); //the window needs to be closed before returning
    }

    public AlertBox(Label lbMessageAlert) {
        this.lbMessageAlert = lbMessageAlert;
        this.btnClose = btnClose;

    }

    public Label getLbMessageAlert() {
        return lbMessageAlert;
    }

    public void setLbMessageAlert(Label lbMessageAlert) {
        this.lbMessageAlert = lbMessageAlert;
    }
}
