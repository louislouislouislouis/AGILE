package org.hexanome.controller;

import org.hexanome.model.Intersection;

/**
 * This class is the representation of the first AddRequestState
 * <p>
 * State after entering the method addRequest from TourState
 * <p>
 * -> Wait for the user leftClick on a first point (corresponding to the pickup point)
 * <p>
 * -> Wait for the user to rightClick to come back to the tour state
 *
 * @author Gastronom'if
 */
public class AddRequestState1 implements State {
    //

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            // we must check if the intersection isn't isolated
            if (controller.getMap().isIsolated(i.getIdIntersection(), controller.getPlanning().getWarehouse().getId())) {
                throw new Exception("ISOLATED POINT");
            } else {
                // we change the selected intersection in the next state
                controller.addRequestState2.setPickUp(i);

                // we change the state of the controller
                controller.setCurrentState(controller.addRequestState2);
            }
        }
    }

    @Override
    public void rightClick(MainsScreenController controller) {
        // we clear the view
        controller.getMapView().removeLayer(controller.getIntersectionLayer());

        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(true);
        controller.getBtnLoadMap().setDisable(true);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(true);
        controller.getBtnRedo().setDisable(true);
        controller.getBtnUndo().setDisable(true);
        controller.getBtnStopCalcul().setDisable(true);
        controller.getBtnDeleteTableRow().setDisable(true);
        controller.getBtnEditTableRow().setDisable(true);
    }
}
