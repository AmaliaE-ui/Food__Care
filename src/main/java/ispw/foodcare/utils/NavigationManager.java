package ispw.foodcare.utils;

import ispw.foodcare.Main;
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

    //Versione per il Main.java (usa stage già noto)
    public static void switchScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            logger.warning("Errore nel cambio scena: " + fxmlPath);
            e.printStackTrace();
        }
    }

    //Versione per i contorller GUI (usa ActionEvent per ricavare lo Stage)
    public static void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root, 900, 600);

            // Recupera lo stage dalla sorgente dell’evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            logger.warning("Errore nel cambio scena con evento: " + fxmlPath);
            e.printStackTrace();
        }
    }
}
