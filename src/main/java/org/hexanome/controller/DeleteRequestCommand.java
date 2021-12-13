package org.hexanome.controller;

import org.hexanome.controller.tsp.GraphAPI;
import org.hexanome.model.Request;

/**
 * Command from the command design pattern that is used to delete a request
 *
 * @author Gastronom'if
 */
public class DeleteRequestCommand implements Command {
    private MainsScreenController controller;
    private Request request;

    /**
     * Create the command which delete a request from the planning
     *
     * @param request    the request to delete
     * @param controller the controller
     */
    public DeleteRequestCommand(MainsScreenController controller, Request request) {
        this.controller = controller;
        this.request = request;
    }

    public void doCommand() {
        controller.getPlanning().removeRequest(request);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.DELETE_REQUEST(request, controller.getMap(), controller.getTour());

        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }

    public void undoCommand() {
        controller.getPlanning().addRequest(request);

        GraphAPI graphAPI = new GraphAPI();
        graphAPI.ADD_REQUEST(controller.getPlanning(), controller.getMap(), controller.getTour(), true);

        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }
}
