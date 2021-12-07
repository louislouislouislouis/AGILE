package org.hexanome.controller;

/**
 * Command interface from the command pattern that declare the methods doCommand and undoCommand
 *
 * @author Gastronom'if
 */
public interface Command {

    /**
     * Execute the command this
     */
    void doCommand();

    /**
     * Execute the reverse command of this
     */
    void undoCommand();
}
