package org.hexanome.controller;

import javafx.scene.paint.Color;
import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * This class is the representation of planning state
 * <p>
 * State after entering the method loadPlanning from the map state
 * <p>
 * -> Wait for the user to load another planning from a xml file
 * <p>
 * -> Wait for the user to load another map from a xml file
 * <p>
 * -> Wait for the user to add a request
 * <p>
 * -> Wait for the user to modify a request
 * <p>
 * -> Wait for the user to delete a request
 *
 * @author Gastronom'if
 */
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
        controller.getBtnRedo().setDisable(false);
        controller.getBtnUndo().setDisable(false);
    }

    @Override
    public void deleteRequest(MainsScreenController controller, Point selectedItem, ListOfCommands listOfCommands) {
        Color colorItem = selectedItem.getColor();
        controller.getPlanning().getRequests().forEach(request -> {
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
            if (colorEquals == true) {
                // we add the command to the list of command
                // it will be executed there
                listOfCommands.add(new DeleteRequestCommand(controller, request));
                System.out.println("Request que se elimina : " + request);
            } else {
                System.out.println("No se elimina ninguna request");
            }
        });
    }

    @Override
    public void undo(ListOfCommands listOfCommands) {
        listOfCommands.undo();
    }

    @Override
    public void redo(ListOfCommands listOfCommands) {
        listOfCommands.redo();
    }

}
