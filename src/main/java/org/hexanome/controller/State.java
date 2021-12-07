package org.hexanome.controller;

import org.hexanome.data.ExceptionXML;
import org.hexanome.model.*;
import org.hexanome.vue.CustomMap;
import org.hexanome.vue.CustomMapLayer;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * State interface from the state pattern that declare all the methods used by the states
 *
 * @author Gastronom'if
 */
public interface State {
    default void undo(ListOfCommands listOfCommands) {
    }


    default void redo(ListOfCommands listOfCommands) {
    }


    default void rightClick(MainsScreenController controller) {
    }


    default void leftClick(MainsScreenController controller, Intersection i) {
    }


    default void addRequest(MainsScreenController controller) {
    }


    default void modifyRequest(MainsScreenController controller) {
    }


    default void deleteRequest(MainsScreenController controller, Point selectedItem, ListOfCommands listOfCommands) {
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

    default void validate(MainsScreenController controller, int duration, ListOfCommands listOfCommands) {
    }

    default void showDialogBox(MainsScreenController controller) {
    }

    default void finishCompute(MainsScreenController controller) {

    }
}
