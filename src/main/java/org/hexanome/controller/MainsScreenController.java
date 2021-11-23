package org.hexanome.controller;

import java.io.File;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

public class MainsScreenController {
    @FXML
    HBox mapContainer;

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
        System.out.println(map.getIntersections().size());
        System.out.println(map.getSegments().size());

        VBox root = new VBox();

        /* Création de la carte Gluon JavaFX */
        MapView mapView = new MapView();

        /* Création du point avec latitude et longitude */
        MapPoint mapPoint = new MapPoint(46.227638, 2.213749);

        /* Création et ajoute une couche à la carte */

        // MapLayer mapLayer = new CustomPinLayer(mapPoint);
        // MapLayer mapLayer = new CustomCircleMarkerLayer(mapPoint);
        // mapView.addLayer(mapLayer);

        /* Zoom de 5 */
        mapView.setZoom(5);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPoint, 0.1);

        mapContainer.getChildren().add(mapView);
    }

    public void addRequest(ActionEvent actionEvent) {
    }

    public void calculateRoute(ActionEvent actionEvent) {
    }

    /**@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    } **/
}
