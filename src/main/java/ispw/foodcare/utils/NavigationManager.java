package ispw.foodcare.utils;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.logging.Level;


public final class NavigationManager {

    private NavigationManager() {}
    private static final Logger logger = Logger.getLogger(NavigationManager.class.getName());
    private static final String ERRORE = "Errore nel cambio scena. FXML: ";

    /*SCENE (Stage già noto) */
    public static void switchScene(Stage stage, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> ERRORE + fxmlPath);
        }
    }

    /* SCENE (da ActionEvent) */
    public static void switchScene(ActionEvent event, String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle(title);
            stage.show();
        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> ERRORE + fxmlPath);
        }
    }

    /* PANE (sostituisce il contenuto di #contentArea) */
    public static void switchPane(String fxmlPath, Node sourceNode) {
        switchPane(fxmlPath, sourceNode, null, null);
    }

    /* PANE con passaggio dati (generico e type-safe) */
    public static <C> void switchPane(String fxmlPath,
                                      Node sourceNode,
                                      Class<C> controllerType,
                                      Consumer<C> initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationManager.class.getResource(fxmlPath));
            Parent newContent = loader.load();

            if (controllerType != null && initializer != null) {
                Object controller = loader.getController();
                if (!controllerType.isInstance(controller)) {
                    throw new IllegalStateException("Controller di tipo inatteso: " +
                            controller.getClass().getName() + " atteso " + controllerType.getName());
                }
                initializer.accept(controllerType.cast(controller)); // ✅ passi la/e bean qui
            }

            AnchorPane contentArea = (AnchorPane) sourceNode.getScene().lookup("#contentArea");
            contentArea.getChildren().setAll(newContent);
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);

        } catch (IOException e) {
            logger.log(Level.SEVERE, e, () -> ERRORE + fxmlPath);
        }
    }
}
