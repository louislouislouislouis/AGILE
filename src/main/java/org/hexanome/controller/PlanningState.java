package org.hexanome.controller;

import com.gluonhq.maps.MapPoint;
import javafx.scene.shape.Shape;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.*;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class PlanningState implements State {
    // State after loading the map
    // -> Wait for the user to load a planning or to load another map

    @Override
    public void loadPlanning(MainsScreenController controller, MapIF map, PlanningRequest planning, File selectedFile,
                             CustomMap mapView, CustomMapLayer requestLayer, CustomMapLayer tourLayer) {
        if (selectedFile != null) {
            // We initialize the deserializer
            RequestDeserializer mydomrequest = new RequestDeserializer();
            try {

                // We clear the requestLayer before loading an XML file with requests
                planning.clearPlanning();
                mapView.removeLayer(requestLayer);
                mapView.removeLayer(tourLayer);

                // we load the map
                mydomrequest.load(planning, selectedFile, map);


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

                HashMap<Long, Shape> shapeList = requestLayer.getShapeList();

                // enable to scroll to selected point from the map

                shapeList.forEach((id, shape) -> {
                    shape.setOnMouseClicked(mouseEvent -> {

                        for (int i = 0; i < controller.tableView.getItems().size(); i++) {
                            Point point = (Point) controller.tableView.getItems().get(i);
                            if (Objects.equals(point.getId(), id)) {
                                controller.tableView.scrollTo(i);
                                controller.tableView.getSelectionModel().select(i);
                                break;
                            }
                        }
                    });
                });
                mapView.addLayer(requestLayer);

                // we change the state of the controller
                controller.setCurrentState(controller.planningState);
            } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("aucun fichier choisi");
        }

    }

    @Override
    public void loadMap(MainsScreenController controller, MapIF map, File selectedFile,
                        CustomMap mapView, CustomMapLayer requestLayer, CustomMapLayer tourLayer) {

        if (selectedFile != null) {
            // We initialize the deserializer
            MapDeserializer domMap = new MapDeserializer();
            try {
                // We delete the map's content before loading the xml

                map.clearMap();
                mapView.removeLayer(requestLayer);
                mapView.removeLayer(tourLayer);

                // we load the map
                domMap.load(map, selectedFile);


                /* Zoom de 5 */
                mapView.setZoom(14);

                /* creation of the mapPoint on which the camera will be centered
                 *  We use the longitude and latitude of Lyon
                 * */
                MapPoint mapPointCamera = new MapPoint(45.760327, 4.876824);

                /* Centre la carte sur le point */
                mapView.flyTo(0, mapPointCamera, 0.1);

                //force la mise à jour de la carte en la supprimant et la rajoutant dans le conteneur
                controller.mapContainer.getChildren().clear();
                controller.mapContainer.getChildren().add(mapView);

                //Force rerender (Bug fix - Gluon Maps Issue drag)
                mapView.setOnMouseReleased(e -> {
                    System.out.println("onMousedetect");
                    //Pour les layers de request
                    requestLayer.forceReRender();
                    tourLayer.forceReRender();

                    //Pour le fond de la map
                    mapView.layout();

                });

                // we change the state of the controller
                controller.setCurrentState(controller.mapState);

            } catch (ParserConfigurationException | SAXException | IOException | ExceptionXML e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("aucun fichier choisi");
        }
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.btnLoadRequest.setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(false);
        controller.getBtnAddRequest().setDisable(false);
    }
}
