package org.hexanome.controller;

import javafx.scene.control.TextInputDialog;
import javafx.scene.paint.Color;
import org.hexanome.model.*;

import java.util.Optional;

/**
 * This class is the representation of the forth AddRequestState
 * <p>
 * State after entering the method leftClick() from AddRequest3
 * <p>
 * State must be initialized with the previous delivery Point, pickup Point and the pickup duration
 * <p>
 * -> Wait for the user to validate a correct duration (positive), it will call AddRequestCommand
 * <p>
 * -> Wait for the user to cancel his input, it returns to the tour state
 *
 * @author Gastronom'if
 */
public class AddRequestState4 implements State {
    Intersection pickUp;
    Intersection delivery;
    int pickUpDuration;

    public void setPickUp(Intersection i) {
        pickUp = i;
    }

    public void setDelivery(Intersection i) {
        delivery = i;
    }

    public void setPickUpDuration(int pickUpDuration) {
        this.pickUpDuration = pickUpDuration;
    }

    @Override
    public void cancel(MainsScreenController controller) {
        // we clear the view
        controller.getMapView().removeLayer(controller.getIntersectionLayer());

        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }

    @Override
    public void validate(MainsScreenController controller, int duration, ListOfCommands listOfCommands) throws Exception {
        if (duration > 0) {
            Color color = ColorEnum.values()[controller.getPlanning().getRequests().size()].color;

            // we lack 2 states for each duration time

            PickupPoint pickupPoint = new PickupPoint(pickUp, pickUpDuration, color);
            DeliveryPoint deliveryPoint = new DeliveryPoint(delivery, duration, color);

            Request request = new Request(pickupPoint, deliveryPoint);

            // update planning
            //controller.getPlanning().addRequest(request);

            // add command to listOfCommand
            listOfCommands.add(new AddRequestCommand(controller, request));


            controller.getMapView().removeLayer(controller.getIntersectionLayer());

            // we change the state of the controller
            controller.setCurrentState(controller.tourState);
        } else {
            // we clear the view
            controller.getMapView().removeLayer(controller.getIntersectionLayer());
            // we change the state of the controller
            controller.setCurrentState(controller.tourState);
            throw new Exception("Duration cannot be negative");
        }

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

    @Override
    public void showDialogBox(MainsScreenController controller) {
        // create a text input dialog
        TextInputDialog td = new TextInputDialog("180");

        // setHeaderText
        td.setHeaderText("Enter the delivery duration in second");

        Optional<String> result = td.showAndWait();

        result.ifPresentOrElse(text -> {
            int duration = Integer.parseInt(text);
            controller.validate(duration);
        }, () -> {
            controller.cancel();
        });
    }
}
