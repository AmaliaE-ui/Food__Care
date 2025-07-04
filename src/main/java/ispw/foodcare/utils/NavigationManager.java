package ispw.foodcare.utils;

import ispw.foodcare.bean.NutritionistBean;
import ispw.foodcare.controller.viewcontroller.BookAppointmentGuiController;
import ispw.foodcare.controller.viewcontroller.NutritionistProfileGuiController;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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

    public static void switchPane(String fxmlPath, Node sourceNode, Object controllerConfig) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent newContent = loader.load();

            // Passaggio del NutritionistBean se presente
            if (controllerConfig != null) {
                Object controller = loader.getController();
                if (controller instanceof BookAppointmentGuiController && controllerConfig instanceof NutritionistBean) {
                    ((BookAppointmentGuiController) controller).setNutritionist((NutritionistBean) controllerConfig);
                }
                if(controller instanceof NutritionistProfileGuiController && controllerConfig instanceof NutritionistBean) {
                    ((NutritionistProfileGuiController) controller).setNutritionistBean( (NutritionistBean) controllerConfig);
                }
            }

            AnchorPane contentArea = (AnchorPane) sourceNode.getScene().lookup("#contentArea");
            contentArea.getChildren().setAll(newContent);
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
