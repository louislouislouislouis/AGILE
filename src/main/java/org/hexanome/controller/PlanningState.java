package org.hexanome.controller;

import org.hexanome.controller.tsp.GraphAPI;
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
 * -> Wait for the user to launch the computing of the tour
 *
 * @author Gastronom'if
 */
public class PlanningState implements State {
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
    public void computeTour(MainsScreenController controller, MapIF map, PlanningRequest planning, Tour tour) {
        //method that calculates the most optimal path of the tour
        controller.setAllowcalculation(true);
        controller.getBtnStopCalcul().setDisable(false);
        controller.changeCursor("W");
        new Thread(new Runnable() {
            private PlanningRequest planning;
            private MapIF map;
            private Tour tour;
            private MainsScreenController ctrl;

            public Runnable init(PlanningRequest planning, MapIF map, Tour tour, MainsScreenController controller1) {
                this.planning = planning;
                this.map = map;
                this.tour = tour;
                this.ctrl = controller1;
                return this;
            }

            @Override
            public void run() {
                new GraphAPI().V1_TSP(this.planning, this.map, this.tour, ctrl);
                this.ctrl.setCurrentState(this.ctrl.tourState);
                this.ctrl.getBtnStopCalcul().setDisable(true);
                this.ctrl.setAllowcalculation(false);
                this.ctrl.changeCursor("N");
            }
        }.init(planning, map, tour, controller)).start();


        // we change the state of the controller
        controller.setCurrentState(controller.waitingComputeState);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(false);
        controller.getBtnAddRequest().setDisable(true);
        controller.getBtnRedo().setDisable(true);
        controller.getBtnUndo().setDisable(true);
        controller.getBtnStopCalcul().setDisable(true);
        controller.getBtnDeleteTableRow().setDisable(true);
        controller.getBtnEditTableRow().setDisable(true);
    }
}
