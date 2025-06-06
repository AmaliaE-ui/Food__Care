module ispw.foodcare {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens ispw.foodcare to javafx.fxml;
    opens ispw.foodcare.controller.guicontroller to javafx.fxml;
    opens ispw.foodcare.bean to javafx.fxml;


    exports ispw.foodcare;
}