package org.hexanome.controller;

import org.hexanome.model.MapIF;
import org.hexanome.model.PlanningRequest;
import org.hexanome.model.Request;

public class AddRequestCommand implements Command {
    private PlanningRequest planning;
    private Request request;

    public AddRequestCommand(PlanningRequest planning, Request request) {
        this.planning = planning;
        this.request = request;
    }

    public void doCommand() {
        planning.addRequest(request);
    }

    public void undoCommand() {
        planning.removeRequest(request);
    }
}
