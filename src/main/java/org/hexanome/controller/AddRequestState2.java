package org.hexanome.controller;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import org.hexanome.model.Intersection;

import java.util.Optional;

public class AddRequestState2 implements State {

    Intersection pickUp;

    public void setPickUp(Intersection i) {
        pickUp = i;
    }

    @Override
    public void cancel(MainsScreenController controller) {
        // we clear the view
        controller.getMapView().removeLayer(controller.getIntersectionLayer());

        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }

    @Override
    public void validate(MainsScreenController controller, int duration, ListOfCommands listOfCommands) {
        if (duration > 0) {
            // we change the selected intersection in the next state
            controller.addRequestState3.setPickUp(pickUp);

            // we change the duration in the next state
            controller.addRequestState3.setPickUpDuration(duration);

            // we change the state of the controller
            controller.setCurrentState(controller.addRequestState3);
        } else {
            System.out.println("duration cannot be negative");
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
    }

    @Override
    public void showDialogBox(MainsScreenController controller) {
        // create a text input dialog
        TextInputDialog td = new TextInputDialog("180");

        // setHeaderText
        td.setHeaderText("Enter the pickup duration in second");

        Optional<String> result = td.showAndWait();

        result.ifPresentOrElse(text -> {
            int duration = Integer.parseInt(text);
            controller.validate(duration);
        }, () -> {
            controller.cancel();
        });


    }
}
