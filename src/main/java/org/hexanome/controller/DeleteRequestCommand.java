package org.hexanome.controller;

import org.hexanome.model.Request;

public class DeleteRequestCommand implements Command {
    private MainsScreenController controller;
    private Request request;

    public DeleteRequestCommand(MainsScreenController controller, Request request) {
        this.controller = controller;
        this.request = request;
    }

    public void doCommand() {
        controller.getPlanning().removeRequest(request);
    }

    public void undoCommand() {
        controller.getPlanning().addRequest(request);
    }
}
