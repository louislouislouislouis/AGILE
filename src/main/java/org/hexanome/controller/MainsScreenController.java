package org.hexanome.controller;

import java.io.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.LocalTimeStringConverter;
import org.hexanome.data.ExceptionXML;
import org.hexanome.model.*;
import org.hexanome.vue.*;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.hexanome.vue.ExceptionBox;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;

import com.gluonhq.maps.MapPoint;

public class MainsScreenController implements Observer {

    /*---------------------------VARIABLES------------------------------------------------------------*/
    private MapIF map;
    private PlanningRequest planning;
    private Tour tour;

    private static final ObservableList<Point> data = FXCollections.observableArrayList();

    /* command variable */
    private ListOfCommands listOfCommands;

    /* state variable */

    // Instances associated with each possible state of the controller
    protected final InitialState initialState = new InitialState();
    protected final MapState mapState = new MapState();
    protected final PlanningState planningState = new PlanningState();
    protected final TourState tourState = new TourState();
    protected final ComputeWaitingState waitingComputeState = new ComputeWaitingState();
    protected final AddRequestState1 addRequestState1 = new AddRequestState1();
    protected final AddRequestState2 addRequestState2 = new AddRequestState2();
    protected final AddRequestState3 addRequestState3 = new AddRequestState3();
    protected final AddRequestState4 addRequestState4 = new AddRequestState4();
    protected final DeleteRequestState deleteRequestState = new DeleteRequestState();
    protected final ModifyRequestState modifyRequestState = new ModifyRequestState();

    // current state
    private State currentState = initialState;

    /* Création de la carte Gluon JavaFX */
    private CustomMap mapView;
    private CustomMapLayer requestLayer;
    private CustomMapLayer tourLayer;
    private CustomMapLayer intersectionLayer;


    //
    private Boolean allowcalculation;

    //Declaration of the interactive buttons in the mainsScreen.fxml
    @FXML
    private Button btnStopCalcul;
    @FXML
    private Button btnAddRequest;
    @FXML
    private Button btnLoadMap;
    @FXML
    private Button btnLoadRequest;
    @FXML
    private Button btnRedo;
    @FXML
    private Button btnUndo;
    @FXML
    private Button btnValidateRoute;
    @FXML
    HBox mapContainer;
    @FXML
    private TableView<Point> tableView;
    @FXML
    private TableColumn<Point, LocalTime> columnArrivalTime;
    @FXML
    private TableColumn<Point, Color> columnColor;
    /*@FXML
    private TableColumn<Point, Button> columnDelete;*/
    @FXML
    private TableColumn<Point, LocalTime> columnDepartureTime;
    @FXML
    private TableColumn<Point, String> columnID;
    /*@FXML
    private TableColumn<Point, Button> columnModify;*/
    @FXML
    private TableColumn<Point, String> columnType;
    @FXML
    private Button btnDeleteTableRow;
    @FXML
    private Button btnEditTableRow;
    @FXML
    private Spinner<LocalTime> spnrArrivalTime;
    @FXML
    private Spinner<LocalTime> spnrDepartureTime;

    /*----------------------Constructor---------------------------------------*/

    public MainsScreenController() {
        listOfCommands = new ListOfCommands();
        map = new MapIF();
        planning = new PlanningRequest();
        tour = new Tour();

        mapView = new CustomMap();
        requestLayer = new CustomMapLayer();
        tourLayer = new CustomMapLayer();
        intersectionLayer = new CustomMapLayer();

        mapView.addLayer(requestLayer);
        mapView.addLayer(tourLayer);
        mapView.addLayer(intersectionLayer);
        this.allowcalculation = false;

    }

    /*-------------------------GETTERS AND SETTERS-----------------------------------------------------*/
    public Boolean isAllowcalculation() {
        return allowcalculation;
    }

    public void setAllowcalculation(boolean allowcalculation) {
        this.allowcalculation = allowcalculation;
    }

    public Button getBtnStopCalcul() {
        return btnStopCalcul;
    }

    public Button getBtnDeleteTableRow() {
        return btnDeleteTableRow;
    }

    public Button getBtnEditTableRow() {
        return btnEditTableRow;
    }

    public Button getBtnLoadMap() {
        return btnLoadMap;
    }

    public Button getBtnAddRequest() {
        return btnAddRequest;
    }

    public Button getBtnValidateRoute() {
        return btnValidateRoute;
    }


    public MapIF getMap() {
        return map;
    }

    public void setMap(MapIF map) {
        this.map = map;
    }

    public PlanningRequest getPlanning() {
        return planning;
    }

    public void setPlanning(PlanningRequest planning) {
        this.planning = planning;
    }

    public Tour getTour() {
        return tour;
    }

    public void setTour(Tour tour) {
        this.tour = tour;
    }

    public CustomMap getMapView() {
        return mapView;
    }

    public Button getBtnLoadRequest() {
        return btnLoadRequest;
    }

    public Button getBtnRedo() {
        return btnRedo;
    }

    public Button getBtnUndo() {
        return btnUndo;
    }

    public CustomMapLayer getIntersectionLayer() {
        return intersectionLayer;
    }

    /*--------------------------------Methods----------------------------------------------------------*/

    /**
     * Change the current state of the controller
     *
     * @param state the new current state
     */
    protected void setCurrentState(State state) {
        currentState = state;
        currentState.enableButton(this);
        currentState.showDialogBox(this);
    }

