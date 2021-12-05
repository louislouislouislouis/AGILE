package org.hexanome.controller;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> commands;
    private int i;

    public ListOfCommands() {
        i = -1;
        commands = new LinkedList<>();
    }

    public void add(Command c) {
        i++;
        commands.add(i, c);
        c.doCommand();
    }

    public void undo(Command c) {
        if (i >= 0) {
            commands.get(i).undoCommand();
            i--;
        }
    }

    public void redo(Command c) {
        i++;
        commands.get(i).doCommand();
    }
}
