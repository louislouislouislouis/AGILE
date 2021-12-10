package org.hexanome.controller;

import org.hexanome.model.Intersection;
import org.hexanome.model.Point;

public class ModifyRequestState implements State {
    /*
            Missing tableview is not updating
            Missing refresh points in map
         */
    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            System.out.println(i);
            Point p = controller.getTableView().getSelectionModel().getSelectedItem();
            p.setAddress(i);
            controller.getMapView().removeLayer(controller.getIntersectionLayer());
            controller.setCurrentState(controller.tourState);
        }
    }


    @Override
    public void enableButton(MainsScreenController controller) {
        controller.getBtnLoadRequest().setDisable(false);
        controller.getBtnLoadMap().setDisable(false);
        controller.getBtnValidateRoute().setDisable(false);
        controller.getBtnAddRequest().setDisable(false);
        controller.getBtnRedo().setDisable(false);
        controller.getBtnUndo().setDisable(false);
        controller.getBtnStopCalcul().setDisable(false);
        controller.getBtnDeleteTableRow().setDisable(false);
        controller.getBtnEditTableRow().setDisable(false);
    }
}
