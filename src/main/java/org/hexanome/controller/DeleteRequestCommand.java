package org.hexanome.controller;

import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Request;

public class DeleteRequestCommand implements Command {
    private PlanningRequest planning;
    private Request request;

    public DeleteRequestCommand(PlanningRequest planning, Request request) {
        this.planning = planning;
        this.request = request;
    }

    public void doCommand() {
        planning.removeRequest(request);
    }

    public void undoCommand() {
        planning.addRequest(request);
    }
}
