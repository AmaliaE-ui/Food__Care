module ispw.foodcare {
    requires javafx.fxml;
    requires javafx.web;

    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires com.calendarfx.view;

    opens ispw.foodcare to javafx.fxml;
    opens ispw.foodcare.controller.guicontroller to javafx.fxml;
    opens ispw.foodcare.bean to javafx.fxml;


    exports ispw.foodcare;
    opens ispw.foodcare.dao to javafx.fxml;
    opens ispw.foodcare.controller.guicontroller to javafx.fxml;
    opens ispw.foodcare.controller.viewcontroller to javafx.fxml;
}