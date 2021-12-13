package org.hexanome.controller;

import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.Request;

/**
 * Command from the command design pattern that is used to add a request
 *
 * @author Gastronom'if
 */
public class AddRequestCommand implements Command {
    private MainsScreenController controller;
    private Request request;

    /**
     * Create the command which add a request to the planning
     *
     * @param request    the request to add
     * @param controller the controller
     */
    public AddRequestCommand(MainsScreenController controller, Request request) {
        this.controller = controller;
        this.request = request;
    }

    public void doCommand() {
        controller.getPlanning().addRequest(request);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.ADD_REQUEST(controller.getPlanning(), controller.getMap(), controller.getTour(), false);


        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }

    public void undoCommand() {
        controller.getPlanning().removeRequest(request);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.DELETE_REQUEST(request, controller.getMap(), controller.getTour());


        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }
}
