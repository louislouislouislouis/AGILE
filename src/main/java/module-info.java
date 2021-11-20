module org.hexanome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens org.hexanome.controller to javafx.fxml;
    exports org.hexanome.vue;
}