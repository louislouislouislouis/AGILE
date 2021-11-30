package org.hexanome.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.gluonhq.maps.MapLayer;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import org.hexanome.model.*;
import org.hexanome.vue.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import com.gluonhq.maps.MapPoint;
import com.gluonhq.maps.MapView;

import static org.hexanome.model.AlertBox.displayAlert;

public class MainsScreenController implements Observer {
    @FXML
    HBox mapContainer;
    @FXML
    Button btnLoadRequest;
    @FXML
    TableView tableView;

    /*---------------------------VARIABLES------------------------------------------------------------*/
    private MapIF map = new MapIF();
    private PlanningRequest planning = new PlanningRequest();
    private Tour tour;

    private static final ObservableList<Point> data = FXCollections.observableArrayList();

    /* Création de la carte Gluon JavaFX */
    private CustomMap mapView = new CustomMap();
    private CustomMapLayer requestLayer = new CustomMapLayer();
    private CustomMapLayer tourLayer = new CustomMapLayer();

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

        //VBox root = new VBox();

        /* Création et ajoute une couche à la carte */

        //MapLayer mapLayer = new CustomPinLayer(mapPoint);
        /*CustomMapLayer mapLayer = new CustomMapLayer();

        //add points to the layer
        map.getIntersections().forEach((id, intersection) -> {
            MapPoint mapPoint = new MapPoint(intersection.getLatitude(), intersection.getLongitude());
            mapLayer.addPoint(id, mapPoint);
        });

        mapView.addLayer(mapLayer);*/

        /* Zoom de 5 */
        mapView.setZoom(14);

        /* creation of the mapPoint on which the camera will be centered
         *  We use the longitude and latitude of Lyon
         * */
        MapPoint mapPointCamera = new MapPoint(45.760327, 4.876824);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPointCamera, 0.1);

        //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(mapView);

        //Force rerender (Bug fix - Gluon Maps Issue drag)
        mapView.setOnMouseReleased(e -> {
            System.out.println("onMousedetect");
            //Pour les layers de request
            requestLayer.forceReRender();
            tourLayer.forceReRender();

            //Pour le fond de la map
            mapView.layout();

        });
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

        /* Création et ajoute une couche à la carte */

        requestLayer = new CustomMapLayer();

        //add points to the layer

        //warehouse
        Intersection warehouse = planning.getWarehouse().getAddress();
        requestLayer.addSpecialPoint(warehouse.getIdIntersection(),
                new MapPoint(warehouse.getLatitude(), warehouse.getLongitude()), planning.getWarehouse().getColor());

        //requests
        planning.getRequests().forEach((request) -> {
            Intersection deliveryInt = request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            requestLayer.addSpecialPoint(deliveryInt.getIdIntersection(), mapPointDelivery, request.getDeliveryPoint().getColor());

            Intersection pickupInt = request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            requestLayer.addSpecialPoint(pickupInt.getIdIntersection(), mapPointPickup, request.getPickupPoint().getColor());
        });

        HashMap<Long, Circle> circleList = requestLayer.getCircleList();

        circleList.forEach((id, circle) -> {
            circle.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
                if (newValue) {
                    circle.setRadius(10);
                } else {
                    circle.setRadius(10);
                }
            });
        });
        mapView.addLayer(requestLayer);

    }

    public void addRequest(ActionEvent actionEvent) {
        //method that uploads an XML file with the command
        //File selectedFile = fileChooser(actionEvent);
        //System.out.println(selectedFile);
        displayAlert("ALERT!", "For the moment this functionality is not available");
    }

    public void computeTour(ActionEvent actionEvent) {
        //method that calculates the most optimal path of the tour
        tour = new Tour(new ArrayList<>(), null, this);
        mapView.removeLayer(tourLayer);
        new GraphAPI().V1_TSP(planning, map, tour);

        //Add Segment to the layer

        tourLayer = new CustomMapLayer();

        List<Intersection> intersectionList = tour.getIntersections();

        System.out.println(intersectionList.size());

        for (int i = 0; i < intersectionList.size() - 1; i++) {
            Intersection start;
            Intersection end;
            start = intersectionList.get(i);
            end = intersectionList.get(i + 1);
            MapPoint mapPointStart = new MapPoint(start.getLatitude(), start.getLongitude());
            tourLayer.addPoint(start.getIdIntersection(), mapPointStart);
            MapPoint mapPointEnd = new MapPoint(end.getLatitude(), end.getLongitude());
            tourLayer.addPoint(end.getIdIntersection(), mapPointEnd);
            tourLayer.addSegment(mapPointStart, mapPointEnd, (long) i);
        }

        HashMap<Long, Polyline> polylineList = tourLayer.getPolylineList();

        polylineList.forEach((aLong, polyline) -> {
            polyline.hoverProperty().addListener((observable, oldValue, newValue) -> {

                if (newValue) {
                    polylineList.forEach((id, poly) -> {

                        if (id <= aLong) {
                            poly.setStrokeWidth(10);
                            poly.setStroke(Color.OLIVE);
                        }
                    });
                } else {
                    polylineList.forEach((id, poly) -> {
                        if (id <= aLong) {
                            poly.setStrokeWidth(5);
                            poly.setStroke(Color.RED);
                        }
                    });
                }
            });
        });

        mapView.removeLayer(requestLayer);
        mapView.addLayer(tourLayer);
        mapView.addLayer(requestLayer);

        tour.notifyChange("UPDATEMAP");

        updateTableView();
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

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("Une fonction d'actualisation à été appelé");
        String action = (String) arg;
        switch (action) {
            case "UPDATEMAP":
                System.out.println("Update map called");
                requestLayer.forceReRender();
                tourLayer.forceReRender();
                mapView.layout();
                break;
            default:
                System.out.println("Unknown method");

        }

    }


    private void updateTableView() {
        // columns initialization
        TableColumn idCol = (TableColumn) tableView.getColumns().get(0);
        TableColumn typeCol = (TableColumn) tableView.getColumns().get(1);
        TableColumn arrivalCol = (TableColumn) tableView.getColumns().get(2);
        TableColumn waitingCol = (TableColumn) tableView.getColumns().get(3);
        TableColumn departureCol = (TableColumn) tableView.getColumns().get(4);

        // cell factory
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        waitingCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

        tableView.setItems(data);

        data.add(planning.getWarehouse());

        planning.getRequests().forEach(request -> {
            data.add(request.getDeliveryPoint());
            data.add(request.getPickupPoint());
        });
    }


}
