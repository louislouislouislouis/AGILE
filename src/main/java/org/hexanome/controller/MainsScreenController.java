package org.hexanome.controller;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.model.MapIF;
import org.hexanome.vue.App;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

public class MainsScreenController {
    @FXML
    HBox mapContainer;

    private MapIF map = new MapIF();

    /*---------------------------VARIABLES------------------------------------------------------------*/

    //Declaration of the buttons interactives in the mainsScreen.fxml
    @FXML private Button btnLoadMap;
    @FXML private Button btnAddRequest;
    @FXML private Button btnValidateRoute;
    private static class xmlFile{

    }

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
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);
        if(selectedFile.exists()){
            btnAddRequest.setDisable(false);
            btnValidateRoute.setDisable(false);
        }else{
            btnAddRequest.setDisable(true);
            btnValidateRoute.setDisable(true);
        }
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
        //method that uploads an XML file with the command
        File selectedFile = fileChooser(actionEvent);
        System.out.println(selectedFile);

        //Pour trouver les fichers XML qui ont les request
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

    public boolean validationMap(File selectedFile){
        //methode pour trouver les fichers XML qui sont sans request
        boolean validationMap = false;
        return validationMap;
    }
    public boolean validationPlaningRequest(File selectedFile){
        //methode pour trouver les fichers XML qui ont les request
        boolean validationPlaningRequest = false;
        return validationPlaningRequest;
    }