    /**
     * Method be called everytime button load Map is Handled
     *
     * @param actionEvent
     * @return void
     */
    public void selectionMap(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);
        if (selectedFile == null) {
            new ExceptionBox("Veuillez sélectionner un fichier", "Null").display();
        } else {
            // We clear the map before loading an XML file with requests
            map.clearMap();
            try {
                currentState.loadMap(this, map, selectedFile);
                // init the map

            } catch (ExceptionXML | ParserConfigurationException | IOException | SAXException e) {
                new ExceptionBox(e, "XML").display();
            }
        }
        this.initMap();
    }

    /**
     * Method be called everytime button load Request is Handled
     *
     * @param actionEvent
     * @return void
     */
    public void loadRequest(ActionEvent actionEvent) {
        //method that uploads an XML file (carte)
        File selectedFile = fileChooser(actionEvent);

        if (selectedFile == null) {
            new ExceptionBox("Veuillez sélectionner un fichier", "Null").display();
        } else {

            // We clear the requestLayer before loading an XML file with requests
            planning.clearPlanning();

            try {
                currentState.loadPlanning(this, map, planning, selectedFile);
            } catch (ExceptionXML | ParserConfigurationException | IOException | SAXException e) {
                new ExceptionBox(e, "XML").display();
            }
        }
        this.initRequestLayer();
    }


    /**
     * Method be called every time button "add request" is handled
     *
     * @param actionEvent
     * @return void
     */
    public void addRequest(ActionEvent actionEvent) {
        currentState.addRequest(this);
    }

    /**
     * Method to be called every time button compute tour is handled
     * This method call the method computeTour of the current state and reset the listOfCommands
     *
     * @param actionEvent
     * @return void
     */
    public void computeTour(ActionEvent actionEvent) {
        tour = new Tour(new ArrayList<>(), null, this, planning.getWarehouse().getDepartureTime(), map.getMatAdj());

        // we must reset the command list
        listOfCommands.reset();

        // we compute the tour
        currentState.computeTour(this, map, planning, tour);
    }

    public void rightClick() {
        this.currentState.rightClick(this);
    }

    public void leftClick(Intersection i) {
        try {
            this.currentState.leftClick(this, i);
        } catch (Exception e) {
            new ExceptionBox(e, "Other").display();
        }
    }

    public void cancel() {
        this.currentState.cancel(this);
    }

    public void validate(int duration) {
        try {
            this.currentState.validate(this, duration, listOfCommands);
        } catch (Exception e) {
            new ExceptionBox(e, "Other").display();
        }
    }

    /**
     * Method called by window after a click on the button "Undo"
     */
    public void undo(ActionEvent actionEvent) {
        currentState.undo(listOfCommands);
    }

    /**
     * Method called by window after a click on the button "Redo"
     */
    public void redo(ActionEvent actionEvent) {
        currentState.redo(listOfCommands);
    }


    /**
     * x
     * Method called to open a Navigation File
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

    /**
     * used to Update the map
     *
     * @param o observable
     * @arg arg
     */
    @Override
    public void update(Observable o, Object arg) {
        //System.out.println("Une fonction d'actualisation à été appelé");
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
                System.out.println("Unknown method");

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
    }


    public void initMap() {
        // clear possible layer
        mapView.removeLayer(requestLayer);
        tourLayer.resetAll();

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

            if (e.getButton() == MouseButton.SECONDARY) {
                this.rightClick();
            } else if (e.getButton() == MouseButton.PRIMARY) {
                this.leftClick(null);
            }
            //Pour les layers de request
            this.updateMapView();

        });
    }

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


    }

    public void updateMapView() {
        mapView.layout();
    }

    public void updateTourLayer() {
        // clear
        mapView.removeLayer(tourLayer);

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

        tourLayer.tourLineHover();

        mapView.removeLayer(requestLayer);
        mapView.addLayer(tourLayer);
        mapView.addLayer(requestLayer);

        //tour.notifyChange("UPDATEMAP");
    }

    public void initRequestLayer() {
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

    @FXML
    void editTableRow(ActionEvent event) {
        //Modify departure time , arrival time and point in the map
        tableView.setEditable(true);
        currentState.modifyRequest(this);

        //get old data
        Point selectedItem = tableView.getSelectionModel().getSelectedItem();
        System.out.println(selectedItem);
        String id = selectedItem.getId().toString();
        String type = selectedItem.getType();
        LocalTime arrivalTime = selectedItem.getArrivalTime();
        LocalTime departureTime = selectedItem.getDepartureTime();
        Color color = selectedItem.getColor();

        //if(selectedItem.){}
        //Point replacePoint = Point()
        //tableView.change
        Point newPoint = tableView.getSelectionModel().getSelectedItem();
        System.out.println(newPoint);
        //String newId = selectedItem.getId().toString();
        //String newType = selectedItem.getType();
        LocalTime newArrivalTime = selectedItem.getArrivalTime();
        LocalTime newDepartureTime = selectedItem.getDepartureTime();
        //Color color = selectedItem.getColor();


    }

    public void stopTour(ActionEvent actionEvent) {
        currentState.finishCompute(this);
    }

    public void changeCursor(String w) {
        System.out.println("VERIFY CURSOR IF NEEDED");
        if (w.equals("W")) {
            System.out.println(btnAddRequest.getScene().getRoot());
            btnAddRequest.getScene().getRoot().setCursor(Cursor.WAIT);
        } else if (w.equals("N")) {
            System.out.println(btnAddRequest.getScene().getRoot());
            btnAddRequest.getScene().getRoot().setCursor(Cursor.DEFAULT);
        }
    }

    public void updateColorRequest() {
        LinkedList<Request> requests = planning.getRequests();
        for (int i = 0; i < requests.size(); i++) {
            Request request = requests.get(i);
            Color color = ColorEnum.values()[i].color;

            request.getPickupPoint().setColor(color);
            request.getDeliveryPoint().setColor(color);
        }
    }
}