package org.hexanome.controller;

import javafx.scene.paint.Color;
import org.hexanome.model.*;

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
    public void leftClick(MainsScreenController controller, Intersection i) {
        if (i != null) {
            // we change the selected intersection in the next state
            controller.addRequestState4.setPickUp(pickUp);
            controller.addRequestState4.setDelivery(i);

            // we change the duration
            controller.addRequestState4.setPickUpDuration(pickUpDuration);

            // we change the state of the controller
            controller.setCurrentState(controller.addRequestState4);
        } else {

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
    }

}
