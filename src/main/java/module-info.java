module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    exports org.example;
    exports com.example;
    exports escenario2;
    opens com.example to javafx.fxml;
    opens org.example to javafx.fxml;
    opens escenario2 to javafx.fxml;
}