module ca.cmpt213.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;

    opens ca.cmpt213.client to javafx.fxml;
    exports ca.cmpt213.client;
}