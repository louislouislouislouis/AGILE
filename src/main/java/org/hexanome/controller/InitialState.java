package org.hexanome.controller;

import com.gluonhq.maps.MapPoint;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.model.MapIF;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class InitialState implements State {
    // initial state

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
        controller.btnLoadRequest.setDisable(true);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(true);
    }
}
