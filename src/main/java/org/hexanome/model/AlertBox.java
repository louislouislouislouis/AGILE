package org.hexanome.model;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {

    /*---------------------------VARIABLES------------------------------------------------------------*/
    //Declaration of the buttons in the alertBox.fxml
    //This class will create a popup for the user displaying any malfunction in the program
    public static void displayAlert(String title, String message){
        Stage window = new Stage();

        window.initModality(Modality.APPLICATION_MODAL); //blocks any action until the window is dealt with
        window.setTitle(title);

        Label label = new Label();
        label.setText(message);
        Button btnClose = new Button("OK");
        btnClose.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, btnClose);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait(); //the window needs to be closed before returning
    }
}
