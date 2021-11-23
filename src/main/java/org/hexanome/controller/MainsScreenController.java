package org.hexanome.controller;

import java.io.File;
import java.io.IOException;

import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
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
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.vue.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;
import com.gluonhq.maps.MapLayer;

public class MainsScreenController {
    @FXML
    HBox mapContainer;
    @FXML
    Button btnLoadRequest;

    private MapIF map = new MapIF();
    private PlanningRequest planning = new PlanningRequest();

    /* Création de la carte Gluon JavaFX */
    private MapView mapView = new MapView();

    public void selectionMap(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);

        System.out.println(selectedFile);

        // We initialize the deserializer
        MapDeserializer domMap = new MapDeserializer();

        // We delete the map's content before loading the xml

        map.clearMap();

        try {
            domMap.load(map, selectedFile);
            btnLoadRequest.setDisable(false);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML e) {
            e.printStackTrace();
        }

        //VBox root = new VBox();

        /* Création et ajoute une couche à la carte */

        //MapLayer mapLayer = new CustomPinLayer(mapPoint);
        CustomCircleMarkerLayer mapLayer = new CustomCircleMarkerLayer();

        //add points to the layer
        map.getIntersections().forEach((id, intersection) -> {
            MapPoint mapPoint = new MapPoint(intersection.getLatitude(), intersection.getLongitude());
            mapLayer.addPoint(id, mapPoint);
        });

        mapView.addLayer(mapLayer);

        /* Zoom de 5 */
        mapView.setZoom(6);

        /* creation of the mapPoint on which the camera will be centered
        *  We use the longitude and latitude of Lyon
        * */
        MapPoint mapPointCamera = new MapPoint(45.764043, 4.835659);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPointCamera, 0.1);

        mapContainer.getChildren().add(mapView);
    }

    public void loadRequest(ActionEvent actionEvent) {
        Node node = (Node) actionEvent.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(thisStage);

        RequestDeserializer mydomrequest = new RequestDeserializer();

        try {
            mydomrequest.load(planning, selectedFile, map);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ExceptionXML e) {
            e.printStackTrace();
        }

        //VBox root = new VBox();

        /* Création et ajoute une couche à la carte */

        //MapLayer mapLayer = new CustomPinLayer(mapPoint);
        CustomCircleMarkerLayer mapLayer = new CustomCircleMarkerLayer();

        //add points to the layer
        planning.getRequests().forEach((Request) -> {
            Intersection deliveryInt = Request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            mapLayer.addPointDelivery(deliveryInt.getIdIntersection(), mapPointDelivery, Color.AQUA);

            Intersection pickupInt = Request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            mapLayer.addPointPickup(pickupInt.getIdIntersection(), mapPointPickup, Color.AQUA);
        });

        mapView.addLayer(mapLayer);

        /* Zoom de 5 */
        mapView.setZoom(6);

        /* creation of the mapPoint on which the camera will be centered
         *  We use the longitude and latitude of Lyon
         * */
        MapPoint mapPointCamera = new MapPoint(45.764043, 4.835659);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPointCamera, 0.1);

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
