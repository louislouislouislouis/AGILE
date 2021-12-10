package org.hexanome.controller;

import org.hexanome.model.Intersection;
import org.hexanome.model.Point;

public class ModifyRequestState implements State {

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            System.out.println(i);
            //return intersection
            /*// we must check if the intersection isn't isolated
            if (controller.getMap().isIsolated(i.getIdIntersection(), controller.getPlanning().getWarehouse().getId())) {
                throw new Exception("ISOLATED POINT");
            } else {
                // we change the selected intersection in the next state
                controller.addRequestState2.setPickUp(i);

                // we change the state of the controller
                controller.setCurrentState(controller.addRequestState2);
            }*/
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
