package org.hexanome.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.*;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import com.gluonhq.maps.MapPoint;

import static org.hexanome.vue.AlertBox.displayAlert;

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

        // We initialize the deserializer
        MapDeserializer domMap = new MapDeserializer();

        // We delete the map's content before loading the xml

        map.clearMap();
        mapView.removeLayer(requestLayer);
        mapView.removeLayer(tourLayer);

        try {
            domMap.load(map, selectedFile);
            btnLoadRequest.setDisable(false);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            e.printStackTrace();
        }

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
            //Pour les layers de request
            requestLayer.forceReRender();
            tourLayer.forceReRender();

            //Pour le fond de la map
            mapView.layout();

        });
    }

    /**
     * Method be called everytime button load Request is Handled
     *
     * @param actionEvent
     * @return void
     *
     */
    public void loadRequest(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);

        //Remove the old layer
        mapView.removeLayer(requestLayer);
        mapView.removeLayer(tourLayer);

        //Handle other button
        if (selectedFile.exists()) {
            btnAddRequest.setDisable(false);
            btnValidateRoute.setDisable(false);
        } else {
            btnAddRequest.setDisable(true);
            btnValidateRoute.setDisable(true);
        }

        // We clear the requestLayer before loading an XML file with requests
        planning.clearPlanning();

        //Parse the xml file
        RequestDeserializer mydomrequest = new RequestDeserializer();
        try {
            mydomrequest.load(planning, selectedFile, map);
        } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
            e.printStackTrace();
        }

        //Create the Request Layer
        requestLayer = new CustomMapLayer();

        //Add the warehouse
        Intersection warehouse = planning.getWarehouse().getAddress();
        requestLayer.addSpecialPointRectangle(warehouse.getIdIntersection(),
                new MapPoint(warehouse.getLatitude(), warehouse.getLongitude()), planning.getWarehouse().getColor());

        //Add the requests
        planning.getRequests().forEach((request) -> {
            Intersection deliveryInt = request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            requestLayer.addSpecialPointCircle(deliveryInt.getIdIntersection(), mapPointDelivery, request.getDeliveryPoint().getColor());

            Intersection pickupInt = request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            requestLayer.addSpecialPointRectangle(pickupInt.getIdIntersection(), mapPointPickup, request.getPickupPoint().getColor());
        });

        HashMap<Long, Shape> shapeList = requestLayer.getShapeList();

        // enable to scroll to selected point from the map

        shapeList.forEach((id, shape) -> {
            shape.setOnMouseClicked(mouseEvent -> {

                for (int i = 0; i < tableView.getItems().size(); i++) {
                    Point point = (Point) tableView.getItems().get(i);
                    if (Objects.equals(point.getId(), id)) {
                        tableView.scrollTo(i);
                        tableView.getSelectionModel().select(i);
                        break;
                    }
                }
            });
        });
        mapView.addLayer(requestLayer);

    }


    /**
     * Method be called every time button "add request" is handled
     *
     * @param actionEvent
     * @return void
     */
    public void addRequest(ActionEvent actionEvent) {
        displayAlert("ALERT!", "For the moment this functionality is not available");
    }

    /**
     * Method to be called every time button compute tour is handled
     *
     * @param actionEvent
     * @return void
     */
    public void computeTour(ActionEvent actionEvent) {
        //method that calculates the most optimal path of the tour
        tour = new Tour(new ArrayList<>(), null, this,planning.getWarehouse().getDepartureTime(),map.getMatAdj());

        // clear
        mapView.removeLayer(tourLayer);

        new GraphAPI().V1_TSP(planning, map, tour);

        //Add Segment to the layer

        tourLayer = new CustomMapLayer();

        List<Intersection> intersectionList = tour.getIntersections();

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
                            poly.setStrokeWidth(7);
                            poly.setStroke(Color.DODGERBLUE);
                            DropShadow e = new DropShadow();
                            e.setColor(Color.BLUE);
                            e.setRadius(9);
                            poly.setEffect(e);
                        }
                    });
                } else {
                    polylineList.forEach((id, poly) -> {
                        if (id <= aLong) {
                            poly.setStrokeWidth(5);
                            poly.setStroke(Color.DODGERBLUE);
                            poly.setEffect(null);
                        }
                    });
                }
            });
        });

        mapView.removeLayer(requestLayer);
        mapView.addLayer(tourLayer);
        mapView.addLayer(requestLayer);

        tour.notifyChange("UPDATEMAP");

        //updateTableView();
    }


    /**
     * Method called to open a Nevigation File
     * Return the file
     *
     * @param actionEvent
     * @return File
     */
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
        String action = (String) arg;
        switch (action) {
            case "UPDATEMAP":
                tourLayer = new CustomMapLayer();

                List<Intersection> intersectionList = tour.getIntersections();


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
                                    poly.setStrokeWidth(7);
                                    poly.setStroke(Color.DODGERBLUE);
                                    DropShadow e = new DropShadow();
                                    e.setColor(Color.BLUE);
                                    e.setRadius(9);
                                    poly.setEffect(e);
                                }
                            });
                        } else {
                            polylineList.forEach((id, poly) -> {
                                if (id <= aLong) {
                                    poly.setStrokeWidth(5);
                                    poly.setStroke(Color.DODGERBLUE);
                                    poly.setEffect(null);
                                }
                            });
                        }
                    });
                });

                mapView.removeLayer(tourLayer);
                mapView.addLayer(tourLayer);


                requestLayer.forceReRender();
                tourLayer.forceReRender();
                mapView.layout();
                try {
                    TimeUnit.SECONDS.sleep(3);
                }catch ( InterruptedException e) {

                }

                break;
            default:
                System.out.println("Unknown method");

        }

    }


    private void updateTableView() {
        // columns initialization
        TableColumn<Point, String> idCol = (TableColumn) tableView.getColumns().get(0);
        TableColumn<Point, String> typeCol = (TableColumn) tableView.getColumns().get(1);
        TableColumn<Point, String> arrivalCol = (TableColumn) tableView.getColumns().get(2);
        TableColumn<Point, String> waitingCol = (TableColumn) tableView.getColumns().get(3);
        TableColumn<Point, String> departureCol = (TableColumn) tableView.getColumns().get(4);
        TableColumn<Point, Color> colorCol = (TableColumn) tableView.getColumns().get(6);

        // cell factory
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        arrivalCol.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        waitingCol.setCellValueFactory(new PropertyValueFactory<>("duration"));
        departureCol.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));

        colorCol.setCellFactory(tv -> new TableCell<Point, Color>() {
            @Override
            protected void updateItem(Color item, boolean empty) {
                super.updateItem(item, empty);
                //TableRow<Point> currentRow = getTableRow();
                if (item != null) {
                    double r = item.getRed() * 255;
                    double g = item.getGreen() * 255;
                    double b = item.getBlue() * 255;
                    this.setStyle("-fx-background-color: rgb(" + r + ", " + g + ", " + b + ");");

                }

            }
        });

        tableView.setItems(data);

        data.add(planning.getWarehouse());

        planning.getRequests().forEach(request -> {
            data.add(request.getDeliveryPoint());
            data.add(request.getPickupPoint());
        });
    }

    public void undoAction(ActionEvent actionEvent) {
    }

    public void redoAction(ActionEvent actionEvent) {
    }
}
