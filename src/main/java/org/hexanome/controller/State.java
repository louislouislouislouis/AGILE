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
    /**
     * Method called by the controller after a click on the button undo
     *
     * @param listOfCommands the list of commands
     */
    default void undo(ListOfCommands listOfCommands) {
    }

    /**
     * Method called by the controller after a click on the button redo
     *
     * @param listOfCommands the list of commands
     */
    default void redo(ListOfCommands listOfCommands) {
    }

    /**
     * Method called by the controller after a right click
     *
     * @param controller the controler
     */
    default void rightClick(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a left click
     *
     * @param controller the controler
     * @param i          the intersection
     */
    default void leftClick(MainsScreenController controller, Intersection i) throws Exception {
    }

    /**
     * Method called by the controller after a click on the button Add request
     *
     * @param controller the controler
     */
    default void addRequest(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a click on the button edit request
     *
     * @param controller the controler
     */
    default void modifyRequest(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a click on the button delete request
     *
     * @param controller     the controller
     * @param listOfCommands the list of commands
     * @param selectedItem   the selected point to delete
     */
    default void deleteRequest(MainsScreenController controller, Point selectedItem, ListOfCommands listOfCommands) {
    }

    /**
     * Method called by the controller after a click on the button Load Map
     *
     * @param controller   the controller
     * @param map          the object representing the map
     * @param selectedFile the selected file to parse
     */
    default void loadMap(MainsScreenController controller, MapIF map, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    }

    /**
     * Method called by the controller after a click on the button Load request
     *
     * @param controller      the controller
     * @param map             the object representing the map
     * @param selectedFile    the selected file to parse
     * @param planningRequest the object representing the planning
     */
    default void loadPlanning(MainsScreenController controller, MapIF map, PlanningRequest planningRequest, File selectedFile
    ) throws ExceptionXML, ParserConfigurationException, IOException, SAXException {
    }

    /**
     * Method called by the controller after a click on the button compute tour
     *
     * @param controller      the controller
     * @param map             the object representing the map
     * @param tour            the object representing the tour
     * @param planningRequest the object representing the planning
     */
    default void computeTour(MainsScreenController controller, MapIF map, PlanningRequest planningRequest, Tour tour) {
    }

    /**
     * Method called by the controller after we have a new current state to enable the button of the current state
     *
     * @param controller the controller
     */
    default void enableButton(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a click on a cancel button
     *
     * @param controller the controller
     */
    default void cancel(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a click on a validate button
     *
     * @param controller     the controller
     * @param duration       the duration of the point
     * @param listOfCommands the lsit of commands
     */
    default void validate(MainsScreenController controller, int duration, ListOfCommands listOfCommands) throws Exception {
    }

    /**
     * Method called by the controller after we have a new current state
     *
     * @param controller the controller
     */
    default void showDialogBox(MainsScreenController controller) {
    }

    /**
     * Method called by the controller after a click on the stop button
     *
     * @param controller the controller
     */
    default void finishCompute(MainsScreenController controller) {
    }
}
