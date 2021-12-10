package org.hexanome.controller;

import javafx.scene.paint.Color;
import org.hexanome.model.Intersection;
import org.hexanome.model.Point;
import org.hexanome.model.Request;
import org.hexanome.model.Tour;

import java.util.ArrayList;

public class ModifyRequestState implements State {
    /*
            Missing tableview is not updating
            Missing refresh points in map
         */
    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            System.out.println(i);
            Point pointToUpdate = controller.getTableView().getSelectionModel().getSelectedItem();
            pointToUpdate.setAddress(i);
            controller.getMapView().removeLayer(controller.getIntersectionLayer());
            controller.setCurrentState(controller.tourState);
            controller.updateTableView();
            controller.initRequestLayer();
            controller.updateTourLayer();
            controller.tour = new Tour(new ArrayList<>(), null, controller, controller.planning.getWarehouse().getDepartureTime(), controller.map.getMatAdj());
            // we must reset the command list
            controller.listOfCommands.reset();
            // we compute the tour
            controller.currentState.computeTour(controller, controller.map, controller.planning, controller.tour);
            controller.updateMapView();


        }
    }


    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(false);
        controller.getBtnAddRequest().setDisable(false);
        controller.getBtnRedo().setDisable(false);
        controller.getBtnUndo().setDisable(false);
        controller.getBtnStopCalcul().setDisable(false);
        controller.getBtnDeleteTableRow().setDisable(false);
        controller.getBtnEditTableRow().setDisable(false);
    }
}
