module org.hexanome {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.hexanome to javafx.fxml;
    exports org.hexanome;
}