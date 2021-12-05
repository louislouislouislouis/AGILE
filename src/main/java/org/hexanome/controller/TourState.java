package org.hexanome.controller;

import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class TourState implements State {
    // State after loading the map
    // -> Wait for the user to load a planning
    // -> Wait for the user to load another map
    // -> Wait for the user to add a request
    // -> Wait for the user to modify a request
    // -> Wait for the user to delete a request

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
    public void addRequest(MainsScreenController controller) {
        // on affiche les points des intersections sur la carte
        controller.updateMapIntersection();

        // we change the state of the controller
        controller.setCurrentState(controller.addRequestState1);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(false);
    }
}
