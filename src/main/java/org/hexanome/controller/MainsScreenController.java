package org.hexanome.controller;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hexanome.data.ExceptionXML;
import org.hexanome.model.*;
import org.hexanome.vue.AlertBox;
import org.hexanome.vue.App;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
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
    protected final AddRequestState1 addRequestState1 = new AddRequestState1();
    protected final AddRequestState2 addRequestState2 = new AddRequestState2();
    protected final AddRequestState3 addRequestState3 = new AddRequestState3();
    protected final AddRequestState4 addRequestState4 = new AddRequestState4();
    /*protected final ModifyRequestState modifyRequestState = new ModifyRequestState();
    protected final DeleteRequestState deleteRequestState = new DeleteRequestState();*/

    // current state
    private State currentState = initialState;

    /* Création de la carte Gluon JavaFX */
    private CustomMap mapView;
    private CustomMapLayer requestLayer;
    private CustomMapLayer tourLayer;
    private CustomMapLayer intersectionLayer;

    //Declaration of the interactive buttons in the mainsScreen.fxml
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
    private TableColumn<Point, String> columnArrivalTime;
    @FXML
    private TableColumn<Point, Color> columnColor;
    /*@FXML
    private TableColumn<Point, Button> columnDelete;*/
    @FXML
    private TableColumn<Point, String> columnDepartureTime;
    @FXML
    private TableColumn<Point, String> columnID;
    /*@FXML
    private TableColumn<Point, Button> columnModify;*/
    @FXML
    private TableColumn<Point, String> columnType;
    /*@FXML
    private Button btnDeleteTableRow;
    @FXML
    private Button btnEditTableRow;*/

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

    }

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

    public void setMapView(CustomMap mapView) {
        this.mapView = mapView;
    }

    public CustomMapLayer getRequestLayer() {
        return requestLayer;
    }

    public void setRequestLayer(CustomMapLayer requestLayer) {
        this.requestLayer = requestLayer;
    }

    public CustomMapLayer getTourLayer() {
        return tourLayer;
    }

    public void setTourLayer(CustomMapLayer tourLayer) {
        this.tourLayer = tourLayer;
    }

    public Button getBtnLoadRequest() {
        return btnLoadRequest;
    }

    public void setBtnLoadRequest(Button btnLoadRequest) {
        this.btnLoadRequest = btnLoadRequest;
    }

    public Button getBtnRedo() {
        return btnRedo;
    }

    public void setBtnRedo(Button btnRedo) {
        this.btnRedo = btnRedo;
    }

    public Button getBtnUndo() {
        return btnUndo;
    }

    public void setBtnUndo(Button btnUndo) {
        this.btnUndo = btnUndo;
    }

    public HBox getMapContainer() {
        return mapContainer;
    }

    public void setMapContainer(HBox mapContainer) {
        this.mapContainer = mapContainer;
    }

    public TableView<Point> getTableView() {
        return tableView;
    }

    public void setTableView(TableView<Point> tableView) {
        this.tableView = tableView;
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
        System.out.println(selectedFile);
        if (selectedFile == null) {
            AlertBox.displayAlert("Message d'erreur", "Veuillez sélectionner un fichier");
        } else {
            // We clear the map before loading an XML file with requests
            map.clearMap();
            try {
                currentState.loadMap(this, map, selectedFile);
                // init the map
                this.initMap();
            } catch (ExceptionXML | ParserConfigurationException | IOException | SAXException e) {
                if (e.getMessage() == "Wrong format")
                    e.printStackTrace();
                AlertBox.displayAlert("Message d'erreur", "Le fichier n'est pas valide pour cette action");
            }
            currentState.enableButton(this);
        }
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
        System.out.println(selectedFile);

        if (selectedFile == null) {
            System.out.println("pas de fichier selectionné");
        } else {

            // We clear the requestLayer before loading an XML file with requests
            planning.clearPlanning();

            try {
                currentState.loadPlanning(this, map, planning, selectedFile);
                // update the request layer
                this.initRequestLayer();
            } catch (ExceptionXML | ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
            currentState.enableButton(this);
        }

    }


    /**
     * Method be called every time button "add request" is handled
     *
     * @param actionEvent
     * @return void
     */
    public void addRequest(ActionEvent actionEvent) {
        currentState.addRequest(this);

        currentState.enableButton(this);
    }

    /**
     * Method to be called every time button compute tour is handled
     *
     * @param actionEvent
     * @return void
     */
    public void computeTour(ActionEvent actionEvent) {
        tour = new Tour(new ArrayList<>(), null, this, planning.getWarehouse().getDepartureTime(), map.getMatAdj());

        // we compute the tour
        currentState.computeTour(this, map, planning, tour);
        // this.updateTourLayer();
        System.out.println("End of Compute Tour");
        currentState.enableButton(this);
    }

    public void rightClick() {
        this.currentState.rightClick(this);
        System.out.println("rightclick");
        System.out.println(this.currentState);
        currentState.enableButton(this);
    }

    public void leftClick(Intersection i) {
        this.currentState.leftClick(this, i);
        this.currentState.enableButton(this);
        this.currentState.showDialogBox(this);
    }

    public void cancel() {
        this.currentState.cancel(this);
        this.currentState.enableButton(this);
        this.currentState.showDialogBox(this);
    }

    public void validate(int duration) {
        this.currentState.validate(this, duration, listOfCommands);
        this.currentState.enableButton(this);
        this.currentState.showDialogBox(this);
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
                System.out.println("Update map called");
                Platform.runLater(new Runnable() {
                    private MainsScreenController myController;

                    public Runnable init(MainsScreenController myParam) {
                        this.myController = myParam;
                        return this;
                    }

                    @Override
                    public void run() {
                        myController.updateDynamycMap();
                    }
                }.init(this));
                //this.updateDynamycMap();
                try {
                    System.out.println("updtaMapView222 Called");
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


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
        /*columnModify.setCellValueFactory(new PropertyValueFactory<>("modify"));
        columnDelete.setCellValueFactory(new PropertyValueFactory<>("delete"));
*/
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
        //mapView.removeLayer(tourLayer);

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
            System.out.println("onMousedetect");
            //Pour les layers de request
            this.updateMapView();

        });
    }

    public void updateDynamycMap() {
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
        //requestLayer.forceReRender();
        //tourLayer.forceReRender();
        mapView.layout();

        System.out.println("updtaMapView Called");
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
        Point selectedItem = tableView.getSelectionModel().getSelectedItem();
        String typeItem = selectedItem.getType();
        Color colorItem = selectedItem.getColor();
        planning.getRequests().forEach(request -> {
            DeliveryPoint delivery = request.getDeliveryPoint();
            PickupPoint pickup = request.getPickupPoint();
            System.out.println("Delivery " + delivery);
            System.out.println("Pickup " + pickup);
            Color colorDelivery = request.getDeliveryPoint().getColor();
            System.out.println("DeliveryColor : " + colorDelivery);
            Color colorPickup = request.getPickupPoint().getColor();
            System.out.println("PickupColor " + colorPickup);
            Boolean equals = colorDelivery.equals(colorPickup);
            Boolean colorEquals = colorDelivery.equals(colorItem);
            System.out.println("Equals? " + equals);
            System.out.println(colorEquals);
            if (colorEquals == true){
                planning.removeRequest(request);
                System.out.println("Request que se elimina : " + request);
            }else{
                System.out.println("No se elimina ninguna request");
            }
        });
        tableView.getItems().remove(selectedItem);
        updateTableView();
        updateTourLayer();
    }

    @FXML
    void editTableRow(ActionEvent event) {
        //Modify departure time , arrival time and point in the map

        Point selectedItem = tableView.getSelectionModel().getSelectedItem();
        String typeItem = selectedItem.getType();

        switch (typeItem){
            case("warehouse"):
                //getWarehouse()->PlanningRequest
                break;
        }
        //tableView.getSelectionModel();
        //tableView.setOnKeyReleased();
        System.out.println(tableView.getSelectionModel().getSelectedItem());
    }
}
