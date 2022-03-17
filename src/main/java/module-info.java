module connect4_pattern {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires java.desktop;


    opens Client to javafx.fxml;
    exports Client;
}