package org.hexanome.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.vue.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class MainsScreenController {
    private MapIF map = new MapIF();
    private PlanningRequest planning = new PlanningRequest();

    public void selectionMap(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);

        System.out.println(selectedFile);

        MapDeserializer domMap = new MapDeserializer();

        try {
            domMap.load(map, selectedFile);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML e) {
            e.printStackTrace();
        }
        System.out.println(map);
    }

    public void addRequest(ActionEvent actionEvent) {
    }
    /**@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    } **/
}
