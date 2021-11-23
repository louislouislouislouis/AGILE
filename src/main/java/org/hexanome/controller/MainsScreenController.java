package org.hexanome.controller;

import java.io.File;
import java.io.FileFilter;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hexanome.model.MapIF;
import org.hexanome.vue.App;

public class MainsScreenController {

    private MapIF map = new MapIF();

    /*---------------------------VARIABLES------------------------------------------------------------*/

    //Declaration of the buttons interactives in the mainsScreen.fxml
    @FXML private Button btnLoadMap;
    @FXML private Button btnAddRequest;
    @FXML private Button btnValidateRoute;

    /*-------------------------GETTERS AND SETTERS-----------------------------------------------------*/
    public Button getBtnLoadMap() {return btnLoadMap;}
    public void setBtnLoadMap(Button btnLoadMap) {this.btnLoadMap = btnLoadMap;}
    public Button getBtnAddRequest() {return btnAddRequest;}
    public void setBtnAddRequest(Button btnAddRequest) {this.btnAddRequest = btnAddRequest;}
    public Button getBtnValidateRoute() {return btnValidateRoute;}
    public void setBtnValidateRoute(Button btnValidateRoute) {this.btnValidateRoute = btnValidateRoute;}

    /*--------------------------------Methods----------------------------------------------------------*/

    public void selectionMap(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
/*        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);*/

        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);
        if(selectedFile.exists()){
            btnAddRequest.setDisable(false);
            btnValidateRoute.setDisable(false);
        }else{
            btnAddRequest.setDisable(true);
            btnValidateRoute.setDisable(true);
        }

    }

    public void addRequest(ActionEvent actionEvent) {
        //method that uploads an XML file with the command
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);
        if(selectedFile.exists()){
            btnAddRequest.setDisable(false);
            btnValidateRoute.setDisable(false);
        }else{
            btnAddRequest.setDisable(true);
            btnValidateRoute.setDisable(true);
        }
    }

    public void calculateRoute(ActionEvent actionEvent) {
        //method that calculates the most optimal path of the tour
    }

    public File fileChooser(ActionEvent actionEvent){
        //method that opens the File Explorer to allow the user to get their XML files
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        //Filter declaration for XML files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter); //utilisation du filter
        File selectedFile = fileChooser.showOpenDialog(thisStage);
        return selectedFile;
    }
    /**@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    } **/
}
