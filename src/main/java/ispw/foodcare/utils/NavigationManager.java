package ispw.foodcare.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

public class NavigationManager {
    private NavigationManager(){}

    private static final Logger logger = Logger.getLogger(NavigationManager.class.getName());

    public static void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            Parent root = FXMLLoader.load(NavigationManager.class.getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            logger.warning("Errore nel cambio scena: " + fxmlPath);
            e.printStackTrace();
        }
    }

}
