module org.hexanome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;
    requires com.gluonhq.maps;
    requires javafx.web;
    opens org.hexanome.controller to javafx.fxml;
    exports org.hexanome.vue;
    exports org.hexanome.data;
    exports org.hexanome.model;
}