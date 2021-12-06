package org.hexanome.controller;

import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Request;

public class AddRequestCommand implements Command {
    private MainsScreenController controller;
    private Request request;

    public AddRequestCommand(MainsScreenController controller, Request request) {
        this.controller = controller;
        this.request = request;
    }

    public void doCommand() {
        controller.getPlanning().addRequest(request);
        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }

    public void undoCommand() {
        controller.getPlanning().removeRequest(request);
        controller.updateRequestLayer();
        controller.updateTourLayer();
        controller.updateTableView();
    }
}
