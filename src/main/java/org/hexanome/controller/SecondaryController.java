package org.hexanome.controller;

import java.io.IOException;

import javafx.fxml.FXML;
import org.hexanome.vue.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}