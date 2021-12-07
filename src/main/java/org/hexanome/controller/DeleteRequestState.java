package org.hexanome.controller;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.hexanome.model.Request;

import java.util.Optional;

/**
 * This class is the representation of Delete Request State
 * <p>
 * State after entering the method deleteRequest from TourState
 * <p>
 * -> Wait for the user to validate as a way to confirm the delete action
 * <p>
 * -> Wait for the user to cancel this action
 *
 * @author Gastronom'if
 */
public class DeleteRequestState implements State {
    Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    @Override
    public void cancel(MainsScreenController controller) {
        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }

    @Override
    public void validate(MainsScreenController controller, int duration, ListOfCommands listOfCommands) {
        if (request != null) {
            // we add the command to the list of command
            // it will be executed there
            listOfCommands.add(new DeleteRequestCommand(controller, request));

            // we change the state of the controller
            controller.setCurrentState(controller.tourState);
        } else {
            System.out.println("REQUEST IS NULL");
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a Confirmation Dialog");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            /* if the user chose the ok button */
            controller.validate(-1);
        } else {
            /* or else the user either chose cancel or the close button */
            controller.cancel();
        }
    }
}
