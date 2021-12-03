package org.hexanome.controller;

import org.hexanome.model.MapIF;

import java.io.File;

public interface State {
    default void undo(MainsScreenController controller) {
    }

    ;

    default void redo(MainsScreenController controller) {
    }

    ;

    default void rightClick(MainsScreenController controller) {
    }

    ;

    default void leftClick(MainsScreenController controller) {
    }

    ;

    default void addRequest(MainsScreenController controller) {
    }

    ;

    default void modifyRequest(MainsScreenController controller) {
    }

    ;

    default void deleteRequest(MainsScreenController controller) {
    }

    ;

    default void loadMap(MainsScreenController controller, MapIF map, File selectedFile) {
    }

    ;

    default void loadPlanning(MainsScreenController controller) {
    }

    ;

    default void computeTour(MainsScreenController controller) {
    }

    ;

    default void enableButton(MainsScreenController controller) {
    }

    ;

}
