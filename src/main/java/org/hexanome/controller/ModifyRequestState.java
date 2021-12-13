package org.hexanome.controller;

import org.hexanome.model.*;
import org.hexanome.vue.ExceptionBox;

import java.util.*;

public class ModifyRequestState implements State {
    Request oldRequest;
    Request newRequest;
    DeliveryPoint oldDeliveryPoint;
    PickupPoint oldPickupPoint;
    DeliveryPoint newDeliveryPoint;
    PickupPoint newPickupPoint;
    Point pointToUpdate;
    int position1;
    int position;

    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            clickEdit(controller, i, controller.listOfCommands);
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
        controller.getBtnStopCalcul().setDisable(true);
        controller.getBtnDeleteTableRow().setDisable(true);
        controller.getBtnEditTableRow().setDisable(true);
    }

    private void clickEdit(MainsScreenController controller, Intersection i, ListOfCommands listOfCommands) {
        System.out.println(i);
        pointToUpdate = controller.getTableView().getSelectionModel().getSelectedItem();
        System.out.println("Point to uptdate: " + pointToUpdate);
        LinkedList<Request> requestList = controller.getPlanning().getRequests();
        System.out.println(requestList);
        oldRequest = requestList.element();


        if (pointToUpdate.getType().equals("pickup")) {
            oldDeliveryPoint = oldRequest.getDeliveryPoint();
            oldPickupPoint = oldRequest.getPickupPoint();
            newPickupPoint = new PickupPoint(pointToUpdate.getAddress(), oldPickupPoint.getDuration(), oldPickupPoint.getColor());
            newPickupPoint.setAddress(i);
            newRequest = new Request(newPickupPoint, oldDeliveryPoint);
        } else if (pointToUpdate.getType().equals("delivery")) {
            oldDeliveryPoint = oldRequest.getDeliveryPoint();
            oldPickupPoint = oldRequest.getPickupPoint();
            newDeliveryPoint = new DeliveryPoint(pointToUpdate.getAddress(), oldDeliveryPoint.getDuration(), oldDeliveryPoint.getColor());
            newDeliveryPoint.setAddress(i);
            newRequest = new Request(oldPickupPoint, newDeliveryPoint);
        } else if (pointToUpdate.getType().equals("warehouse")) {
            new ExceptionBox("Cannot change warehouse", "Behavioral problem").display();
            newRequest = oldRequest;
        }

        position1 = requestList.indexOf(oldRequest);

        System.out.println(position1);
        listOfCommands.add(new ModifyRequestCommand(controller, oldRequest, newRequest));
        //requestList.set(position, newRequest);
        position = requestList.indexOf(newRequest);
        System.out.println(position);
        LinkedList<Request> newRequestList = controller.getPlanning().getRequests();
        //requestList = controller.getPlanning().getRequests();
        //Collections.swap(requestList, position, requestList.size()-1);
        Collections.swap(newRequestList, position, requestList.size()-1);
        System.out.println("New request list" + requestList);
        System.out.println("New request list2" + newRequestList);
        controller.getMapView().removeLayer(controller.getIntersectionLayer());
        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }
}
