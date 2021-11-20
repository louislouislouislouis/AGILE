module org.hexanome {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.xml;

    opens org.hexanome to javafx.fxml;
    exports org.hexanome;
}