<<<<<<< Updated upstream
    /**@FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    } **/
}
=======

    /**
     * used to Update the map
     *
     * @param o observable
     * @arg arg
     */
    @Override
    public void update(Observable o, Object arg) {
        String action = (String) arg;
        switch (action) {
            case "UPDATEMAP":
                Platform.runLater(new Runnable() {
                    private MainsScreenController myController;

                    public Runnable init(MainsScreenController myParam) {
                        this.myController = myParam;
                        return this;
                    }

                    @Override
                    public void run() {
                        myController.updateDynamicMap();
                        myController.updateTableView();
                    }
                }.init(this));

                break;
            default:

        }

    }

    /**
     * this method update the layer with intersections
     */
    public void updateMapIntersection() {
        // Add all the intersection to the layer
        intersectionLayer = new CustomMapLayer();

        HashMap<Long, Intersection> intersectionMap = map.getIntersections();

        intersectionMap.forEach((id, intersection) -> {
            MapPoint mapPoint = new MapPoint(intersection.getLatitude(), intersection.getLongitude());
            intersectionLayer.addPoint(id, mapPoint);
        });

        intersectionLayer.intersectionEvent(this, map);

        mapView.addLayer(intersectionLayer);

        intersectionLayer.forceReRender();


    }

    /**
     * this method update the table view on the GUI
     */
    public void updateTableView() {
        // cell factory

        columnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnType.setCellValueFactory(new PropertyValueFactory<>("type"));
        columnArrivalTime.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        columnDepartureTime.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        columnColor.setCellValueFactory(new PropertyValueFactory<>("color"));

        //set editable columns
        columnArrivalTime.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter())); //allows editing when the cell is double clicked
        columnDepartureTime.setCellFactory(TextFieldTableCell.forTableColumn(new LocalTimeStringConverter()));
        //columnType.setCellFactory(TextFieldTableCell.forTableColumn());

        columnColor.setCellFactory(tv -> new TableCell<Point, Color>() {
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

        // we delete the previous data to avoid duplicate
        data.clear();

        data.add(planning.getWarehouse());

        planning.getRequests().forEach(request -> {
            data.add(request.getDeliveryPoint());
            data.add(request.getPickupPoint());
        });
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null)
                requestLayer.animateOnePoint(newSelection.getId());
        });
    }

    /**
     * this method initialize the map
     */
    public void initMap() {
        // clear possible layer
        mapView.removeLayer(requestLayer);
        tourLayer.resetAll();

        /* Zoom de 5 */
        mapView.setZoom(13.5);

        /* creation of the mapPoint on which the camera will be centered
         *  We use the longitude and latitude of Lyon
         * */
        MapPoint mapPointCamera = new MapPoint(45.754676, 4.866590);

        /* Centre la carte sur le point */
        mapView.flyTo(0, mapPointCamera, 0.1);

        //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
        mapContainer.getChildren().clear();
        mapContainer.getChildren().add(mapView);

        //Force rerender (Bug fix - Gluon Maps Issue drag)
        mapView.setOnMouseReleased(e -> {

            if (e.getButton() == MouseButton.SECONDARY) {
                this.rightClick();
            } else if (e.getButton() == MouseButton.PRIMARY) {
                this.leftClick(null);
            }
            //Pour les layers de request
            this.updateMapView();

        });
    }

    /**
     * this method update the layer with the dynamic tour
     */
    public void updateDynamicMap() {
        tourLayer.resetAll();

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

        tourLayer.tourLineHover();
        tourLayer.forceReRender();
        //mapView.layout();

        updateTimeAndDistance();
    }

    /**
     * this method update the map view
     */
    public void updateMapView() {
        mapView.layout();
    }

    /**
     * this method update the tour layer
     */
    public void updateTourLayer() {
        // clear
        mapView.removeLayer(tourLayer);

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

        tourLayer.tourLineHover();

        mapView.removeLayer(requestLayer);
        mapView.addLayer(tourLayer);
        mapView.addLayer(requestLayer);

        updateTimeAndDistance();
    }

    /**
     * this method update the layer with intersections
     */
    public void updateRequestLayer() {
        mapView.removeLayer(requestLayer);
        requestLayer.resetAll();
        tourLayer.resetAll();
        //Create the Request Layer

        requestLayer = new CustomMapLayer();

        //Add the warehouse
        Intersection warehouse = planning.getWarehouse().getAddress();
        requestLayer.addSpecialPointRectangle(warehouse.getIdIntersection(),
                new MapPoint(warehouse.getLatitude(), warehouse.getLongitude()), planning.getWarehouse().getColor());

        //Add the requests
        for (Request request : planning.getRequests()) {
            Intersection deliveryInt = request.getDeliveryPoint().getAddress();
            MapPoint mapPointDelivery = new MapPoint(deliveryInt.getLatitude(), deliveryInt.getLongitude());
            requestLayer.addSpecialPointCircle(deliveryInt.getIdIntersection(), mapPointDelivery, request.getDeliveryPoint().getColor());

            Intersection pickupInt = request.getPickupPoint().getAddress();
            MapPoint mapPointPickup = new MapPoint(pickupInt.getLatitude(), pickupInt.getLongitude());
            requestLayer.addSpecialPointRectangle(pickupInt.getIdIntersection(), mapPointPickup, request.getPickupPoint().getColor());
        }

        // enable to scroll to selected point from the map

        requestLayer.scrollToPointEvent(tableView);

        mapView.addLayer(requestLayer);

        requestLayer.forceReRender();
    }

    /**
     * this method is used to deletr a request
     */
    @FXML
    void deleteTableRow(ActionEvent event) {
        //Delete point in the map
        //If table row not selectet
        Point selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            this.currentState.deleteRequest(this, selectedItem, listOfCommands);
        } else {
            new ExceptionBox("Please select the point you want to delete", "Null").display();
        }
    }

    /**
     * this method edit a request
     */
    @FXML
    void editTableRow(ActionEvent event) {
        //Modify departure time , arrival time and point in the map
        tableView.setEditable(true);
        currentState.modifyRequest(this);
    }

    /**
     * this method enable the user to stop the computation
     */
    public void stopTour(ActionEvent actionEvent) {
        currentState.finishCompute(this);
    }

    public void changeCursor(String w) {
        if (w.equals("W")) {
            btnAddRequest.getScene().getRoot().setCursor(Cursor.WAIT);
        } else if (w.equals("N")) {
            btnAddRequest.getScene().getRoot().setCursor(Cursor.DEFAULT);
        }
    }

    /**
     * this method update the time and distance displayed on the GUI
     */
    public void updateTimeAndDistance() {
        int distance = tour.getCost().intValue();
        LocalTime duration;

        duration = LocalTime.ofSecondOfDay(planning.getWarehouse().getArrivalTime().toSecondOfDay() - planning.getWarehouse().getDepartureTime().toSecondOfDay());

        StringBuilder distanceString = new StringBuilder();
        if (distance % 1000 == 0) {
            distanceString.append("000");
        } else if (distance % 1000 > 0 && distance % 1000 < 10) {
            distanceString.append("00");
            distanceString.append(distance % 1000);
        } else if (distance % 1000 >= 10 && distance % 1000 < 100) {
            distanceString.append("0");
            distanceString.append(distance % 1000);
        } else {
            distanceString.append(distance % 1000);
        }

        durationLabel.setText("Duration : " + (duration.getHour() == 0 ? "" : duration.getHour() + " h ") + duration.getMinute() + " min");
        distanceLabel.setText("Distance : " + distance / 1000 + " " + distanceString + " m");
    }
}
>>>>>>> Stashed changes
