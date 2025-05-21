module ispw.food_care {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens ispw.food_care to javafx.fxml;
    opens ispw.food_care.controller.guiController to javafx.fxml;

    exports ispw.food_care;
}