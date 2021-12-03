package org.hexanome.controller;

public interface State {
    default void undo() {
    }

    ;

    default void redo() {
    }

    ;

    default void rightClick() {
    }

    ;

    default void leftClick() {
    }

    ;

    default void addRequest() {
    }

    ;

    default void modifyRequest() {
    }

    ;

    default void deleteRequest() {
    }

    ;

    default void loadMap() {
    }

    ;

    default void loadPlanning() {
    }

    ;

    default void computeTour() {
    }

    ;
    
}
