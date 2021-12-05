package org.hexanome.controller;

import javafx.scene.paint.Color;
import org.hexanome.model.*;

public class AddRequestState2 implements State {

    Intersection selectedIntersection;

    public void setSelectedIntersection(Intersection i) {
        selectedIntersection = i;
    }

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) {
        System.out.println(i);

        if (i != null) {
            Color color = ColorEnum.values()[controller.getPlanning().getRequests().size()].color;

            // we lack 2 states for each duration time

            PickupPoint pickupPoint = new PickupPoint(selectedIntersection, 10, color);
            DeliveryPoint deliveryPoint = new DeliveryPoint(i, 10, color);

            Request request = new Request(pickupPoint, deliveryPoint);

            controller.getPlanning().addRequest(request);
            
            // we change the state of the controller
            controller.setCurrentState(controller.tourState);
        } else {

        }
    }

    @Override
    public void rightClick(MainsScreenController controller) {
        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(true);
        controller.getBtnLoadMap().setDisable(true);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(true);
    }

}
