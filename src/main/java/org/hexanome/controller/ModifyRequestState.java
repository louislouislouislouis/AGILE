package org.hexanome.controller;

import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import org.hexanome.model.*;
import org.hexanome.vue.ExceptionBox;

import java.util.LinkedList;

public class ModifyRequestState implements State {
    Request oldRequest;
    Request newRequest;
    DeliveryPoint oldDeliveryPoint;
    PickupPoint oldPickupPoint;
    DeliveryPoint newDeliveryPoint;
    PickupPoint newPickupPoint;
    Point pointToUpdate;
    @Override
    public void leftClick(MainsScreenController controller, Intersection i) throws Exception {
        if (i != null) {
            clickEdit(controller, i, controller.listOfCommands);
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

    public void clickEdit(MainsScreenController controller, Intersection i, ListOfCommands listOfCommands){
        System.out.println(i);
        pointToUpdate = controller.getTableView().getSelectionModel().getSelectedItem();
        System.out.println("Point to uptdate: " + pointToUpdate);
        LinkedList<Request> requestList = controller.getPlanning().getRequests();
        oldRequest = requestList.element();
        if(pointToUpdate.getType().equals("pickup")){
            oldDeliveryPoint = oldRequest.getDeliveryPoint();
            oldPickupPoint = oldRequest.getPickupPoint();
            newPickupPoint = new PickupPoint(pointToUpdate.getAddress(), oldPickupPoint.getDuration(), oldPickupPoint.getColor());
            newPickupPoint.setAddress(i);
            newRequest = new Request(newPickupPoint,oldDeliveryPoint);
        }else if(pointToUpdate.getType().equals("delivery")){
            oldDeliveryPoint = oldRequest.getDeliveryPoint();
            oldPickupPoint = oldRequest.getPickupPoint();
            newDeliveryPoint = new DeliveryPoint(pointToUpdate.getAddress(), oldDeliveryPoint.getDuration(), oldDeliveryPoint.getColor());
            newDeliveryPoint.setAddress(i);
            newRequest = new Request(oldPickupPoint,newDeliveryPoint);
        }else if(pointToUpdate.getType().equals("warehouse")){
            new ExceptionBox("Cannot change warehouse", "Behavioral problem").display();
            newRequest = oldRequest;
        }
        listOfCommands.add(new DeleteRequestCommand(controller, oldRequest));
        listOfCommands.add(new AddRequestCommand(controller, newRequest));
        controller.getMapView().removeLayer(controller.getIntersectionLayer());
        // we change the state of the controller
        controller.setCurrentState(controller.tourState);
    }
}
