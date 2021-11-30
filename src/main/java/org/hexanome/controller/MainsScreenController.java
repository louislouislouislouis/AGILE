package org.hexanome.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;

import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;


import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hexanome.controller.tsp.Graph;
import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Tour;
import org.hexanome.vue.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

public class MainsScreenController {
    @FXML
    HBox mapContainer;
    @FXML
    Button btnLoadRequest;

    /*---------------------------VARIABLES------------------------------------------------------------*/
    private MapIF map = new MapIF();
    private PlanningRequest planning = new PlanningRequest();
    private Tour tour;

    /* Création de la carte Gluon JavaFX */
    private MapView mapView = new MapView();
    private MapLayer requestLayer = new MapLayer();
    private MapLayer tourLayer = new MapLayer();

    //Declaration of the interactive buttons in the mainsScreen.fxml
    @FXML
    private Button btnLoadMap;
    @FXML
    private Button btnAddRequest;
    @FXML
    private Button btnValidateRoute;

    /*-------------------------GETTERS AND SETTERS-----------------------------------------------------*/
    public Button getBtnLoadMap() {
        return btnLoadMap;
    }

    public void setBtnLoadMap(Button btnLoadMap) {
        this.btnLoadMap = btnLoadMap;
    }

    public Button getBtnAddRequest() {
        return btnAddRequest;
    }

    public void setBtnAddRequest(Button btnAddRequest) {
        this.btnAddRequest = btnAddRequest;
    }

    public Button getBtnValidateRoute() {
        return btnValidateRoute;
    }

    public void setBtnValidateRoute(Button btnValidateRoute) {
        this.btnValidateRoute = btnValidateRoute;
    }

    /*--------------------------------Methods----------------------------------------------------------*/

    public void selectionMap(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);

        // We initialize the deserializer
        MapDeserializer domMap = new MapDeserializer();

        // We delete the map's content before loading the xml

        map.clearMap();
        mapView.removeLayer(requestLayer);
        mapView.removeLayer(tourLayer);

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


