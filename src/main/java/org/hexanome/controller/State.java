package org.hexanome.controller;

import org.hexanome.data.ExceptionXML;
import org.hexanome.model.Intersection;
import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Tour;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public interface State {
    default void undo(MainsScreenController controller) {
    }


    default void redo(MainsScreenController controller) {
    }


    default void rightClick(MainsScreenController controller) {
    }


    default void leftClick(MainsScreenController controller, Intersection i) {
    }


    default void addRequest(MainsScreenController controller) {
    }


    default void modifyRequest(MainsScreenController controller) {
    }


    default void deleteRequest(MainsScreenController controller) {
    }


    default void loadMap(MainsScreenController controller, MapIF map, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    }


    default void loadPlanning(MainsScreenController controller, MapIF map, PlanningRequest planningRequest, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    }


    default void computeTour(MainsScreenController controller, MapIF map, PlanningRequest planningRequest, Tour tour) {
    }


    default void enableButton(MainsScreenController controller) {
    }

    default void cancel(MainsScreenController controller) {
    }

    default void validate(MainsScreenController controller, int duration) {
    }

    default void showDialogBox(MainsScreenController controller) {
    }
}
