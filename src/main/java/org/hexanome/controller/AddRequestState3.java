package org.hexanome.controller;

import org.hexanome.model.*;

/**
 * This class is the representation of the third AddRequestState
 * <p>
 * State after entering the method validate() from AddRequest2
 * <p>
 * State must be initialized with the previous pickup Point and the pickup duration
 * <p>
 * -> Wait for the user leftClick on a second point (corresponding to the delivery point)
 * <p>
 * -> Wait for the user to rightClick to come back to the tour state
 *
 * @author Gastronom'if
 */
public class AddRequestState3 implements State {
    Intersection pickUp;

    int pickUpDuration;

    public void setPickUp(Intersection i) {
        pickUp = i;
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            // we must check if the intersection isn't isolated
            if (controller.getMap().isIsolated(i.getIdIntersection(), controller.getPlanning().getWarehouse().getId())) {
                throw new Exception("ISOLATED POINT");
            } else {
                // we change the selected intersection in the next state
                controller.addRequestState4.setPickUp(pickUp);
                controller.addRequestState4.setDelivery(i);

                // we change the duration
                controller.addRequestState4.setPickUpDuration(pickUpDuration);

                // we change the state of the controller
                controller.setCurrentState(controller.addRequestState4);
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
