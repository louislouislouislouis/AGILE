package org.hexanome.controller;

import java.io.IOException;
import javafx.fxml.FXML;
import org.hexanome.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
