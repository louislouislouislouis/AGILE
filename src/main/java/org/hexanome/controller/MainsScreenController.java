package org.hexanome.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import com.gluonhq.maps.MapLayer;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.event.ActionEvent;
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
    private HashMap<String, MapLayer> layerList = new HashMap<>();

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
        layerList.forEach((id, layer) -> {
            mapView.removeLayer(layer);
        });


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
        CustomMapLayer mapLayer = new CustomMapLayer();

        //add points to the layer
        map.getIntersections().forEach((id, intersection) -> {
            MapPoint mapPoint = new MapPoint(intersection.getLatitude(), intersection.getLongitude());
            mapLayer.addPoint(id, mapPoint);
        });

        layerList.put("mapLayer", mapLayer);

        mapView.addLayer(mapLayer);

        /* Zoom de 5 */
        mapView.setZoom(14);

        /* creation of the mapPoint on which the camera will be centered
         *  We use the longitude and latitude of Lyon
         * */
        MapPoint mapPointCamera = new MapPoint(45.764043, 4.835659);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPointCamera, 0.1);

        mapContainer.getChildren().add(mapView);
    }

    public void loadRequest(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);

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
        mapView.removeLayer(layerList.get("requestLayer"));

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
        planning.getRequests().forEach((Request) -> {
            Intersection deliveryInt = Request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            mapLayer.addPointDelivery(deliveryInt.getIdIntersection(), mapPointDelivery, Color.RED);

            Intersection pickupInt = Request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            mapLayer.addPointPickup(pickupInt.getIdIntersection(), mapPointPickup, Color.RED);
        });

        layerList.put("requestLayer", mapLayer);

        mapView.addLayer(mapLayer);

        //mapContainer.getChildren().add(mapView);
    }

    public void addRequest(ActionEvent actionEvent) {
        //method that uploads an XML file with the command
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);
    }

    public void computeTour(ActionEvent actionEvent) {
        //method that calculates the most optimal path of the tour

        tour = new GraphAPI().V1_TSP(planning, map);

        //Add Segment to the layer

        CustomMapLayer mapLayer = (CustomMapLayer) layerList.get("requestLayer");

        HashMap<Long, Pair<MapPoint, Shape>> pointList = mapLayer.getPointList();

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
