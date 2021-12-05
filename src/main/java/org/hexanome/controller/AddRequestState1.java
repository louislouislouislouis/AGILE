package org.hexanome.controller;

import org.hexanome.model.Intersection;

public class AddRequestState1 implements State {
    // State when receiving the message addRequest() from InitialState
    // -> Wait for the user to enter a first point (corresponding to the pickup point)

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) {
        System.out.println(i);

        if (i != null) {
            // we change the selected intersection in the next state
            controller.addRequestState2.setSelectedIntersection(i);
            
            // we change the state of the controller
            controller.setCurrentState(controller.addRequestState2);
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
