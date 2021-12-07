package org.hexanome.controller;

import java.util.LinkedList;

public class ListOfCommands {
    private LinkedList<Command> commands;
    private int currentIndex;

    public ListOfCommands() {
        currentIndex = -1;
        commands = new LinkedList<>();
    }

    /**
     * Add command c to this
     *
     * @param c the command to add
     */
    public void add(Command c) {
        int i = currentIndex + 1;
        while (i < commands.size()) {
            commands.remove(i);
        }
        currentIndex++;
        commands.add(currentIndex, c);
        c.doCommand();
    }

    /**
     * Temporary remove the last added command (this command may be reinserted again with redo)
     */
    public void undo() {
        if (currentIndex >= 0) {
            Command c = commands.get(currentIndex);
            currentIndex--;
            c.undoCommand();
        }
    }

    /**
     * Permanently remove the last added command (this command cannot be reinserted again with redo)
     */
    public void cancel() {
        if (currentIndex >= 0) {
            Command cde = commands.get(currentIndex);
            commands.remove(currentIndex);
            currentIndex--;
            cde.undoCommand();
        }
    }

    /**
     * Reinsert the last command removed by undo
     */
    public void redo() {
        if (currentIndex < commands.size() - 1) {
            currentIndex++;
            commands.get(currentIndex).doCommand();
        }
    }

    /**
     * Permanently remove all commands from the list
     */
    public void reset() {
        currentIndex = -1;
        commands.clear();
    }
}
