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


import org.hexanome.data.ExceptionXML;
import org.hexanome.data.MapDeserializer;
import org.hexanome.data.RequestDeserializer;
import org.hexanome.model.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * This class is the representation of map state
 * <p>
 * State after entering the method loadMap from the initial state
 * <p>
 * -> Wait for the user to load a planning from a xml file
 * <p>
 * -> Wait for the user to load another map from a xml file
 *
 * @author Gastronom'if
 */
public class ComputeWaitingState implements State {
    public void finishCompute(MainsScreenController controller) {
        controller.setAllowcalculation(false);

        controller.changeCusror("N");
        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
        System.out.println("in ComputeWaitings signal to stop");
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(true);
        controller.getBtnLoadMap().setDisable(true);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(true);
        controller.getBtnRedo().setDisable(true);
        controller.getBtnUndo().setDisable(true);
        controller.getBtnStopCalcul().setDisable(false);
        controller.getBtnDeleteTableRow().setDisable(true);
        controller.getBtnEditTableRow().setDisable(true);
    }
}
