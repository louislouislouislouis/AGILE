package org.hexanome.controller;

import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.Request;

public class ModifyRequestCommand implements Command {
    private MainsScreenController controller;
    private Request oldRequest;
    private Request newRequest;

    /**
     * Create the command which add a request to the planning
     *
     * @param oldRequest the old request
     * @param newRequest the new request
     * @param controller the controller
     */
    public ModifyRequestCommand(MainsScreenController controller, Request oldRequest, Request newRequest) {
        this.controller = controller;
        this.oldRequest = oldRequest;
        this.newRequest = newRequest;
    }

    @Override
    public void doCommand() {
        controller.getPlanning().removeRequest(oldRequest);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.DELETE_REQUEST(oldRequest, controller.getMap(), controller.getTour());

        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
        
        controller.getPlanning().addRequest(newRequest);

        graphAPI.ADD_REQUEST(controller.getPlanning(), controller.getMap(), controller.getTour(), false);


        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }

    @Override
    public void undoCommand() {
        controller.getPlanning().removeRequest(newRequest);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.DELETE_REQUEST(newRequest, controller.getMap(), controller.getTour());

        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();

        controller.getPlanning().addRequest(oldRequest);


        graphAPI.ADD_REQUEST(controller.getPlanning(), controller.getMap(), controller.getTour(), false);


        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }
}
