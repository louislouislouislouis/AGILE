package org.hexanome.controller;

public class ReverseCommand implements Command {
    private Command cmd;

    public ReverseCommand(Command cmd) {
        this.cmd = cmd;
    }

    public void doCommand() {
        cmd.undoCommand();
    }

    public void undoCommand() {
        cmd.doCommand();
    }
}