        /* Création de la WebView et du moteur */
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
        System.out.println(webEngine.isJavaScriptEnabled());
        webEngine.setOnAlert(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(event.getData());
            alert.showAndWait();
        });
        /* Charge la carte HTML avec Leafletjs */
        webEngine.loadContent("<!DOCTYPE html>\n"
                + "<html lang=\"fr\">\n"
                + "\n"
                + "<head>\n"
                + "    <meta charset=\"UTF-8\">\n"
                + "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n"
                + "    <title>Carte</title>\n"
                + "    <!-- leafletjs CSS -->\n"
                + "    <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.min.css\" />\n"
                + "    <!-- leafletjs JS -->\n"
                + "    <script src=\"https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/leaflet.min.js\"></script>\n"
                + "    <style>\n"
                + "        /* Carte plein écran */\n"
                + "        html,\n"
                + "        body {\n"
                + "            margin: 0;\n"
                + "            height: 100%;\n"
                + "            width: 100%;\n"
                + "            background-color: red;\n"
                + "        }\n"
                + "\n"
                + "        #map {\n"
                + "            width: 100%;\n"
                + "            height: 100%;\n"
                + "        }\n"
                + "    </style>\n"
                + "</head>\n"
                + "\n"
                + "<body>\n"
                + "\n"
                + "    <!-- L'endroit ou la carte va s'afficher -->\n"
                + "    <div id=\"map\"></div>\n"
                + "\n"
                + "    <script>\n"
                + "        /* Les options pour afficher la France */\n"
                + "        const mapOptions = {\n"
                + "            center: [46.225, 0.132],\n"
                + "            zoom: 5\n"
                + "        }\n"
                + "\n"
                + "        /* Les options pour affiner la localisation */\n"
                + "        const locationOptions = {\n"
                + "            maximumAge: 10000,\n"
                + "            timeout: 5000,\n"
                + "            enableHighAccuracy: true\n"
                + "        };\n"
                + "\n"
                + "        /* Création de la carte */\n"
                + "        var map = new L.map(\"map\", mapOptions);\n"
                + "\n"
                + "        /* Création de la couche OpenStreetMap */\n"
                + "        var layer = new L.TileLayer(\"https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png\",\n"
                + "            { attribution: '&copy; <a href=\"https://www.openstreetmap.org/copyright\">OpenStreetMap</a> contributors' });\n"
                + "\n"
                + "        /* Ajoute la couche de la carte */\n"
                + "        map.addLayer(layer);\n"
                + "\n"
                + "        /* Verifie que le navigateur est compatible avec la géolocalisation */\n"
                + "        if (\"geolocation\" in navigator) {\n"
                + "            navigator.geolocation.getCurrentPosition(handleLocation, handleLocationError, locationOptions);\n"
                + "        } else {\n"
                + "            /* Le navigateur n'est pas compatible */\n"
                + "            alert(\"Géolocalisation indisponible\");\n"
                + "        }\n"
                + "\n"
                + "        function handleLocation(position) {\n"
                + "            /* Zoom avant de trouver la localisation */\n"
                + "            map.setZoom(16);\n"
                + "            /* Centre la carte sur la latitude et la longitude de la localisation de l'utilisateur */\n"
                + "            map.panTo(new L.LatLng(position.coords.latitude, position.coords.longitude));\n"
                + "        }\n"
                + "\n"
                + "        function handleLocationError(err) {\n"
                + "            alert(`ERREUR (${err.code}): ${err.message}`);\n"
                + "        }\n"
                + "\n"
                + "    </script>\n"
                + "\n"
                + "</body>\n"
                + "\n"
                + "</html>");

        webView.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("ss");
            }
        });
        //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(webView);
    }


    public void loadRequest(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);

        // on enleve les laers actuels

        mapView.removeLayer(requestLayer);
        mapView.removeLayer(tourLayer);

        if (selectedFile.exists()) {
            btnAddRequest.setDisable(false);
            btnValidateRoute.setDisable(false);
        } else {
            btnAddRequest.setDisable(true);
            btnValidateRoute.setDisable(true);
        }

        RequestDeserializer mydomrequest = new RequestDeserializer();

        // We clear the requestLayer before loading an XML file with requests

        planning.clearPlanning();

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
        CustomMapLayer mapLayer = new CustomMapLayer();

        //add points to the layer

        //warehouse
        Intersection warehouse = planning.getWarehouse().getAddress();
        mapLayer.addPointWarehouse(warehouse.getIdIntersection(),
                new MapPoint(warehouse.getLatitude(), warehouse.getLongitude()), Color.DARKGOLDENROD);

        //requests
        planning.getRequests().forEach((Request) -> {
            Intersection deliveryInt = Request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            mapLayer.addPointDelivery(deliveryInt.getIdIntersection(), mapPointDelivery, Color.RED);

            Intersection pickupInt = Request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            mapLayer.addPointPickup(pickupInt.getIdIntersection(), mapPointPickup, Color.RED);
        });

        requestLayer = mapLayer;

        mapView.addLayer(requestLayer);

        //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(mapView);
    }

    public void addRequest(ActionEvent actionEvent) {
        //method that uploads an XML file with the command
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);
    }

    public void computeTour(ActionEvent actionEvent) {
        //method that calculates the most optimal path of the tour

        mapView.removeLayer(tourLayer);

        tour = new GraphAPI().V1_TSP(planning, map);

        //Add Segment to the layer

        CustomMapLayer mapLayer = new CustomMapLayer();

        List<Intersection> intersectionList = tour.getIntersections();

        System.out.println(intersectionList.size());

        for (int i = 0; i < intersectionList.size() - 1; i++) {
            Intersection start;
            Intersection end;
            start = intersectionList.get(i);
            end = intersectionList.get(i + 1);
            MapPoint mapPointStart = new MapPoint(start.getLatitude(), start.getLongitude());
            mapLayer.addPoint(start.getIdIntersection(), mapPointStart);

            MapPoint mapPointEnd = new MapPoint(end.getLatitude(), end.getLongitude());
            mapLayer.addPoint(end.getIdIntersection(), mapPointEnd);

            mapLayer.addSegment(mapPointStart, mapPointEnd);
        }

        tourLayer = mapLayer;

        mapView.addLayer(tourLayer);

        //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(mapView);
    }

    public File fileChooser(ActionEvent actionEvent) {
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

    public boolean validationMap(File selectedFile) {
        //method that validates de xmlFile is empty
        boolean validationMap = false;
        return validationMap;
    }

    public boolean validationPlaningRequest(File selectedFile) {
        //method that validates the list of request in the xmlFile
        boolean validationPlaningRequest = false;
        return validationPlaningRequest;
    }

    /**@FXML private void switchToSecondary() throws IOException {
    App.setRoot("secondary");
    } **/
}
