package org.hexanome.vue;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.hexanome.controller.MainsScreenController;

import java.io.IOException;
import java.net.URL;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private MainsScreenController controller;

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("javafx.platform", "desktop");

        /*
         * Définit l'user agent pour éviter l'exception
         * "Server returned HTTP response code: 403"
         */

        System.setProperty("http.agent", "Gluon Mobile/1.0.3");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainsScreen.fxml"));

        Parent root = loader.load();
        scene = new Scene(root, 1450, 768);

        URL url = this.getClass().getResource("stylesheet.css");
        String css = url.toExternalForm();
        scene.getStylesheets().add(css);

        controller = loader.getController();

        scene.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                controller.rightClick();
            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                controller.leftClick(null);
            }
        });

        stage.setScene(scene);
        stage.show();

        controller.initMap();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void start() {
        launch();
    }
}