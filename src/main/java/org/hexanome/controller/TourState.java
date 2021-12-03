package org.hexanome.controller;

public class TourState implements State {

    @Override
    public void enableButton(MainsScreenController controller) {
        controller.btnLoadRequest.setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(true);
        controller.getBtnAddRequest().setDisable(false);
    }
}
