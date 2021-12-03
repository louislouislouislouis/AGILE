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
    // State after loading the planning
    // -> Wait for the user to load a planning
    // -> Wait for the user to load another map
    // -> Wait for the user to compute the tour

    @Override
    public void loadPlanning(MainsScreenController controller, MapIF map, PlanningRequest planning, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        // We initialize the deserializer
        RequestDeserializer mydomrequest = new RequestDeserializer();

        // we load the map
        mydomrequest.load(planning, selectedFile, map);

        // we change the state of the controller
        controller.setCurrentState(controller.planningState);
    }

    @Override
    public void loadMap(MainsScreenController controller, MapIF map, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
        // We initialize the deserializer
        MapDeserializer domMap = new MapDeserializer();

        // we load the map
        domMap.load(map, selectedFile);

        // we change the state of the controller
        controller.setCurrentState(controller.mapState);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.btnLoadRequest.setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(false);
        controller.getBtnAddRequest().setDisable(true);
    }
}
