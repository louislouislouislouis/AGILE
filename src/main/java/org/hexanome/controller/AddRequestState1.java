package org.hexanome.controller;

import org.hexanome.model.Intersection;

public class AddRequestState1 implements State {
    // State when receiving the message addRequest() from InitialState
    // -> Wait for the user to enter a first point (corresponding to the pickup point)

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) {

    }
}
